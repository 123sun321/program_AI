package com.cube001.cubeboot.service.assignment.dto;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.cube001.cubeboot.base.entity.Grade;
import com.cube001.cubeboot.course.entity.Course;
import com.cube001.cubeboot.course.entity.CourseGroup;
import lombok.Data;
import lombok.experimental.Accessors;


import java.util.Date;
import java.util.List;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [ 文件名 ]
 * 类的详细说明
 *
 * @author 张人杰
 * @version 1.0
 * @date 2025/3/21 9:31
 */

@Data
public class ListAssignmentDto {

    private  Long id;

    /** 课程ID */
    private Long courseId;
    /** 学年 */
    private String year;
    /** 所属班级 */
    private Long gradeId;
    /** 标题 */
    private String title;
    /** 作业要求 */
    private String summary;
    /** 排序 */
    private Integer sort;
    /** 提交人数 */

    private Integer submitNumber;
    /** 评分 分为已评分1和未评分0 */

    private Integer noScore;
    /**小组编号 */
    private Long groupId;
    /**团队类别 */
    private Integer teamCategory;
    /** 创建时间 */

    private Date creationTime;
    /** 班级对象 */

    private Grade grade;
    /** 课程对象 */

    private Course course;
    /** 课程小组对象 */
    private CourseGroup courseGroup;
}
