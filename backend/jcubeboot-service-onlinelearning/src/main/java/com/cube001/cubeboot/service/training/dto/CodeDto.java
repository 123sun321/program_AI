package com.cube001.cubeboot.service.training.dto;

import lombok.Data;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: CodeDto
 * CodeDto
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/3/11 16:24
 */
@Data
public class CodeDto {
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
    /**参加练习时间*/
    private Integer studyTime;
    /**练习编号*/
    private Long exerciseId;
}
