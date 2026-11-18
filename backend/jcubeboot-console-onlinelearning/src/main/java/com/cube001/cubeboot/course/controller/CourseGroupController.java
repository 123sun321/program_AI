package com.cube001.cubeboot.course.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.base.entity.Label;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.course.entity.CourseGroup;
import com.cube001.cubeboot.course.entity.CourseJoin;
import com.cube001.cubeboot.service.base.dto.base.LabelDto;
import com.cube001.cubeboot.service.course.CourseGroupService;
import com.cube001.cubeboot.service.course.CourseJoinService;
import com.cube001.cubeboot.service.course.dto.CourseGroupConverter;
import com.cube001.cubeboot.service.course.dto.CourseGroupDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: CourseGroupController
 * CourseGroupController
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/7/5 13:34edit
 */
@Api(value = "CourseGroupController", description = "课程小组")
@RestController
@RequestMapping(value = "${cubeboot.app-manager-path}/coursegroup")
public class CourseGroupController implements IApplicationService {

    @Autowired
    private CourseGroupService courseGroupService;
    @Autowired
    private CourseGroupConverter courseGroupConverter;
    @Autowired
    private CourseJoinService courseJoinService;
    /**
     * 根据id查询课程小组
     * @author anonymous
     * @date 2025/7/5 13:38
     * @param id
     * @return
     */
    @ApiOperation(value="根据id查询课程小组", notes="根据id查询课程小组")
    @GetMapping(value = "/get")
    public CourseGroupDto get(@RequestParam("id") Long id){
        CourseGroup courseGroup = courseGroupService.selectById(id);
        return  courseGroupConverter.entityToDto(courseGroup);
    }

    /**
     * 创建课程小组
     * @Author anonymous
     * @param courseGroupDto
     */
    @ApiOperation(value="创建课程小组", notes="创建课程小组")
    @PostMapping(value = "/add")
    @ResponseBody
    public void create(@RequestBody @Validated CourseGroupDto courseGroupDto){
        CourseGroup courseGroup = courseGroupConverter.dtoToEntity(courseGroupDto);
        courseGroupService.insert(courseGroup);
    }

    /**
     * 创建课程小组
     * @Author anonymous
     * @param courseGroupDto
     */
    @ApiOperation(value="修改课程小组", notes="修改课程小组")
    @PostMapping(value = "/edit")
    @ResponseBody
    public void edit(@RequestBody @Validated CourseGroupDto courseGroupDto){
        CourseGroup courseGroup = courseGroupConverter.dtoToEntity(courseGroupDto);
        courseGroupService.updateById(courseGroup);
    }
    /**
     * 课程小组
     * @Author anonymous
     * @date 2025/7/5 13:41
     * @param id
     * @return
     */
    @ApiOperation(value = "课程小组删除")
    @GetMapping(value = "/del")
    public Boolean del(@RequestParam("id") Long id){
        Wrapper<CourseJoin> wrapper = new EntityWrapper<>();
        wrapper.eq("course_group_id",id);
        if(this.courseJoinService.selectCount(wrapper)>0){
            throw new FriendException("已有学生参与该小组，无法删除！");
        }
        return courseGroupService.deleteById(id);
    }

    /**
     * 课程小组分页
     * @Author anonymous
     * @date 2025/7/5 13:44
     * @param searchDto
     * @return
     */
    @ApiOperation(value = "课程小组分页")
    @PostMapping(value = "/page")
    public Page<CourseGroupDto> page(@RequestBody CommonSearchDto<CourseGroup> searchDto){
        Wrapper<CourseGroup> wrapper = searchDto.formToEntityWrapperWithSearch(new String[]{"name"},"");
        Page<CourseGroup> page = courseGroupService.selectPage(searchDto.getPlusPage(),wrapper);
        return courseGroupConverter.pageEntityToDto(page);
    }

    /**
     * 根据课程编号获取小组信息
     * @Author anonymous
     * @param courseId
     * @return
     */
    @ApiOperation(value = "根据课程编号获取小组信息")
    @GetMapping(value = "/list")
    public List<CourseGroupDto> list(@RequestParam("courseId") Long courseId){
        Wrapper<CourseGroup> wrapper = new EntityWrapper<>();
        wrapper.eq("course_id",courseId);
        return courseGroupConverter.listEntityToDto(this.courseGroupService.selectList(wrapper));
    }

}
