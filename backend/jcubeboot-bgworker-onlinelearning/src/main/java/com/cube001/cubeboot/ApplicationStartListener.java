package com.cube001.cubeboot;

import com.cube001.cubeboot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: com.cube001.cubeboot
 * 类的详细说明
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/1/10 13:51
 */
@Component
public class ApplicationStartListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private StudentStudyService studentStudyService;



    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        try {
            studentStudyService.build();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
