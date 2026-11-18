package com.cube001.cubeboot.course.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.course.entity.ExerciseJoinReport;
import com.cube001.cubeboot.service.course.ExerciseJoinReportService;
import com.cube001.cubeboot.service.course.ExerciseJoinService;
import com.cube001.cubeboot.service.course.dto.*;
import com.cube001.cubeboot.vo.course.CommonSearchExtendUserIdVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: ExerciseJoinReportController
 * 实验练习报告控制器
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/3/28 18:09
 */
@Api(value = "ExerciseJoinReportController", description = "实验练习报告控制器")
@RestController
@RequestMapping(value = "${cubeboot.app-manager-path}/exercisejoinreport")
@RequiresPermissions("exercisejoinreport")
public class ExerciseJoinReportController implements IApplicationService {

    @Autowired
    private ExerciseJoinReportService exerciseJoinReportService;
    @Autowired
    private ExerciseJoinReportConverter exerciseJoinReportConverter;
    @Autowired
    private ExerciseJoinService exerciseJoinService;

    /**
     * 添加实验练习
     * @author anonymous
     * @param exerciseJoinReportDto
     */
    @ApiOperation(value = "添加实验练习")
    @PostMapping("/add")
    @RequiresPermissions("exercisejoinreport:add")
    public void add(@RequestBody @Validated ExerciseJoinReportDto exerciseJoinReportDto) {
        Long userId = ShiroKit.getUserId();
        if(exerciseJoinReportDto.getExerciseId()==null){
            throw new FriendException("实验编号不能为空！");
        }
        //获取参与实验编号
        Long exerciseJoinId = this.exerciseJoinService.insert(userId,exerciseJoinReportDto.getExerciseId()).getId();
        exerciseJoinReportDto.setExerciseJoinId(exerciseJoinId);
        ExerciseJoinReport exerciseJoinReport = exerciseJoinReportConverter.dtoToEntity(exerciseJoinReportDto);
        exerciseJoinReportService.create(exerciseJoinReport);
    }

    /**
     * 修改实验练习
     * @author anonymous
     * @param exerciseJoinReportDto
     */
    @ApiOperation(value = "修改实验练习")
    @PostMapping("/edit")
    @RequiresPermissions("exercisejoinreport:edit")
    public void edit(@RequestBody @Validated ExerciseJoinReportDto exerciseJoinReportDto) {
        if(exerciseJoinReportDto.getExerciseJoinId()==null){
            throw new FriendException("实验参与编号不能为空！");
        }
        this.exerciseJoinReportService.edit(ShiroKit.getUserId(),exerciseJoinReportDto);
    }

    /**
     * 通过报告编号获取报告详情
     * @param id
     * @return
     */
    @ApiOperation(value = "通过报告编号获取报告详情")
    @GetMapping("/get")
    @RequiresPermissions("exercisejoinreport:get")
    public ExerciseJoinReportDto get(@RequestParam("id") Long id){
        Wrapper<ExerciseJoinReportDto> wrapper = new EntityWrapper<>();
        wrapper.eq("a.id",id);
        exerciseJoinReportService.updateClick(id);
        return exerciseJoinReportService.selectReport(wrapper);
    }
    /**
     * 通过参与实验编号获取报告详情
     * @author anonymous
     * @param id
     * @return
     */
    @ApiOperation(value = "通过参与实验编号获取报告详情")
    @GetMapping("/getbyjoinid")
    @RequiresPermissions("exercisejoinreport:getbyjoinid")
    public ExerciseJoinReportDto getByJoinId(@RequestParam("id") Long id){
        Wrapper<ExerciseJoinReport> wrapper = new EntityWrapper<>();
        wrapper.eq("exercise_join_id",id);
        List<ExerciseJoinReport> exerciseJoinReports = exerciseJoinReportService.selectList(wrapper);
        if(exerciseJoinReports.size()>0){
            return exerciseJoinReportConverter.entityToDto(exerciseJoinReports.get(0));
        }
        else {
            return null;
        }
    }

    /**
     * 通过实验编号获取报告详情
     * @param id
     * @return
     */
    @ApiOperation(value = "通过实验编号获取报告详情")
    @GetMapping("/getByExerciseId")
    @RequiresPermissions("exercisejoinreport:getbyjoinid")
    public ExerciseJoinReportDto getByExerciseId(@RequestParam("id") Long id){
        Long userId = ShiroKit.getUserId();
        return exerciseJoinReportConverter.entityToDto(this.exerciseJoinReportService.getByExerciseId(userId,id));
    }
    /**
     * 根据用户编号获取报告分页
     * @author anonymous
     * @param commonSearchExtendUserIdVo
     * @return
     */
    @ApiOperation(value = "根据用户编号获取报告分页",notes = "根据用户编号获取报告分页")
    @RequestMapping(value = "/page",method = RequestMethod.POST)
    @RequiresPermissions("exercisejoinreport:page")
    public Page<ExerciseJoinReportDto> page(@RequestBody CommonSearchExtendUserIdVo<ExerciseJoinReportDto> commonSearchExtendUserIdVo){
        Page page = commonSearchExtendUserIdVo.getPlusPage();
        Wrapper<ExerciseJoinReportDto> wrapper = commonSearchExtendUserIdVo.formToEntityWrapper(null,null);
        // 获取当前登录用户的报告
        if(commonSearchExtendUserIdVo.getUserId()==null){
            commonSearchExtendUserIdVo.setUserId(ShiroKit.getUserId());
        }
        else { //非私有报告
            wrapper.eq("a.private_report",0);
        }
        wrapper.eq("d.user_id",commonSearchExtendUserIdVo.getUserId());
        Page<ExerciseJoinReportDto> exerciseJoinReportDtoPage = exerciseJoinReportService.searchPage(page,wrapper);
        return exerciseJoinReportDtoPage;
    }
    /**
     * 获取所有公开报告
     * @author anonymous
     * @param commonSearchDto
     * @return
     */
    @ApiOperation(value = "获取所有公开报告",notes = "获取所有公开报告")
    @RequestMapping(value = "/publicpage",method = RequestMethod.POST)
    @RequiresPermissions("exercisejoinreport:publicpage")
    public Page<ExerciseJoinReportDto> publicPage(@RequestBody CommonSearchDto<ExerciseJoinReportDto> commonSearchDto){
        Page page = commonSearchDto.getPlusPage();
        Wrapper<ExerciseJoinReportDto> wrapper = commonSearchDto.formToEntityWrapper(null,null);
        wrapper.eq("a.private_report",0);
        Page<ExerciseJoinReportDto> exerciseJoinReportDtoPage = exerciseJoinReportService.searchPage(page,wrapper);
        return exerciseJoinReportDtoPage;
    }
    /**
     * 相关报告
     * @author anonymous
     * @param exerciseId
     * @return
     */
    @ApiOperation(value = "相关报告",notes = "相关报告")
    @RequestMapping(value = "/correlationreport",method = RequestMethod.GET)
    @RequiresPermissions("exercisejoinreport:list")
    public List<ExerciseJoinReportDto> correlationReport(@RequestParam("exerciseId") Long exerciseId){
        return exerciseJoinReportService.correlationReport(exerciseId);
    }
}
