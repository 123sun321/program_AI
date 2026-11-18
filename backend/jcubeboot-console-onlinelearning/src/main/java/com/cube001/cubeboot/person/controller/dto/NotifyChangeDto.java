package com.cube001.cubeboot.person.controller.dto;

import lombok.Data;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: NotifyChangeDto.java
 * 消息通知开启关闭状态dto
 *
 * @author 张向宇
 * @version 1.0
 * @date 2025/01/07 16:08
 */
@Data
public class NotifyChangeDto {
    /**消息模块名称*/
    private String moduleName;
    /**消息平台类型名称*/
    private String platformName;
    /**用户设置的值*/
    private Boolean userSelect;
}
