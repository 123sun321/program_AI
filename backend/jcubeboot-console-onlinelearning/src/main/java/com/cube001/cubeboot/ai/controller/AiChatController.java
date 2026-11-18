package com.cube001.cubeboot.ai.controller;

import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.base.controller.BaseController;
import com.cube001.cubeboot.service.ai.AiChatService;
import com.cube001.cubeboot.service.ai.dto.AnalyzeTaskDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Api(value = "AiChatController", description = "AI 聊天控制器")
@RestController
@RequestMapping(value = "${cubeboot.app-manager-path}/ai")
public class AiChatController extends BaseController {

    @Autowired
    private AiChatService aiChatService;

    @ApiOperation(value = "发送 AI 聊天消息")
    @PostMapping("/chat")
    public Map<String, Object> chat(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        String codeContent = request.get("codeContent");

        try {
            String reply = aiChatService.chat(message, codeContent);

            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("data", reply);
            result.put("message", "success");
            return result;
        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("code", 500);
            errorResult.put("message", "AI 服务调用失败：" + e.getMessage());
            return errorResult;
        }
    }

    @ApiOperation(value = "启动知识点分析任务")
    @PostMapping("/startAnalyze")
    public Map<String, Object> startAnalyze() {
        Long studentId = ShiroKit.getUserId();

        try {
            AnalyzeTaskDto cached = aiChatService.getCachedTask(studentId);
            if (cached != null) {
                Map<String, Object> result = new HashMap<>();
                result.put("code", 200);
                result.put("data", cached);
                result.put("message", "使用缓存结果");
                return result;
            }

            String taskId = aiChatService.startKnowledgeAnalysis(studentId);
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("data", taskId);
            result.put("message", "分析任务已启动");
            return result;
        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("code", 500);
            errorResult.put("message", "启动失败：" + e.getMessage());
            return errorResult;
        }
    }

    @ApiOperation(value = "查询分析任务进度")
    @GetMapping("/analyzeProgress")
    public Map<String, Object> analyzeProgress(@RequestParam("taskId") String taskId) {
        try {
            AnalyzeTaskDto task = aiChatService.getTaskProgress(taskId);
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("data", task);
            result.put("message", "success");
            return result;
        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("code", 500);
            errorResult.put("message", "查询失败：" + e.getMessage());
            return errorResult;
        }
    }

    @ApiOperation(value = "启动课程维度知识点分析")
    @PostMapping("/startCourseAnalyze")
    public Map<String, Object> startCourseAnalyze(@RequestBody Map<String, Long> request) {
        Long studentId = request.get("studentId");
        Long courseId = request.get("courseId");

        try {
            AnalyzeTaskDto cached = aiChatService.getCachedCourseTask(studentId, courseId);
            if (cached != null) {
                Map<String, Object> result = new HashMap<>();
                result.put("code", 200);
                result.put("data", cached);
                result.put("message", "使用缓存结果");
                return result;
            }

            String taskId = aiChatService.startCourseKnowledgeAnalysis(studentId, courseId);
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("data", taskId);
            result.put("message", "分析任务已启动");
            return result;
        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("code", 500);
            errorResult.put("message", "启动失败：" + e.getMessage());
            return errorResult;
        }
    }
}
