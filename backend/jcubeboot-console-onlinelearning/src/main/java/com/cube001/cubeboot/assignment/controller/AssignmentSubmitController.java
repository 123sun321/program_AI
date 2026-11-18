package com.cube001.cubeboot.assignment.controller;


import com.baomidou.mybatisplus.annotations.SqlParser;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.assignment.entity.Assignment;
import com.cube001.cubeboot.assignment.entity.AssignmentSubmit;
import com.cube001.cubeboot.assignment.entity.AssignmentSubmitWithCode;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.domain.tenant.TenantFliter;
import com.cube001.cubeboot.core.domain.tenant.TenantIdUtil;
import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.core.validateGroup.EditChecks;
import com.cube001.cubeboot.service.assignment.AssignmentService;
import com.cube001.cubeboot.service.assignment.AssignmentSubmitService;
import com.cube001.cubeboot.service.assignment.dto.*;
import com.cube001.cubeboot.service.base.StudentService;
import com.cube001.cubeboot.service.training.CodeService;
import com.cube001.cubeboot.service.training.TrainingService;
import com.cube001.cubeboot.service.training.dto.*;
import com.cube001.cubeboot.training.entity.Code;
import com.cube001.cubeboot.training.entity.Training;
import com.cube001.cubeboot.websocket.WebSocket;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2018 - 2019, ZhengZho uChuangZhi. Co., Ltd
 * FileName:
 * 类的详细说明
 * 提交作业详情
 *
 * @author 张人杰
 * @version 1.0
 * @date 2025/3/22 13:14
 */


@Api(value = "assignmentSubmit Controller ", description = "作业提交 Controller")
@RestController
@RequestMapping("${cubeboot.app-manager-path}/assignmentsubmit")
@RequiresPermissions("assignmentsubmit")
@EnableAsync
public class AssignmentSubmitController implements IApplicationService {

    @Autowired
    private AssignmentSubmitService assignmentSubmitService;
    @Autowired
    private AssignmentSubmitConverter assignmentSubmitConverter;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TrainingService trainingService;
    @Autowired
    private TrainingConverter trainingConverter;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private CodeService codeService;
    @Autowired
    private CodeConverter codeConverter;

    @ApiOperation(value = "搜索/分页展示", notes = "作业提交的分页展示")
    @RequestMapping(value = "page", method = RequestMethod.POST)
    @RequiresPermissions("assignmentsubmit:page")
    public Page<ListAssignmentSubmitDto> page(@RequestBody AssignmentCommenSearchDto<AssignmentSubmit> searchDto) {
        Page<AssignmentSubmit> assignmentSubmitList = null;
        Map<String, String> map = new HashMap<>();

        //作业编号
//        map.put("assignment_id","ams.");
        map.put("id", "am.");
//        map.put("grade_id", "bs.");
        map.put("score", "ams.");
        map.put("creation_time", "ams.");

//        String[][] arr={{"name", "code"},{"creation_time"}};

        if (searchDto.isSubmit()!=null){
            if (searchDto.isSubmit()){
                if (searchDto.getGradeID()!=null){
                    assignmentSubmitList = assignmentSubmitService.getAssignmentSubmitList(searchDto.getPlusPage(), Long.valueOf(searchDto.getForm().get("id").toString()),searchDto.getCourseID(), searchDto.formToEntityWrapperWithSearch(new String[]{"name", "code"}, "bs.", map).eq("bs.grade_id",searchDto.getGradeID()).isNotNull("ams.id"));
                }else if (searchDto.getGroupID()!=null){
                    assignmentSubmitList = assignmentSubmitService.getAssignmentSubmitList(searchDto.getPlusPage(), Long.valueOf(searchDto.getForm().get("id").toString()),searchDto.getCourseID(), searchDto.formToEntityWrapperWithSearch(new String[]{"name", "code"}, "bs.", map).eq("cj.course_group_id",searchDto.getGroupID()).isNotNull("ams.id"));
                }
            }else {
                if (searchDto.getGradeID()!=null){
                    assignmentSubmitList = assignmentSubmitService.getAssignmentSubmitList(searchDto.getPlusPage(), Long.valueOf(searchDto.getForm().get("id").toString()),searchDto.getCourseID(), searchDto.formToEntityWrapperWithSearch(new String[]{"name", "code"}, "bs.", map).eq("bs.grade_id",searchDto.getGradeID()).isNull("ams.id"));
                }else if (searchDto.getGroupID()!=null){
                    assignmentSubmitList = assignmentSubmitService.getAssignmentSubmitList(searchDto.getPlusPage(), Long.valueOf(searchDto.getForm().get("id").toString()),searchDto.getCourseID(), searchDto.formToEntityWrapperWithSearch(new String[]{"name", "code"}, "bs.", map).eq("cj.course_group_id",searchDto.getGroupID()).isNull("ams.id"));
                }
            }
        }else {
            if (searchDto.getGradeID()!=null){
                assignmentSubmitList = assignmentSubmitService.getAssignmentSubmitList(searchDto.getPlusPage(), Long.valueOf(searchDto.getForm().get("id").toString()),searchDto.getCourseID(), searchDto.formToEntityWrapperWithSearch(new String[]{"name", "code"}, "bs.", map).eq("bs.grade_id",searchDto.getGradeID()));
            }else if (searchDto.getGroupID()!=null){
                assignmentSubmitList = assignmentSubmitService.getAssignmentSubmitList(searchDto.getPlusPage(), Long.valueOf(searchDto.getForm().get("id").toString()),searchDto.getCourseID(), searchDto.formToEntityWrapperWithSearch(new String[]{"name", "code"}, "bs.", map).eq("cj.course_group_id",searchDto.getGroupID()));
            }
        }
        return assignmentSubmitConverter.pageEntityToDto(assignmentSubmitList);

    }

