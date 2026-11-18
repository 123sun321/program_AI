package com.cube001.cubeboot.service.course.dto;

import com.cube001.cubeboot.course.entity.Course;
import com.cube001.cubeboot.course.entity.Exercise;
import com.cube001.cubeboot.course.entity.ExerciseJoinRecord;
import lombok.Data;

import java.util.List;

/**
 * Copyright (C),2018-2019, ZhengZhouChuangZhi. Co.,Ltd.
 * FileName: [文件名]
 * 类的详细说明
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/3/14 10:30
 */
@Data
public class CoursePathDto {

    private Long id;
    private String category;
    private Long courseId;
    private Integer sort;
    /** 该课程是否参与 */
    private Boolean isJoinEd;
    /** 课程详情 */
    private Course course;
    private Exercise exercise;
    private List<ExerciseJoinRecord> exerciseJoinRecordList;
}
