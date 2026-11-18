package com.cube001.cubeboot.jobs;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: com.cube001.cubeboot.jobs
 * 测试工作bean
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/1/10 11:25
 */
public class TestJob extends QuartzJobBean {


    private Long schoolId;

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    //  School school = schoolService.getSchool(schoolId);
    }
}
