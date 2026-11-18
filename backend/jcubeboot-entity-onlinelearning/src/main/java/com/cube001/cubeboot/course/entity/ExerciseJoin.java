package com.cube001.cubeboot.course.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.Version;

import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.base.entity.Grade;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import com.cube001.cubeboot.training.entity.Training;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author anonymous
 * @since 2019-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_exercise_join")
public class ExerciseJoin extends FullAuditedEntityNPk<ExerciseJoin> {

    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.ID_WORKER)

    /**
     * 编号
     */
    private Long id;
    /**
     * 实验编号
     */
    @TableField("exercise_id")
    private Long exerciseId;
    /**
     * 学生编号
     */
    @TableField("student_id")
    private Long studentId;
    /**
     * 练习报告
     */
    private String joinReport;
    /**
     * 对应练习
     */
    private Long trainingId;

    @TableField(exist = false)
    private Exercise exercise;
    @TableField(exist = false)
    private Student student;
    @TableField(exist = false)
    private Grade grade;
    @TableField(exist = false)
    private Training training;
}
