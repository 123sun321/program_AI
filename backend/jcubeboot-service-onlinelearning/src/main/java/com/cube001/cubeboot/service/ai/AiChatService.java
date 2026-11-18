package com.cube001.cubeboot.service.ai;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.service.ai.dto.AnalyzeTaskDto;
import com.cube001.cubeboot.service.ai.dto.KnowledgeMasteryDto;
import com.cube001.cubeboot.service.ai.dto.PracticeRecordDto;
import com.cube001.cubeboot.service.base.mapper.StudentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
public class AiChatService {

    private static final Logger log = LoggerFactory.getLogger(AiChatService.class);

    @Value("${ai.dashscope.api-key}")
    private String apiKey;

    @Value("${ai.dashscope.model:qwen-turbo}")
    private String model;

    @Value("${ai.dashscope.system-prompt:你是一个专业的前端开发学习助手，主要帮助学生理解代码、学习编程知识。请用简洁易懂的语言解释，多用代码示例说明。}")
    private String systemPrompt;

    private static final String API_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    @Qualifier("analysisExecutor")
    private Executor analysisExecutor;

    public String chat(String userMessage, String codeContent) {
        List<Map<String, String>> messages = new ArrayList<>();

        Map<String, String> systemMsg = new HashMap<>();
        systemMsg.put("role", "system");
        systemMsg.put("content", systemPrompt);
        messages.add(systemMsg);

        StringBuilder userContent = new StringBuilder();
        if (codeContent != null && !codeContent.isEmpty()) {
            userContent.append("当前代码内容：\n```\n").append(codeContent).append("\n```\n\n");
        }
        userContent.append("用户问题：").append(userMessage);

        Map<String, String> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", userContent.toString());
        messages.add(userMsg);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("messages", messages);

        try {
            HttpResponse response = HttpRequest.post(API_URL)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .body(JSON.toJSONString(requestBody))
                    .timeout(60000)
                    .execute();

            if (response.getStatus() == 200) {
                JSONObject json = JSON.parseObject(response.body());
                JSONArray choices = json.getJSONArray("choices");
                if (choices != null && choices.size() > 0) {
                    return choices.getJSONObject(0).getJSONObject("message").getString("content");
                }
            }
            return "AI 服务调用失败，状态码：" + response.getStatus();
        } catch (Exception e) {
            throw new RuntimeException("AI 服务调用失败：" + e.getMessage(), e);
        }
    }

    public String startKnowledgeAnalysis(Long userId) {
        Student student = studentMapper.getStudentByUserId(userId);
        if (student == null) {
            throw new RuntimeException("未找到学生信息");
        }
        Long studentId = student.getId();
        
        String taskId = UUID.randomUUID().toString().replace("-", "");
        createTaskRecord(taskId, studentId);

        CompletableFuture.runAsync(() -> {
            try {
                analyzeKnowledgeAsync(taskId, studentId);
            } catch (Exception e) {
                log.error("知识点分析失败", e);
                updateTaskStatus(taskId, "failed", 0, e.getMessage());
            }
        }, analysisExecutor);

        return taskId;
    }

    public AnalyzeTaskDto getTaskProgress(String taskId) {
        String sql = "SELECT * FROM t_ai_analysis_task WHERE id = ?";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, taskId);
        if (rows.isEmpty()) {
            throw new RuntimeException("任务不存在");
        }

        Map<String, Object> row = rows.get(0);
        AnalyzeTaskDto dto = new AnalyzeTaskDto();
        dto.setTaskId(taskId);
        dto.setStatus((String) row.get("status"));
        dto.setProgress(row.get("progress") != null ? (Integer) row.get("progress") : 0);
        dto.setTotalItems(row.get("total_items") != null ? (Integer) row.get("total_items") : 0);
        dto.setCompletedItems(row.get("completed_items") != null ? (Integer) row.get("completed_items") : 0);
        dto.setErrorMessage((String) row.get("error_message"));

        if ("completed".equals(dto.getStatus()) && row.get("result") != null) {
            String resultJson = (String) row.get("result");
            dto.setResult(JSON.parseArray(resultJson, KnowledgeMasteryDto.class));
        }

