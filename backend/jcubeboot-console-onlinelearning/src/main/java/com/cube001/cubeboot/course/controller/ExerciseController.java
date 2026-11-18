package com.cube001.cubeboot.course.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.base.entity.Label;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.course.entity.Exercise;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.service.base.LabelService;
import com.cube001.cubeboot.service.course.ExerciseJoinService;
import com.cube001.cubeboot.service.course.ExerciseService;
import com.cube001.cubeboot.service.course.dto.ExerciseCommSearchDto;
import com.cube001.cubeboot.service.course.dto.ExerciseConverter;
import com.cube001.cubeboot.service.course.dto.ExerciseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: 实验练习控制器
 * ExerciseController
 *
 * @author Juci
 * @version 1.0
 * @date 2025/3/11 13:50
 */
@Api(value = "ExerciseController", description = "实验练习控制器")
@RestController
@RequestMapping(value = "${cubeboot.app-manager-path}/exercise")
@RequiresPermissions("exercise")
public class ExerciseController implements IApplicationService {

    @Autowired
    private ExerciseService exerciseService;
    @Autowired
    private ExerciseConverter exerciseConverter;
    @Autowired
    private ExerciseJoinService exerciseJoinService;
    @Autowired
    private LabelService labelService;
    /**
     * 添加实验练习
     * @author Juci
     * @param exerciseDto
     * @return
     */
    @ApiOperation(value = "添加实验练习")
    @PostMapping("/add")
    @RequiresPermissions("exercise:add")
    public void add(@RequestBody @Validated ExerciseDto exerciseDto) {
        //学校端与教师端同调用这个接口
        exerciseDto.setUserId(ShiroKit.getUserId());
        exerciseService.insert(exerciseDto);
    }

    /**
     * 获取实验详情
     * @param id
     * @return
     */
    @ApiOperation(value = "获取")
    @RequiresPermissions("exercise:get")
    @RequestMapping(value = "get", method = RequestMethod.GET)
    public ExerciseDto get(@RequestParam("id") Long id) {
        return this.exerciseService.get(id);
    }
    /**
     * 修改实验练习
     * @author Juci
     * @param exerciseDto
     */
    @ApiOperation(value = "修改实验练习")
    @PostMapping("/edit")
    @RequiresPermissions("exercise:edit")
    public void update(@RequestBody @Validated ExerciseDto exerciseDto){
        exerciseService.update(exerciseDto);
    }
    /**
     * 批量删除
     * @author Juci
     * @param ids
     */
    @ApiOperation(value = "批量删除")
    @PostMapping("/del")
    @RequiresPermissions("exercise:del")
    public void delete(@RequestBody List<Long> ids){
        exerciseService.delete(ids);
    }

    /**
     * 分页获取实验列表
     * @author Juci
     * @param searchDto
     * @return
     */
    @ApiOperation(value = "分页获取实验列表")
    @RequestMapping(value = "page", method = RequestMethod.POST)
    @RequiresPermissions("exercise:page")
    public Page<ExerciseDto> page(@RequestBody CommonSearchDto<ExerciseDto> searchDto) {
        Long userId = ShiroKit.getUserId();
        Page<ExerciseDto> page = searchDto.getPlusPage();
        HashMap<String,String> map = new HashMap<>();
        map.put("course_id","a.");
        map.put("type","a.");
        Wrapper<ExerciseDto> courseWrapper = searchDto.formToEntityWrapperWithSearch(new String[]{"name","knowledge"},"a.",map);
        courseWrapper.eq("a.user_id",userId);
        return exerciseService.searchPage(page, courseWrapper);
    }

