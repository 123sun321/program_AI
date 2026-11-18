package com.cube001.cubeboot.service.assignment.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.cube001.cubeboot.assignment.entity.Assignment;
import com.cube001.cubeboot.base.entity.Grade;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.service.training.dto.TrainingDto;
import lombok.Data;

import java.util.Date;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: AssignmentSubmitDto
 * AssignmentSubmitDto
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/6/10 13:38
 */
@Data
public class AssignmentSubmitDto {
    private Long id;
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
    /**创建时间*/
    private Date creationTime;
    /**编辑记录*/
    private String recordData;
   /**学生的对象*/
    private Student student;
   /**作业对象*/
    private Assignment assignment;
    /**班级对象*/
    private Grade grade;
    /**练习记录*/
    private TrainingDto trainingDto;
    /*代码相似度*/
    private String codeSimilarity;
}
