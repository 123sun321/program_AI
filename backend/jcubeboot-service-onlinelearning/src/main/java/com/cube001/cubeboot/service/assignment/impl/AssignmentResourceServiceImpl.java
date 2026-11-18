package com.cube001.cubeboot.service.assignment.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.assignment.entity.AssignmentResource;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.course.entity.ExerciseResource;
import com.cube001.cubeboot.service.assignment.AssignmentResourceService;
import com.cube001.cubeboot.service.assignment.dto.AssignmentResourceConverter;
import com.cube001.cubeboot.service.assignment.dto.AssignmentResourceDto;
import com.cube001.cubeboot.service.assignment.dto.ListAssignmentResourceDto;
import com.cube001.cubeboot.service.assignment.mapper.AssignmentResourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/*
* Copyright (C), 2018 - 2019, ZhengZho uChuangZhi. Co., Ltd
* FileName:
* 类的详细说明
*
* @author 张人杰
* @date   2019/4/23 14:50
* @version 1.0
*/
@Service
public class AssignmentResourceServiceImpl extends ServiceImpl<AssignmentResourceMapper, AssignmentResource> implements AssignmentResourceService {

    @Autowired
    private AssignmentResourceConverter assignmentResourceConverter;
    /*
      * @description 作业资源的保存
      * @author 张人杰
      * @date 2025/4/24 17:08
      * @Param
      * @return
      */

    @Override
    public void bacthinsert(ListAssignmentResourceDto listAssignmentResourceDto) {
        Wrapper<AssignmentResource> wrapper = new EntityWrapper<>();
        wrapper.eq("assignment_id",listAssignmentResourceDto.getAssignmentid());
        List<AssignmentResourceDto> assignmentResources=listAssignmentResourceDto.getAssignmentResourceDtos();
        List<Long> ids=assignmentResources.stream().map(m->m.getId()).collect(Collectors.toList());
        if (ids.size()==0){
            baseMapper.delete(wrapper);
        }else{
            wrapper.notIn("id", ids);
            baseMapper.delete(wrapper);
        }
        for (AssignmentResourceDto assignmentResourceDto:assignmentResources) {
            if (assignmentResourceDto.getId()==null){
                assignmentResourceDto.setAssignmentId(listAssignmentResourceDto.getAssignmentid());
                if (baseMapper.insert(assignmentResourceConverter.editDtoToEntity(assignmentResourceDto))==0){
                    throw  new FriendException("作业资源保存失败");
                }
            }else {
                if (baseMapper.updateById(assignmentResourceConverter.editDtoToEntity(assignmentResourceDto))==0){
                    throw new FriendException("作业保存失败");
                }
            }
        }
    }
}
