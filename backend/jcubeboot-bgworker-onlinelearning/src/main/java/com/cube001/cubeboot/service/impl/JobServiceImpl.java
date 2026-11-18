package com.cube001.cubeboot.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cube001.cubeboot.base.entity.Teachers;
import com.cube001.cubeboot.core.domain.tenant.TenantFliter;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: com.cube001.cubeboot.service.impl
 * 类的详细说明
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/1/11 17:06
 */
public abstract class JobServiceImpl {

    private String jobKey;
    private String group;
    private String triggerKey;
    private Class<? extends Job> scheduleJob;

    @Autowired
    private Scheduler scheduler;

    public JobServiceImpl(String jobKey, String group, String triggerKey, Class<? extends Job> scheduleJob) {
        this.jobKey = jobKey;
        this.group = group;
        this.triggerKey = triggerKey;
        this.scheduleJob = scheduleJob;
    }


    protected void createJob(String corn, Map<String, String> jobData) throws SchedulerException {

        JobBuilder builder = JobBuilder.newJob(scheduleJob)
                .withIdentity(jobKey, group);
        if (jobData != null) {
            for (String key : jobData.keySet()) {
                builder.usingJobData(key, jobData.get(key));
            }
        }
        JobDetail jobDetail = builder.build();

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(corn)
                .withMisfireHandlingInstructionDoNothing();


        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey, group)
                .withSchedule(scheduleBuilder)
                .startNow()
                .build();

        if (scheduler.checkExists(new JobKey(jobKey, group))) {
            scheduler.deleteJob(new JobKey(jobKey,group));
        }
        scheduler.scheduleJob(jobDetail, cronTrigger);

    }
}