    @ApiOperation(value = "编辑", notes = "作业提交 编辑分数")
    @RequestMapping(value = "editscore", method = RequestMethod.POST)
    @RequiresPermissions("assignmentsubmit:edit")
    public void editscore(@RequestParam("id") Long sid, @RequestParam("score") Integer score) {
        assignmentSubmitService.editorScore(sid, score);
    }

    @ApiOperation(value = "编辑", notes = "作业提交 设为推荐")
    @RequestMapping(value = "editrecommend", method = RequestMethod.GET)
    @RequiresPermissions("assignmentsubmit:get")
    public void editaddRecommend(@RequestParam("id") Long sid) {
        assignmentSubmitService.editorRecommend(sid);
    }

    @ApiOperation(value = "编辑", notes = "作业提交 取消设为推荐")
    @RequestMapping(value = "editdelRecommend", method = RequestMethod.GET)
    @RequiresPermissions("assignmentsubmit:get")
    public void editdelRecommend(@RequestParam("id") Long sid) {
        assignmentSubmitService.editordelRecommend(sid);
    }

    @ApiOperation(value = "作业提交函数计算", notes = "获取总提交数，最高分，最低分")
    @RequestMapping(value = "getCountByAssignmentId", method = RequestMethod.POST)
    @RequiresPermissions("assignmentsubmit:get")
    public GetCountAssignmentSubmitDto getCountByAssignment(Long assignmentid,Long courseid,Long gradeid,Long groupid) {
        return assignmentSubmitService.getByassignmentId(assignmentid,courseid,gradeid,groupid);
    }

    /**
     * 作业提交单个查询
     * @author Juci
     * @param assignmenSubmitId
     * @return
     */
    @ApiOperation(value = "作业提交单个查询", notes = "通过ID获取整条数据")
    @RequestMapping(value = "getAssignmentSubmitById", method = RequestMethod.GET)
    @RequiresPermissions("assignmentsubmit:get")
    public AssignmentSubmitDto getAssignmentSubmitById(@RequestParam("id") Long assignmenSubmitId) {
        return assignmentSubmitService.getEntityWithCode(assignmenSubmitId);
    }

    /**
     * @return list
     * @description 获取所有为评分的作业列表
     * @author 马勇超
     * @date 2025/4/8 14:11
     */
    @ApiOperation(value = "取为评分作业列表")
    @GetMapping(value = "getNoScoreList")
    @RequiresPermissions("assignmentsubmit:list")
    public List<ListAssignmentSubmitDto> getNoScoreList() {
        List<AssignmentSubmit> assignmentSubmits = assignmentSubmitService.getNoScoreList();
        return assignmentSubmitConverter.listASToDto(assignmentSubmits);
    }

    /**
     * @param assignmentId
     * @return
     * @description 根据作业id获取代码详情
     * @author 马勇超
     * @date 2025/5/13 9:57
     */
    @ApiOperation(value = "根据作业id获取代码详情")
    @GetMapping(value = "getCodeByAssignmentId")
    public TrainingDto getCodeByAssignmentId(@RequestParam("assignmentId") Long assignmentId) {
        Long trainingId = assignmentSubmitService.selectOne(new EntityWrapper<AssignmentSubmit>().eq("assignment_id", assignmentId)).getTrainingId();
        return trainingConverter.entityToDto(trainingService.getTrainingAndCodeById(trainingId));
    }

