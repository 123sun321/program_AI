package com.cube001.cubeboot;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: com.cube001.cubeboot
 * 类的详细说明
 *
 * @author 陈乐乐
 * @version 1.0
 * @date 2025/1/10 14:09
 */
@Configuration
public class WorkerConfig {

    @Autowired
    private JobFactory jobFactory;
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Bean(name = "scheduler")
    public Scheduler scheduler() {
        schedulerFactoryBean.setJobFactory(jobFactory);
        return schedulerFactoryBean.getScheduler();
    }

}
