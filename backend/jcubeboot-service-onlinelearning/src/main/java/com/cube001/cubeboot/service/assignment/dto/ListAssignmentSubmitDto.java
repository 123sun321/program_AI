package com.cube001.cubeboot.service.assignment.dto;

import com.cube001.cubeboot.assignment.entity.Assignment;
import com.cube001.cubeboot.base.entity.Grade;
import com.cube001.cubeboot.base.entity.Student;
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
 * @date 2025/3/22 13:54
 */

@Data
public class ListAssignmentSubmitDto {
    /** 学生编号 */
    private  Long id;
    /** 作业编号 */
    private Long assignmentId;
    /** 学生编号 */
    private Long studentId;
    /** 评分 */
    private Integer score;
    /** 推荐作品 */
    private int recommend;
    /** 对应练习 */
    private Long trainingId;
    /** 是否评分**/
    private Boolean whetherScore;
    /*代码相似度*/
    private String codeSimilarity;
    /**作业提交创建的时间*/
    private Date creationTime;
    /**学生的对象*/
    private Student student;
    /**作业对象*/
    private Assignment assignment;
    /**班级对象*/
    private Grade grade;
}
