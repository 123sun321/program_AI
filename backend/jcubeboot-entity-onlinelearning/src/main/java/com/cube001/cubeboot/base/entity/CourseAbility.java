package com.cube001.cubeboot.base.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.core.domain.entity.EntityNPk;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: com.cube001.cubeboot.base.entity
 * 课程和评分项关联表
 *
 * @author 陈乐乐
 * @version 1.0
 * @date 2025/12/3 13:47
 */
@Data
@Accessors(chain = true)
@TableName("t_courseability")
public class CourseAbility extends EntityNPk<CourseAbility> {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long abilityId;

    private Long courseId;

    /**
     * 能力要项名称
     */
    @TableField(exist = false)
    private String abilityName;
    /**
     * 能力要项类别
     */
    @TableField(exist = false)
    private String category;
}
