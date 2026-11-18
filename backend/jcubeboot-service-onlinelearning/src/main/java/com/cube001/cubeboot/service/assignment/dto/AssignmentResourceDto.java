package com.cube001.cubeboot.service.assignment.dto;


import com.cube001.cubeboot.assignment.entity.AssignmentResource;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [ 文件名 ]
 * 类的详细说明
 *
 * @author 张人杰
 * @version 1.0
 * @date 2025/4/23 15:44
 */

@Data
public class AssignmentResourceDto {
    /** 编号 */
    @NotNull(groups = AssignmentResource.class)
    private  Long id;
    /**
     * 课程编号
     */
    @NotNull(message = "课程ID不能为空")
    private Long assignmentId;
    /**
     * 资源名称
     */
    @NotNull(message = "资源名称不能为空")
    private String name;
    /**
     * 资源链接
     */
    @NotNull(message = "资源链接不能为空")
    private String url;
    /**
     * 排序
     */
    @NotNull(message = "排序不能为空")
    private Integer sort;

}
