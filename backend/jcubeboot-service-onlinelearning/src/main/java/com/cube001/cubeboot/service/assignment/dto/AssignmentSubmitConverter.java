package com.cube001.cubeboot.service.assignment.dto;

import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.assignment.entity.Assignment;
import com.cube001.cubeboot.assignment.entity.AssignmentSubmit;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [ AssignmentSubmitConverter ]
 * 类的详细说明
 * 作业提交的Converter
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/3/25 8:36
 */

@Mapper( componentModel ="spring")
public interface AssignmentSubmitConverter {
    AssignmentSubmitDto entityToDto(AssignmentSubmit assignmentSubmit);
    AssignmentSubmit submistDtoToEntity(ListAssignmentSubmitDto assignmentSubmitDto);
    List<ListAssignmentDto> listEntityToDto(List<Assignment> Assignments);
    List<ListAssignmentSubmitDto> listASToDto(List<AssignmentSubmit> assignmentSubmits);
    Page<ListAssignmentSubmitDto> pageEntityToDto(Page<AssignmentSubmit> page);
}
