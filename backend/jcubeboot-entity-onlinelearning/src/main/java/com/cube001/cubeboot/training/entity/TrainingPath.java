package com.cube001.cubeboot.training.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: TrainingPaths.java
 * 练习目录的完整实体类
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/3/13 15:26
 */
@Data
@Accessors(chain = true)
@TableName(value = "t_training_path")
public class TrainingPath extends FullAuditedEntityNPk<TrainingPath> {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ID_WORKER)
    private Long id;

    /**目录名称 */
    private String title;

    /**目录父编号   */
    @TableField("parent_id")
    private Long parentId;

    /**用户编号*/
    @TableField("student_id")
    private Long studentId;

    /**是否系统目录*/
    private Boolean system;

    /**排序*/
    private Integer sort;
}
