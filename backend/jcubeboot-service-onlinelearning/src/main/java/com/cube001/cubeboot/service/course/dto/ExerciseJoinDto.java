package com.cube001.cubeboot.service.course.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.cube001.cubeboot.base.entity.Grade;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.course.entity.Exercise;
import com.cube001.cubeboot.training.entity.Training;
import lombok.Data;

import java.util.Date;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [ 文件名 ]
 * 类的详细说明
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/3/12$ 14:54$
 */
@Data
public class ExerciseJoinDto {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private Long id;
    /**
     * 实验编号
     */
    private Long exerciseId;
    /**
     * 学生编号
     */
    private Long studentId;
    /**
     * 练习报告
     */
    private String joinReport;
    /**
     * 对应练习
     */
    private Long trainingId;
    /**创建时间*/
    private Date creationTime;

    private Exercise exercise;
    private Student student;
    private Grade grade;
    private Training training;
}
