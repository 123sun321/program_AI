package com.cube001.cubeboot.base.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.Version;

import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
* Copyright (C), 2025 - 2026, zxy.
* FileName: LabelContent.java
* 类的详细说明
*
* @author anonymous
* @date 2025/3/12 9:30
* @version 1.0
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_base_label_content")
public class LabelContent extends FullAuditedEntityNPk<LabelContent> {

    private static final long serialVersionUID = 1L;

    /**
     * 内容标签表
     */
    @TableId(type = IdType.ID_WORKER)
    private Long id;
    /**
     * 类型（0-课程；1-实验）
     */
    private Integer type;
    /**
     * 标签编号
     */
    @TableField("label_id")
    private Long labelId;
    /**
     * 链接编号
     */
    @TableField("link_id")
    private Long linkId;
    /**
     * 排序
     */
    private Integer sort;


}
