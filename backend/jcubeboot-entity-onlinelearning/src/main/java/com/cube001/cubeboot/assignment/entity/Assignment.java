package com.cube001.cubeboot.assignment.entity;



import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.base.entity.Grade;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import com.cube001.cubeboot.course.entity.Course;
import com.cube001.cubeboot.course.entity.CourseGroup;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author renjie
 * @since 2019-03-18
 */
@Data
@Accessors(chain = true)
@TableName("t_assignment")
public  class Assignment extends FullAuditedEntityNPk<Assignment> {

    /**编号 */
    @TableId(type=IdType.ID_WORKER)
    private  Long id;

    /**所属课程 */
    private Long courseId;
    /**学年 */
    private String year;
    /**所属班级 */
    private Long gradeId;
    /**标题 */
    private String title;
    /**作业要求 */
    private String summary;
    /**排序 */
    private Integer sort;
    /**提交人数 */
    private Integer submitNumber;
    /* 作业查重时间*/
    private  Date duplicateChecking;
    /**未评分 */
    private Integer noScore;
    /**小组编号 */
    private Long groupId;
    /**团队类别 */
    private Integer teamCategory;
    /** 班级对象 */
    @TableField(exist = false)
    private Grade grade;
    /** 课程对象 */
    @TableField(exist = false)
    private Course course;
    /** 课程小组对象 */
    @TableField(exist = false)
    private CourseGroup courseGroup;



}
