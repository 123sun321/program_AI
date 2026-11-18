package com.cube001.cubeboot.base.entity;

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
* FileName: Label.java
* 类的详细说明
*
* @author 马勇超
* @date 2025/3/8 17:56
* @version 1.0 
*/
@Data
@Accessors(chain = true)
@TableName("t_base_label")
public class Label extends FullAuditedEntityNPk<Label> {

    @TableId(type = IdType.ID_WORKER)

    /**
     * 标签编号
     */
    private Long id;
    /**
     * 标签名字
     */
    private String name;

    private String pinyin;


}
