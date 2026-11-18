package com.cube001.cubeboot.service.impl;

import com.cube001.cubeboot.jobs.StudentStudyJob;
import com.cube001.cubeboot.service.StudentStudyService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: com.cube001.cubeboot.service.impl
 * 学生学习记录实现类
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/01/11 14:39
 */
@Service
public class StudentStudyServiceImpl extends JobServiceImpl implements StudentStudyService {

    @Autowired
    private Scheduler scheduler;

    public StudentStudyServiceImpl() {
        super("studentStudyJobJob", "studentStudyJob", "studentStudyJobTrigger", StudentStudyJob.class);
    }

    @Override
    public void build() throws SchedulerException {
        // 每天凌晨一点执行
        createJob("0 0 1 * * ?", null);
    }
}



