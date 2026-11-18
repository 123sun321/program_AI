package com.cube001.cubeboot.service.assignment.dto;

import lombok.Data;

import java.util.List;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [ 文件名 ]
 * 类的详细说明
 *
 * @author 张人杰
 * @version 1.0
 * @date 2025/4/24 11:23
 */

@Data
public class ListAssignmentResourceDto {
    //
    private List<AssignmentResourceDto> assignmentResourceDtos;
    //
    private Long assignmentid;
}
