package com.cube001.cubeboot.service.course.dto;

import com.cube001.cubeboot.course.entity.Course;
import lombok.Data;

import java.util.Date;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: CourseCollectDto
 * CourseCollectDto
 *
 * @author Juci
 * @version 1.0
 * @date 2025/3/28 10:25
 */
@Data
public class CourseCollectDto {

    private Long id;
    /**用户编号*/
    private Long userId;
    /**课程编号*/
    private Long courseId;
    /**创建时间*/
    private Date creationTime;
    /**课程信息*/
    private Course course;
}
