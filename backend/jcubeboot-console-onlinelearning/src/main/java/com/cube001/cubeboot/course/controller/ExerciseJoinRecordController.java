package com.cube001.cubeboot.course.controller;

import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.service.base.StudentService;
import com.cube001.cubeboot.service.course.ExerciseJoinRecordService;
import com.cube001.cubeboot.service.course.dto.ExerciseJoinRecord.ExerciseJoinRecordDto;
import com.cube001.cubeboot.service.course.dto.ExerciseJoinRecord.ExerciseRecordStatisticsDto;
import com.cube001.cubeboot.service.course.dto.ExerciseJoinRecord.ExerciseStatisticNumberDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: ExerciseJoinRecordController
 * 实验参与记录控制器
 *
 * @author Juci
 * @version 1.0
 * @date 2025/4/8 13:37
 */
@Api(value = "ExerciseJoinRecordController", description = "实验参与记录控制器")
@RestController
@RequestMapping(value = "${cubeboot.app-manager-path}/exercisejoinrecord")
@RequiresPermissions("exercisejoinrecord")
public class ExerciseJoinRecordController implements IApplicationService {

    @Autowired
    private ExerciseJoinRecordService exerciseJoinRecordService;
    @Autowired
    private StudentService studentService;
    /**
     * 添加实验练习
     * @author Juci
     * @param exerciseJoinRecordDto
     * @return
     */
    @ApiOperation(value = "添加实验练习记录")
    @PostMapping("/add")
    @RequiresPermissions("exercisejoinrecord:add")
    public void add(@RequestBody @Validated ExerciseJoinRecordDto exerciseJoinRecordDto) {
        Long userId = ShiroKit.getUserId();
        exerciseJoinRecordService.insert(exerciseJoinRecordDto,userId);
    }

    /**
     * 学生参与练习次数相关统计
     * @author
     * @param userId 用户编号
     * @return
     */
    @ApiOperation(value = "学生参与练习次数相关统计")
    @GetMapping("/statisticnumber")
    @RequiresPermissions("exercisejoinrecord:statistic")
    public ExerciseStatisticNumberDto statisticNumber(Long userId){
        userId = userId==null ? ShiroKit.getUserId() : userId;
        return exerciseJoinRecordService.statisticNumber(userId);
    }
    /**
     * 统计学生一年的学习情况
     * @author Juci
     * @param userId 用户编号
     * @return
     */
    @ApiOperation(value = "统计学生一年的学习情况")
    @GetMapping("/exerciserecordstatistics")
    @RequiresPermissions("exercisejoinrecord:statistic")
    public List<ExerciseRecordStatisticsDto> exerciseRecordStatistics(Long userId){
        userId = userId==null ? ShiroKit.getUserId() : userId;
        return exerciseJoinRecordService.exerciseRecordStatistics(userId);
    }
    /**
     * @description  学生参与练习次数相关统计根据学生Id
     * @author 马勇超
     * @date 2025/4/29 9:13
     * @param   studentId
     * @return
     */
    @ApiOperation(value = "学生参与练习次数相关统计根据学生Id")
    @GetMapping("/statisticNumberByStudentId")
    @RequiresPermissions("exercisejoinrecord:statistic")
    public ExerciseStatisticNumberDto statisticNumberByStudentId(@RequestParam("studentId") Long studentId){
        Student student = studentService.selectById(studentId);
        return exerciseJoinRecordService.statisticNumber(student.getUserId());
    }
    /**
    * @description  统计学生一年的学习情况通过学生Id
    * @author 马勇超
    * @date 2025/4/29 9:23
    * @param   studentId
    * @return
    */
    @ApiOperation(value = "统计学生一年的学习情况通过学生Id")
    @GetMapping("/exerciseRecordStatisticsByStduentId")
    @RequiresPermissions("exercisejoinrecord:statistic")
    public List<ExerciseRecordStatisticsDto> exerciseRecordStatisticsByStduentId(Long studentId){
        Student student = studentService.selectById(studentId);
        return exerciseJoinRecordService.exerciseRecordStatistics(student.getUserId());
    }

}
