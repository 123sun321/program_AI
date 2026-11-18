package com.cube001.cubeboot.training.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: Code
 * Code
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/3/11 16:17
 */

@Data
@Accessors(chain = true)
@TableName("t_code")
public class Code extends FullAuditedEntityNPk<Code> {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.ID_WORKER)
    private Long id;
    /**js基础库*/
    private String jsLibrary;
    /**css基础库*/
    private String cssLibrary;
    /** html代码*/
    private String html;
    /** html配置*/
    private String htmlConfig;
    /** js代码*/
    private String js;
    /** js配置*/
    private String jsConfig;
    /** css代码*/
    private String css;
    /** css配置*/
    private String cssConfig;
}