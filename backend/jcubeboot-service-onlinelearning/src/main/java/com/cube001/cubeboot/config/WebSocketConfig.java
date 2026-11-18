package com.cube001.cubeboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
* Copyright (C), 2018 - 2019, ZhengZho uChuangZhi. Co., Ltd.
* FileName: WebSocketConfig.java
* 类的详细说明
*
* @author 张人杰
* @date 2025/7/4 14:41
* @version 1.0
*/
@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter()
    {
        return new ServerEndpointExporter();
    }
}