    /**
     * @param submitWithCode
     * @return
     * @description 提交作业
     * @author 马勇超
     * @date 2025/4/29 16:17
     */
    @ApiOperation(value = "提交作业")
    @PostMapping(value = "submitAssignment")
    @Transactional(rollbackFor = Exception.class)
    @RequiresPermissions("assignmentsubmit:add")
    public void add(@RequestBody AssignmentSubmitWithCode submitWithCode) {
        Student student = studentService.selectOne(new EntityWrapper<Student>().eq("user_id", ShiroKit.getUserId()));
        AssignmentSubmit submit = assignmentSubmitService.selectOne(new EntityWrapper<AssignmentSubmit>()
                .eq("assignment_id",submitWithCode.getAssignmentId()).eq("student_id",student.getId()));
        if(submit == null){
        Assignment assignment = assignmentService.selectById(submitWithCode.getAssignmentId());
        AssignmentSubmit assignmentSubmit = new AssignmentSubmit();
        Code code = submitWithCode.getCode();
        Long codeId = codeService.insert(codeConverter.entityToDto(code));
        Training training = new Training();
        if (codeId > 0) {
            // 代码保存之后保存到练习表
            training.setTitle(assignment.getTitle());
            training.setSummary(null);
            training.setType(null);
            training.setTrainingPathId(null);
            training.setPrivateOrNot(true);
            training.setCanEdit(false);
            training.setStudentId(student.getId());
            training.setCodeId(codeId);
            trainingService.insert(training);
            // 保存到作业提交列表
            assignmentSubmit.setAssignmentId(submitWithCode.getAssignmentId());
            assignmentSubmit.setStudentId(student.getId());
            assignmentSubmit.setTrainingId(training.getId());
            assignmentSubmit.setRecordData(submitWithCode.getRecordData());
            assignmentSubmitService.insert(assignmentSubmit);
            // 该作业提交人数和未评分人数加一
            assignmentService.addSubmitNumber(assignmentSubmit.getAssignmentId());
        } else {
            throw new FriendException("提交失败!");
        }
        }
        else {
            throw new FriendException("作业已经提交！");
        }
    }

    /**
     * @param submitWithCode
     * @return
     * @description 重新提交作业
     * @author 马勇超
     * @date 2025/4/29 16:17
     */
    @ApiOperation(value = "重新提交作业")
    @PostMapping(value = "resetSubmitAssignment")
    @RequiresPermissions("assignmentsubmit:edit")
    public void update(@RequestBody AssignmentSubmitWithCode submitWithCode) {
        AssignmentSubmit assignmentSubmit = assignmentSubmitService.selectOne(new EntityWrapper<AssignmentSubmit>().eq("assignment_id", submitWithCode.getAssignmentId()));
        assignmentSubmit.setRecordData(submitWithCode.getRecordData());
        assignmentSubmitService.updateById(assignmentSubmit);
        Training training = trainingService.selectById(assignmentSubmit.getTrainingId());
        CodeDto codeDto = codeService.get(training.getCodeId());
        Code code = submitWithCode.getCode();
        code.setId(codeDto.getId());
        codeDto = codeConverter.entityToDto(code);
        if (codeService.update(codeDto) == 0) {
            throw new FriendException("重新提交失败！");
        }
    }

    @ApiOperation(value = "作业查重")
    @PostMapping(value = "CheckSimilarity")
    public String CheckSimilarity(@RequestBody List<Long> ids){
//        codeid=1137920854300602370L;

        assignmentSubmitService.gettest1(ids,TenantIdUtil.getTenantId());


        System.out.println(("============>"+Thread.currentThread().getName()));

        return "正在查重......";
    }



    /**
      * @description 根据提交作业id获取代码信息
      * @author 马勇超
      * @date 2025/6/24 10:16
      * @param id
      * @return
     */
    @ApiOperation(value = "根据提交作业id获取代码信息")
    @GetMapping(value = "getCodeByAssignmentSubmitId")
    public CodeDouble getCodeByAssignmentSubmitId(@RequestParam("id1") Long id1, @RequestParam("id2")Long id2){
//        AssignmentSubmit assignmentSubmit = assignmentSubmitService.selectById(id);
//        Training training = trainingService.selectById(assignmentSubmit.getTrainingId());
//        Code code = codeService.selectById(training.getCodeId());
        CodeDouble codeDouble = new CodeDouble();
        Code code1 = codeService.selectByAssignmentSubmitId(id1);
        Code code2 = codeService.selectByAssignmentSubmitId(id2);
        codeDouble.setCodeDto1(codeConverter.entityToDto(code1));
        codeDouble.setCodeDto2(codeConverter.entityToDto(code2));
         return codeDouble;
    }

    /**
      * @description 根据提交作业id获取作业和学生信息
      * @author 马勇超
      * @date 2025/7/4 15:42
      * @param id
      * @return
     */
    @ApiOperation(value = "根据提交作业id获取作业和学生信息")
    @GetMapping(value = "getStudentAndAssignmentById")
    public AssignmentSubmitDto getStudentAndAssignmentById(@RequestParam("id")Long id){
        AssignmentSubmit assignmentSubmit = assignmentSubmitService.getStudentAndAssignmentById(id);
        return assignmentSubmitConverter.entityToDto(assignmentSubmit);
    }
}
