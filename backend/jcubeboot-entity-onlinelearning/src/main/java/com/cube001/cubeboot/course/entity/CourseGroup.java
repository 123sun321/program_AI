package com.cube001.cubeboot.course.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: CourseGroup
 * CourseGroup
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/7/4 14:22
 */
@Data
@TableName("t_course_group")
@Accessors(chain = true)
public class CourseGroup extends FullAuditedEntityNPk<CourseGroup> {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.ID_WORKER)
    private Long id;
    /**小组名称*/
    private String name;
    /**所属课程*/
    private Long courseId;
}
