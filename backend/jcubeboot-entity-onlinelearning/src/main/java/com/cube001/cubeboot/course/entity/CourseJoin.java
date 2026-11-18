package com.cube001.cubeboot.course.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import lombok.Data;
import lombok.experimental.Accessors;


/**
* Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
* FileName: CourseJoin
* 课程参与详情实体类
*
* @author 张浩杰
* @date   2019/3/12 16:20
* @version 1.0
*/
@Data
@Accessors(chain = true)
@TableName("t_course_join")
public class CourseJoin extends FullAuditedEntityNPk<CourseJoin> {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.ID_WORKER)
    private Long id;
    /**
    课程编号
     */
    @TableField("course_id")
    private Long courseId;
    /**
    学生编号
     */
    @TableField("student_id")
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
    @TableField(exist = false)
    private Student student;
    /**
     课程
     */
    @TableField(exist = false)
    private Course course;
    /**
     学习进度
     */
    @TableField(exist = false)
    private  Double learningProgress;
    /**小组信息*/
    @TableField(exist = false)
    private CourseGroup courseGroup;
}