        return dto;
    }

    public AnalyzeTaskDto getCachedTask(Long userId) {
        Student student = studentMapper.getStudentByUserId(userId);
        if (student == null) {
            return null;
        }
        Long studentId = student.getId();
        
        String sql = "SELECT * FROM t_ai_analysis_task WHERE student_id = ? AND task_type = 'knowledge_mastery' AND status = 'completed' AND completion_time > DATE_SUB(NOW(), INTERVAL 1 DAY) ORDER BY completion_time DESC LIMIT 1";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, studentId);
        if (rows.isEmpty()) {
            return null;
        }

        Map<String, Object> row = rows.get(0);
        String taskId = (String) row.get("id");
        AnalyzeTaskDto dto = new AnalyzeTaskDto();
        dto.setTaskId(taskId);
        dto.setStatus("completed");
        dto.setProgress(100);
        dto.setTotalItems(row.get("total_items") != null ? (Integer) row.get("total_items") : 0);
        dto.setCompletedItems(dto.getTotalItems());

        if (row.get("result") != null) {
            String resultJson = (String) row.get("result");
            dto.setResult(JSON.parseArray(resultJson, KnowledgeMasteryDto.class));
        }

        return dto;
    }

    private void createTaskRecord(String taskId, Long studentId) {
        String sql = "INSERT INTO t_ai_analysis_task (id, student_id, task_type, status, progress, creation_time) VALUES (?, ?, 'knowledge_mastery', 'pending', 0, NOW())";
        jdbcTemplate.update(sql, taskId, studentId);
    }

    private void createCourseTaskRecord(String taskId, Long studentId, Long courseId) {
        String sql = "INSERT INTO t_ai_analysis_task (id, student_id, course_id, task_type, status, progress, creation_time) VALUES (?, ?, ?, 'knowledge_mastery', 'pending', 0, NOW())";
        jdbcTemplate.update(sql, taskId, studentId, courseId);
    }

    public String startCourseKnowledgeAnalysis(Long studentId, Long courseId) {
        String taskId = UUID.randomUUID().toString().replace("-", "");
        createCourseTaskRecord(taskId, studentId, courseId);

        CompletableFuture.runAsync(() -> {
            try {
                analyzeCourseKnowledgeAsync(taskId, studentId, courseId);
            } catch (Exception e) {
                log.error("课程知识点分析失败", e);
                updateTaskStatus(taskId, "failed", 0, e.getMessage());
            }
        }, analysisExecutor);

        return taskId;
    }

    public AnalyzeTaskDto getCachedCourseTask(Long studentId, Long courseId) {
        String sql = "SELECT * FROM t_ai_analysis_task WHERE student_id = ? AND course_id = ? AND task_type = 'knowledge_mastery' AND status = 'completed' AND completion_time > DATE_SUB(NOW(), INTERVAL 1 DAY) ORDER BY completion_time DESC LIMIT 1";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, studentId, courseId);
        if (rows.isEmpty()) {
            return null;
        }

        Map<String, Object> row = rows.get(0);
        String taskId = (String) row.get("id");
        AnalyzeTaskDto dto = new AnalyzeTaskDto();
        dto.setTaskId(taskId);
        dto.setStatus("completed");
        dto.setProgress(100);
        dto.setTotalItems(row.get("total_items") != null ? (Integer) row.get("total_items") : 0);
        dto.setCompletedItems(dto.getTotalItems());

        if (row.get("result") != null) {
            String resultJson = (String) row.get("result");
            dto.setResult(JSON.parseArray(resultJson, KnowledgeMasteryDto.class));
        }

        return dto;
    }

    private void analyzeCourseKnowledgeAsync(String taskId, Long studentId, Long courseId) {
        log.info("开始分析学生 {} 在课程 {} 中的知识点掌握度", studentId, courseId);

        List<PracticeRecordDto> records = queryCoursePracticeRecords(studentId, courseId);
        if (records.isEmpty()) {
            updateTaskStatus(taskId, "completed", 100, "[]");
            return;
        }

        Map<String, List<PracticeRecordDto>> knowledgeGroup = groupByKnowledge(records);
        int totalItems = knowledgeGroup.size();
        log.info("共提取 {} 个知识点", totalItems);

        updateTaskProgress(taskId, "running", 0, totalItems, 0);

        List<KnowledgeMasteryDto> results = Collections.synchronizedList(new ArrayList<>());
        int[] completed = {0};

        knowledgeGroup.forEach((knowledge, knowledgeRecords) -> {
            try {
                String codeContext = buildCodeContext(knowledgeRecords);
                String prompt = buildKnowledgePrompt(knowledge, knowledgeRecords.size(), codeContext);
                String aiResponse = chat(prompt, null);
                KnowledgeMasteryDto dto = parseAiResponse(aiResponse, knowledge, knowledgeRecords.size());
                results.add(dto);

                completed[0]++;
                int progress = (int) ((completed[0] * 100.0) / totalItems);
                updateTaskProgress(taskId, "running", progress, totalItems, completed[0]);
            } catch (Exception e) {
                log.error("分析知识点 {} 失败", knowledge, e);
                completed[0]++;
            }
        });

        String resultJson = JSON.toJSONString(results);
        updateTaskStatus(taskId, "completed", 100, null);

        String updateResultSql = "UPDATE t_ai_analysis_task SET result = ? WHERE id = ?";
        jdbcTemplate.update(updateResultSql, resultJson, taskId);

        log.info("学生 {} 在课程 {} 中的知识点分析完成", studentId, courseId);
    }

    private List<PracticeRecordDto> queryCoursePracticeRecords(Long studentId, Long courseId) {
        String sql = "SELECT " +
                "ej.id as join_id, " +
                "ej.exercise_id, " +
                "ej.training_id, " +
                "e.name as exercise_name, " +
                "e.knowledge, " +
                "ej.creation_time as join_time, " +
                "c.html, c.css, c.js " +
                "FROM t_exercise_join ej " +
                "LEFT JOIN t_exercise e ON ej.exercise_id = e.id " +
                "LEFT JOIN t_training tr ON ej.training_id = tr.id " +
                "LEFT JOIN t_code c ON tr.code_id = c.id " +
                "WHERE ej.student_id = ? AND e.course_id = ? AND ej.is_deleted = 0 AND e.knowledge IS NOT NULL AND e.knowledge != '' " +
                "ORDER BY ej.creation_time DESC";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, studentId, courseId);
        return rows.stream().map(row -> {
            PracticeRecordDto dto = new PracticeRecordDto();
            dto.setTrainingId(getLong(row, "training_id"));
            dto.setExerciseId(getLong(row, "exercise_id"));
            dto.setExerciseName(getString(row, "exercise_name"));
            dto.setKnowledge(getString(row, "knowledge"));
            dto.setHtml(getString(row, "html"));
            dto.setCss(getString(row, "css"));
            dto.setJs(getString(row, "js"));
            dto.setJoinTime(getDate(row, "join_time"));
            return dto;
        }).collect(Collectors.toList());
    }

    private void updateTaskStatus(String taskId, String status, int progress, String errorMessage) {
        String sql = "UPDATE t_ai_analysis_task SET status = ?, progress = ?, completion_time = NOW()";
        List<Object> params = new ArrayList<>(Arrays.asList(status, progress));
        if (errorMessage != null) {
            sql += ", error_message = ?";
            params.add(errorMessage);
        }
        sql += " WHERE id = ?";
        params.add(taskId);
        jdbcTemplate.update(sql, params.toArray());
    }

    private void updateTaskProgress(String taskId, String status, int progress, int total, int completed) {
        String sql = "UPDATE t_ai_analysis_task SET status = ?, progress = ?, total_items = ?, completed_items = ? WHERE id = ?";
        jdbcTemplate.update(sql, status, progress, total, completed, taskId);
    }

    private void analyzeKnowledgeAsync(String taskId, Long studentId) {
        log.info("开始分析学生 {} 的知识点掌握度", studentId);

        List<PracticeRecordDto> records = queryPracticeRecords(studentId);
        if (records.isEmpty()) {
            updateTaskStatus(taskId, "completed", 100, "[]");
            return;
        }

        Map<String, List<PracticeRecordDto>> knowledgeGroup = groupByKnowledge(records);
        int totalItems = knowledgeGroup.size();
        log.info("共提取 {} 个知识点", totalItems);

        updateTaskProgress(taskId, "running", 0, totalItems, 0);

        List<KnowledgeMasteryDto> results = Collections.synchronizedList(new ArrayList<>());
        int[] completed = {0};

        knowledgeGroup.forEach((knowledge, knowledgeRecords) -> {
            try {
                String codeContext = buildCodeContext(knowledgeRecords);
                String prompt = buildKnowledgePrompt(knowledge, knowledgeRecords.size(), codeContext);
                String aiResponse = chat(prompt, null);
                KnowledgeMasteryDto dto = parseAiResponse(aiResponse, knowledge, knowledgeRecords.size());
                results.add(dto);

                completed[0]++;
                int progress = (int) ((completed[0] * 100.0) / totalItems);
                updateTaskProgress(taskId, "running", progress, totalItems, completed[0]);
            } catch (Exception e) {
                log.error("分析知识点 {} 失败", knowledge, e);
                completed[0]++;
            }
        });

        String resultJson = JSON.toJSONString(results);
        updateTaskStatus(taskId, "completed", 100, null);

        String updateResultSql = "UPDATE t_ai_analysis_task SET result = ? WHERE id = ?";
        jdbcTemplate.update(updateResultSql, resultJson, taskId);

        log.info("学生 {} 的知识点分析完成", studentId);
    }

    private List<PracticeRecordDto> queryPracticeRecords(Long studentId) {
        String sql = "SELECT " +
                "ej.id as join_id, " +
                "ej.exercise_id, " +
                "ej.training_id, " +
                "e.name as exercise_name, " +
                "e.knowledge, " +
                "ej.creation_time as join_time, " +
                "c.html, c.css, c.js " +
                "FROM t_exercise_join ej " +
                "LEFT JOIN t_exercise e ON ej.exercise_id = e.id " +
                "LEFT JOIN t_training tr ON ej.training_id = tr.id " +
                "LEFT JOIN t_code c ON tr.code_id = c.id " +
                "WHERE ej.student_id = ? AND ej.is_deleted = 0 AND e.knowledge IS NOT NULL AND e.knowledge != '' " +
                "ORDER BY ej.creation_time DESC";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, studentId);
        return rows.stream().map(row -> {
            PracticeRecordDto dto = new PracticeRecordDto();
            dto.setTrainingId(getLong(row, "training_id"));
            dto.setExerciseId(getLong(row, "exercise_id"));
            dto.setExerciseName(getString(row, "exercise_name"));
            dto.setKnowledge(getString(row, "knowledge"));
            dto.setHtml(getString(row, "html"));
            dto.setCss(getString(row, "css"));
            dto.setJs(getString(row, "js"));
            dto.setJoinTime(getDate(row, "join_time"));
            return dto;
        }).collect(Collectors.toList());
    }

    private Map<String, List<PracticeRecordDto>> groupByKnowledge(List<PracticeRecordDto> records) {
        Map<String, List<PracticeRecordDto>> group = new LinkedHashMap<>();

        for (PracticeRecordDto record : records) {
            Set<String> tags = extractKnowledgeTags(record.getKnowledge());
            for (String tag : tags) {
                group.computeIfAbsent(tag, k -> new ArrayList<>()).add(record);
            }
        }

        return group;
    }

    private Set<String> extractKnowledgeTags(String knowledgeText) {
        if (knowledgeText == null || knowledgeText.trim().isEmpty()) {
            return Collections.emptySet();
        }

        try {
            String prompt = "请从以下文本中提取前端知识点标签（如DOM操作、CSS动画、事件监听等），用逗号分隔，不要解释：\n" + knowledgeText;
            String response = chat(prompt, null);

            return Arrays.stream(response.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty() && s.length() < 30)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        } catch (Exception e) {
            log.warn("知识点提取失败，使用原始文本", e);
            Set<String> fallback = new LinkedHashSet<>();
            if (knowledgeText.contains(",")) {
                Arrays.stream(knowledgeText.split(",")).map(String::trim).filter(s -> !s.isEmpty()).forEach(fallback::add);
            } else if (knowledgeText.contains("\n")) {
                Arrays.stream(knowledgeText.split("\n")).map(s -> s.replaceAll("^\\d+[、.]", "").trim()).filter(s -> !s.isEmpty()).forEach(fallback::add);
            } else {
                fallback.add(knowledgeText.trim());
            }
            return fallback;
        }
    }

    private String buildCodeContext(List<PracticeRecordDto> records) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (PracticeRecordDto record : records) {
            if (count >= 5) break;
            if (record.getHtml() != null && !record.getHtml().trim().isEmpty()) {
                sb.append("--- 练习 ").append(count + 1).append(" HTML ---\n").append(record.getHtml()).append("\n\n");
            }
            if (record.getCss() != null && !record.getCss().trim().isEmpty()) {
                sb.append("--- 练习 ").append(count + 1).append(" CSS ---\n").append(record.getCss()).append("\n\n");
            }
            if (record.getJs() != null && !record.getJs().trim().isEmpty()) {
                sb.append("--- 练习 ").append(count + 1).append(" JavaScript ---\n").append(record.getJs()).append("\n\n");
            }
            count++;
        }
        return sb.length() > 8000 ? sb.substring(0, 8000) : sb.toString();
    }

    private String buildKnowledgePrompt(String knowledge, int exerciseCount, String codeContext) {
        return String.format(
                "你是一个鼓励型的前端学习评估专家。请分析学生在以下知识点的练习情况：\n\n" +
                "知识点：%s\n" +
                "练习次数：%d次\n" +
                "代码片段汇总：\n%s\n\n" +
                "评分标准（请适当宽松，鼓励学生）：\n" +
                "- 65分以上：已掌握基础概念，能完成基本功能\n" +
                "- 80分以上：掌握较好，能独立实现功能\n" +
                "- 90分以上：掌握优秀，代码规范且有优化意识\n\n" +
                "请从以下维度评估：\n" +
                "1. 掌握度评分（0-100，请适当宽松）\n" +
                "2. 存在的薄弱环节\n" +
                "3. 具体改进建议（至少2条）\n\n" +
                "仅返回JSON格式，不要包含其他文字：\n" +
                "{\n" +
                "  \"masteryScore\": 75,\n" +
                "  \"weakness\": \"薄弱环节描述\",\n" +
                "  \"suggestions\": [\"建议1\", \"建议2\"]\n" +
                "}",
                knowledge, exerciseCount, codeContext.isEmpty() ? "无代码记录" : codeContext
        );
    }

    private KnowledgeMasteryDto parseAiResponse(String aiResponse, String knowledge, int exerciseCount) {
        KnowledgeMasteryDto dto = new KnowledgeMasteryDto();
        dto.setKnowledge(knowledge);
        dto.setExerciseCount(exerciseCount);

        try {
            String jsonStr = aiResponse;
            int start = aiResponse.indexOf("{");
            int end = aiResponse.lastIndexOf("}");
            if (start >= 0 && end > start) {
                jsonStr = aiResponse.substring(start, end + 1);
            }

            JSONObject json = JSON.parseObject(jsonStr);
            dto.setMasteryScore(json.getInteger("masteryScore"));
            dto.setWeakness(json.getString("weakness"));
            dto.setSuggestions(json.getJSONArray("suggestions") != null ?
                    json.getJSONArray("suggestions").toJavaList(String.class) :
                    new ArrayList<>());

            if (dto.getMasteryScore() == null) dto.setMasteryScore(60);
            if (dto.getWeakness() == null) dto.setWeakness("待进一步分析");
            if (dto.getSuggestions().isEmpty()) dto.getSuggestions().add("建议多加练习");

            if (dto.getMasteryScore() >= 65) {
                dto.setLevel("excellent");
            } else if (dto.getMasteryScore() >= 50) {
                dto.setLevel("good");
            } else {
                dto.setLevel("weak");
            }
        } catch (Exception e) {
            log.warn("解析AI响应失败", e);
            dto.setMasteryScore(60);
            dto.setLevel("good");
            dto.setWeakness("AI分析结果解析失败");
            dto.setSuggestions(Arrays.asList("请重新查看代码练习"));
        }

        return dto;
    }

    private Long getLong(Map<String, Object> row, String key) {
        Object val = row.get(key);
        return val != null ? Long.valueOf(val.toString()) : null;
    }

    private String getString(Map<String, Object> row, String key) {
        Object val = row.get(key);
        return val != null ? val.toString() : null;
    }

    private Date getDate(Map<String, Object> row, String key) {
        Object val = row.get(key);
        if (val instanceof Date) return (Date) val;
        return null;
    }
}
