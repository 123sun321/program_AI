package com.cube001.cubeboot.assignment.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.assignment.entity.Assignment;
import com.cube001.cubeboot.assignment.entity.AssignmentResource;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.course.entity.Course;
import com.cube001.cubeboot.service.assignment.AssignmentResourceService;
import com.cube001.cubeboot.service.assignment.dto.AssignmentResourceConverter;
import com.cube001.cubeboot.service.assignment.dto.AssignmentResourceDto;
import com.cube001.cubeboot.service.assignment.dto.ListAssignmentDto;
import com.cube001.cubeboot.service.assignment.dto.ListAssignmentResourceDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@Api(value = "作业资源Controller",description = "作业资源Controller")
@RequestMapping(value = "${cubeboot.app-manager-path}/assignmentResource")
@RequiresPermissions("assignmentResource")
public class AssignmentResourceController implements IApplicationService {

    @Autowired
    private AssignmentResourceService assignmentResourceService;
    @Autowired
    private AssignmentResourceConverter assignmentResourceConverter;

    @ApiOperation(value = "作业资源的分页展示",notes = "模糊查询分页")
    @RequestMapping(value = "/getPage",method = RequestMethod.POST)
    @RequiresPermissions("assignmentResource:page")
    public Page<AssignmentResourceDto> getPage(@RequestBody CommonSearchDto<AssignmentResource> searchDto){

        Page<AssignmentResource> page = searchDto.getPlusPage();
        Wrapper<AssignmentResource> courseWrapper = searchDto.formToEntityWrapper(null);
        page = assignmentResourceService.selectPage(page, courseWrapper);
        return assignmentResourceConverter.pageEntityToDto(page);
    }

    @ApiOperation(value = "作业资源保存",notes = "更新和添加是同步进行的")
    @RequestMapping(value = "/saveAssignmentResource",method = RequestMethod.POST)
    @RequiresPermissions("assignmentResource:add")
    public void saveAssignmentResource(@RequestBody @Validated ListAssignmentResourceDto listAssignmentResourceDto){

        assignmentResourceService.bacthinsert(listAssignmentResourceDto);
    }
    @ApiOperation(value = "作业资源展示",notes = "作业资源展示")
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public List<AssignmentResourceDto> listAll(@RequestParam("assignment_id") Long  assignment_id){
        Wrapper<AssignmentResource> assignmentResourceWrapper=new EntityWrapper<>();
        assignmentResourceWrapper.eq("assignment_id",assignment_id);
        List<AssignmentResource> assignmentResources = assignmentResourceService.selectList(assignmentResourceWrapper);
        return assignmentResourceConverter.listDtoToEntity(assignmentResources);
    }

}

