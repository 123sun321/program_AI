package com.cube001.cubeboot.service.assignment.dto;

import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.assignment.entity.Assignment;
import com.cube001.cubeboot.assignment.entity.AssignmentResource;
import lombok.Data;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [ 文件名 ]
 * 类的详细说明
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/4/23 15:10
 */

@Mapper( componentModel ="spring")
public interface AssignmentResourceConverter {

    Page<AssignmentResourceDto> pageEntityToDto(Page<AssignmentResource> page);
    AssignmentResource editDtoToEntity(AssignmentResourceDto assignmentResourceDto);
    List<AssignmentResourceDto> listDtoToEntity(List<AssignmentResource> assignmentResources);
}
