package com.cube001.cubeboot.service.course.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.core.validateGroup.EditChecks;
import com.cube001.cubeboot.course.entity.Course;
import com.cube001.cubeboot.course.entity.CourseGroup;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: CourseJoinDto
 * 课程参与详情Dto
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/3/13 10:44
 */
@Data
public class CourseJoinDto {
    @NotNull(groups = EditChecks.class)
    private Long id;
    /**
     课程编号
     */
    private Long courseId;
    /**
     学生编号
     */
    private Long studentId;
    /**
     参与练习数
     */
    private Integer joinExercise;
    /**
     * 所属小组
     */
    private Long courseGroupId;
    /**
     学生
     */
    private Student student;
    /**
     课程
     */
    private Course course;
    /**
     学习进度
     */
    private  Double learningProgress;
    /**
     * 创建时间
     */
    private Date creationTime;
    /**
     * 所属小组
     */
    private CourseGroup courseGroup;
}
