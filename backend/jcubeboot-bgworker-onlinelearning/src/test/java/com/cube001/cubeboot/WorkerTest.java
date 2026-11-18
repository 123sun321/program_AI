package com.cube001.cubeboot;

import com.cube001.cubeboot.core.authorization.service.UserService;
import com.cube001.cubeboot.core.notification.service.MessageSender;
import com.cube001.cubeboot.jobs.*;
import com.cube001.cubeboot.service.base.TeachersService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: com.cube001.cubeboot
 * 类的详细说明
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/1/24 10:34
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class WorkerTest {
    @Autowired
    private MessageSender messageSender;
    @Autowired
    private UserService userService;
    @Autowired
    private TeachersService teachersService;
//    @Autowired
//    private ProfessionalQualityScoreService professionsService;
//    @Autowired
//    private WeekKnowledgeService weekKnowledgeService;

    @Test
    public void remindJob_executeInternal_Test() {
//        RemindJob remindJob = new RemindJob(messageSender, userService, scheduleService);
//        try {
//            remindJob.test();
//
//        } catch (Exception e) {
//
//        }

    }

    @Test
    public void skillJob_executeInternal_test() {
//        SkillJob skillJob = new SkillJob(gradeCourseService, scheduleService, teachersService);
//        skillJob.test();

    }

    @Test
    public void markingJob_executeInternal_test() {
//        MakingsJob makingsJob = new MakingsJob(professionsService, scheduleService, teachersService);
//        makingsJob.test();
}

    @Test
    public void KnowledgeJob_executeInternal_test() {
//        KnowledgeJob knowledgeJob=new KnowledgeJob(weekKnowledgeService,scheduleService,teachersService);
//        knowledgeJob.test();
    }
}
