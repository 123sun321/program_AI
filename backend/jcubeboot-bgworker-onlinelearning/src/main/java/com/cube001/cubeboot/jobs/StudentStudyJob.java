package com.cube001.cubeboot.jobs;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.core.domain.tenant.TenantFliter;
import com.cube001.cubeboot.service.base.StudentService;
import com.cube001.cubeboot.service.course.ExerciseJoinRecordService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;


/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: StudentStudyJob
 * 学生学习记录
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/4/11 13:50
 */
public class StudentStudyJob extends QuartzJobBean {

    @Autowired
    private StudentService studentService;
    @Autowired
    private ExerciseJoinRecordService exerciseJoinRecordService;
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try (TenantFliter tenantFliter = new TenantFliter(false)) {
            Wrapper<Student> wrapper = new EntityWrapper<>();
            List<Student> studentList = studentService.selectList(wrapper);
            for (Student student: studentList) {
                exerciseJoinRecordService.updateContinuousDay(student);
            }
        }
    }
}
