package com.cube001.cubeboot.course.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import lombok.Data;
import lombok.experimental.Accessors;
/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: ExerciseResource
 * ExerciseResource
 *
 * @author Administrator
 * @version 1.0
 * @date 2025/3/11 16:13
 */


@Data
@Accessors(chain = true)
@TableName("t_exercise_resource")
public class ExerciseResource extends FullAuditedEntityNPk<ExerciseResource> {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.ID_WORKER)
    private Long id;
    /** 实验编号*/
    private Long exerciseId;
    /**名称*/
    private String name;
    /** 资源链接*/
    private String url;
    /** 排序*/
    private Integer sort;
}
