package com.cube001.cubeboot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: WechatAccountConfig
 * 读取微信配置类
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/01/02 10:16
 */
@Data
@Component
@ConfigurationProperties(prefix = "cubeboot.wx")
public class WechatAccountConfig {
    /**公众号appId*/
    private String appId;

    /**公众号appSecret*/
    private String appSecret;
}
