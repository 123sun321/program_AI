package com.cube001.cubeboot.course.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * <p>
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: JoinCoursePath
 * 路径详情实体类
 * </p>
 *
 * @author anonymous
 * @since 2019-03-14
 */
@Data
@TableName("t_join_course_path")
@Accessors(chain = true)
public class JoinCoursePath extends FullAuditedEntityNPk<JoinCoursePath> {

    private static final long serialVersionUID = 1L;
    /**主键*/
    @TableId(type = IdType.ID_WORKER)
    private Long id;

    /**学生编号*/
    private Long studentId;

    /**学习课程数*/
    private Integer studyNumber;
    @TableField(exist = false)
    private String code;
    @TableField(exist = false)
    private String name;
    @TableField(exist = false)
    private String gradename;
}
