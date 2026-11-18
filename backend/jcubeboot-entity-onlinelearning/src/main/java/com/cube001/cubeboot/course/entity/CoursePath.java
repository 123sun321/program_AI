package com.cube001.cubeboot.course.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: CoursePath
 * 路径设置实体类
 * </p>
 *
 * @author qw
 * @since 2019-03-11
 */
@Data
@Accessors(chain = true)
@TableName("t_course_path")
public class CoursePath extends FullAuditedEntityNPk<CoursePath> {

    private static final long serialVersionUID = 1L;
    /**主键*/
    @TableId(type = IdType.ID_WORKER)
    private Long id;

    /**课程类别(字典维护)*/
    private String category;

    /**课程编号*/
    private Long courseId;

    /**排序*/
    private Integer sort;

    @TableField(exist = false)
    /** 该课程是否参与 */
    private Boolean isJoinEd;
    /** 课程详情 */
    @TableField(exist = false)
    private Course course;
    /** 实验内容 */
    @TableField(exist = false)
    private Exercise exercise;
    /** 参与实验列表 */
    @TableField(exist = false)
    private List<ExerciseJoinRecord> exerciseJoinRecordList;

}
