package com.cube001.cubeboot.service.course.dto;

import lombok.Data;

import java.util.Date;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: CourseGroupDto
 * CourseGroupDto
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/7/5 13:31
 */
@Data
public class CourseGroupDto {
    private Long id;
    /**小组名称*/
    private String name;
    /**所属课程*/
    private Long courseId;
    /**创建时间*/
    private Date creationTime;
}
