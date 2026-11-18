package com.cube001.cubeboot.course.controller;


import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.course.entity.ExerciseJoin;
import com.cube001.cubeboot.service.course.ExerciseJoinService;
import com.cube001.cubeboot.service.course.dto.ExerciseJoinConvter;
import com.cube001.cubeboot.service.course.dto.ExerciseJoinDetailDto;
import com.cube001.cubeboot.service.course.dto.ExerciseJoinDto;
import com.cube001.cubeboot.service.course.dto.ExerciseJoinRecord.JoinExerciseDto;
import com.cube001.cubeboot.vo.course.CommonSearchExtendUserIdVo;
import com.cube001.cubeboot.vo.course.ExercisePageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: 实验练习参与详情控制器
 * ExerciseJoinController
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/3/11 13:30
 */
@RestController
@RequestMapping(value = "${cubeboot.app-manager-path}/exerciseJoin")
@Api(value = "ExerciseJoinController",description = "实验记录控制类")
@RequiresPermissions("exerciseJoin")
public class ExerciseJoinController implements IApplicationService {


    @Autowired
    private ExerciseJoinService exerciseJoinService;
    @Autowired
    private ExerciseJoinConvter convter;
    /**
     * @description 学生端,通过课程id查找相关的练习记录/参与记录
     * @author anonymous
     * @date 2025/4/27 15:35
     * @param
     * @return
     */
    @ApiOperation(value = "通过课程id查找相关的练习内容和参与记录")
    @GetMapping("/getListByCourseId")
    @RequiresPermissions("exercise:get")
    public List<ExerciseJoinDto> getExerciseListByCourseId(@RequestParam("courseId")Long courseId) {
        List<ExerciseJoin> exerciseList = exerciseJoinService.getExerciseListByCourseId(courseId);
        List<ExerciseJoinDto> listDto = convter.list2Dto(exerciseList);
        return listDto;
    }
    /**
     * @description 搜索/分页查询参与课程的学生完成的实验
     * @author anonymous
     * @date 2025年3月15日 10:06:10
     * @param
     * @return
    */
    @ApiOperation(value = " 参与课程的学生完成的实验")
    @PostMapping(value = "pageDetail")
    @RequiresPermissions("exerciseJoin:page")
    public Page<ExerciseJoinDto> pageDetailStudent(@RequestBody CommonSearchDto<ExerciseJoin> searchDto){
        HashMap<String,String> map = new HashMap<>();
        map.put("profession_id","s.");
        map.put("grade_id","s.");
        map.put("name","s.");
        Page<ExerciseJoin> page = exerciseJoinService.selectWithStudent(searchDto.getPlusPage(),searchDto.formToEntityWrapper(null,map));
        return convter.pageToDto(page);
    }

    /**
     * 根据练习编号查询参与练习列表
     * @author anonymous
     * @param searchDto
     * @return
     */
    @ApiOperation(value = "根据练习编号查询参与练习列表")
    @PostMapping(value = "pageByExerciseId")
    @RequiresPermissions("exerciseJoin:pageByExerciseId")
    public Page<ExerciseJoinDto> pageByExerciseId(@RequestBody ExercisePageVo searchDto) {
        HashMap<String, String> map = new HashMap<>();
        map.put("grade_id", "s.");
        map.put("year", "g.");
        Wrapper<ExerciseJoin> joinWrapper = searchDto.formToEntityWrapperWithSearch(new String[]{"name", "code"}, "s.", map);
        joinWrapper.eq("a.exercise_id", searchDto.getExerciseId());
        Page<ExerciseJoin> page = exerciseJoinService.selectWithStudent(searchDto.getPlusPage(), joinWrapper);
        return convter.pageToDto(page);
    }

    /**
     * 获取用户参与的所有实验
     * @author anonymous
     * @param commonSearchExtendUserIdVo
     * @return
     */
    @ApiOperation(value = "获取用户参与的所有实验")
    @PostMapping(value = "getCurrentPage")
    @RequiresPermissions("exerciseJoin:getCurrentPage")
    public Page<ExerciseJoinDto> getCurrentPage(@RequestBody CommonSearchExtendUserIdVo<ExerciseJoin> commonSearchExtendUserIdVo) {
        Wrapper<ExerciseJoin> joinWrapper = commonSearchExtendUserIdVo.formToEntityWrapperWithSearch(null, null,null);
        // 判断是否传userId
        if(commonSearchExtendUserIdVo.getUserId()==null){
            commonSearchExtendUserIdVo.setUserId(ShiroKit.getUserId());
        }
        // 若通过userId获取实验，只取单个实验记录
        else{
            joinWrapper.eq("e.type",1);
        }
        joinWrapper.eq("s.user_id", commonSearchExtendUserIdVo.getUserId());
        Page<ExerciseJoin> page = exerciseJoinService.selectWithStudent(commonSearchExtendUserIdVo.getPlusPage(), joinWrapper);
        return convter.pageToDto(page);
    }
    /**
     * 通过参与编号获取参与详情
     * @author anonymous
     * @param joinId
     * @return
     */
    @ApiOperation(value = "通过参与编号获取参与详情")
    @GetMapping(value = "get")
    @RequiresPermissions("exerciseJoin:get")
    public ExerciseJoinDetailDto getJoinDetail(@RequestParam("joinId") Long joinId){
        return exerciseJoinService.joinDetail(joinId);
    }
    /**
     * @description 删除/批量删除
     * @author anonymous
     * @date 2025年3月15日 10:01:23
     * @param ids
     * @return
    */
    @ApiOperation(value = "批量删除")
    @PostMapping("/del")
    @RequiresPermissions("exerciseJoin:del")
    public Boolean delete(@RequestBody List<Long> ids){
        Boolean result = exerciseJoinService.delete(ids);
        return result;
    }

    /**
     * 结束实验
     * @author anonymous
     * @param joinExerciseDto
     */
    @ApiOperation(value = "结束实验")
    @PostMapping("/stopExercise")
    public void stopExercise(@RequestBody JoinExerciseDto joinExerciseDto){
        this.exerciseJoinService.stopExercise(ShiroKit.getUserId(),joinExerciseDto);
    }
}