    /**
     * 分页获取学校端实验列表
     * @author Juci
     * @param searchDto
     * @return
     */
    @ApiOperation(value = "分页获取学校端实验列表")
    @RequestMapping(value = "managerpage", method = RequestMethod.POST)
    @RequiresPermissions("exercise:page")
    public Page<ExerciseDto> managerpage(@RequestBody CommonSearchDto<ExerciseDto> searchDto) {
        Page<ExerciseDto> page = searchDto.getPlusPage();
        HashMap<String,String> map = new HashMap<>();
        map.put("course_id","a.");
        map.put("type","a.");
        Wrapper<ExerciseDto> courseWrapper = searchDto.formToEntityWrapperWithSearch(new String[]{"name","knowledge"},"a.",map);
        courseWrapper.andNew("a.course_id is null or b.type=0");
        return exerciseService.searchPage(page, courseWrapper);
    }
    @ApiOperation(value = "通过课程ID分页获取实验列表")
    @RequestMapping(value = "pageByCourseId", method = RequestMethod.POST)
//    @RequiresPermissions("exercise:page")
    public Page<ExerciseDto> pageByCourseId(@RequestBody CommonSearchDto<ExerciseDto> searchDto) {
        Page<ExerciseDto> page = searchDto.getPlusPage();
        HashMap<String,String> map = new HashMap<>();
        map.put("course_id","a.");
        map.put("type","a.");
        Wrapper<ExerciseDto> courseWrapper = searchDto.formToEntityWrapperWithSearch(null,null,map);
        return exerciseService.searchPage(page, courseWrapper);
    }

    /**
    * @description  获取实验列表-案例库
    * @author 马勇超
    * @date 2025/4/9 15:40
    * @param   searchDto
    * @return   list
    */
    @ApiOperation(value = "实验列表-案例库")
    @PostMapping(value = "list")
    @RequiresPermissions("exercise:list")
    public Page<ExerciseDto> list(@RequestBody ExerciseCommSearchDto<Exercise> searchDto){
        Map<String,String> map = new HashMap<>();
        map.put("category","a.");
        map.put("complexity","a.");
        map.put("name","d.");
        Wrapper<Exercise> wrapper = new EntityWrapper<>();
        wrapper = searchDto.formToEntityWrapper(null,map);
        if(searchDto.getPragramName() != null){
            String str = searchDto.getPragramName();
            wrapper.like("a.name",str);
        }
        Page<ExerciseDto> page = exerciseService.selectWithCourse(searchDto.getPlusPage(),wrapper);
        return page;
    }

    /**
    * @description  学生端案例库标签缓存功能
    * @author 马勇超
    * @date 2025/5/6 9:22
    * @return
    */
    @ApiOperation(value = "学生端案例库标签缓存功能")
    @GetMapping(value = "getExerciseLabel")
    @RequiresPermissions("exercise:list")
    public HashSet<String> exerciserLabel(@RequestParam(value = "category",required = false) String category){
        List<Label> labelList = labelService.selectLabelList(category);
        HashSet<String> hashSet = new HashSet<>();
        for (Label l:labelList) {
            hashSet.add(l.getName());
        }
        return hashSet;
    }

    /**
     * 实验上下移动
     * @author Juci
     * @param id1
     * @param id2
     */
    @ApiOperation(value = "实验上下移动")
    @GetMapping(value = "sort")
    @RequiresPermissions("exercise:sort")
    public void sort(@RequestParam("id1") Long id1,@RequestParam("id2") Long id2){
        Exercise exercise1 = exerciseService.selectById(id1);
        Exercise exercise2 = exerciseService.selectById(id2);
        Integer sort1 = exercise1.getSort();
        Integer sort2 = exercise2.getSort();
        exercise1.setSort(sort2);
        exercise2.setSort(sort1);
        exerciseService.updateById(exercise1);
        exerciseService.updateById(exercise2);
    }
    /**
     * 获取实验详情
     * @param id 实验编号
     * @param type 0-新实验 1-继续试验
     * @return
     */
    @ApiOperation(value = "获取实验详情")
    @RequestMapping(value = "getExerciseInfo", method = RequestMethod.GET)
    @RequiresPermissions("exercise:start")
    public ExerciseDto getExerciseInfo(@RequestParam("id") Long id,@RequestParam("type") Integer type) {
        Long userId = ShiroKit.getUserId();
        //获取实验信息
        return this.exerciseService.getExerciseInfo(userId,id,type);
    }
}
