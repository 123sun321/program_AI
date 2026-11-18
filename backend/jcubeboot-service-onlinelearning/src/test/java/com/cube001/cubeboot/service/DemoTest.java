package com.cube001.cubeboot.service;

import com.cube001.cubeboot.core.domain.tenant.TenantFliter;
import com.cube001.cubeboot.service.course.CourseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: DemoTest.java
 * 单元测试示例类
 *
 * @author 张向宇
 * @version 1.0
 * @date 2025/12/06 10:53
 */
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {"dev"})
@SpringBootTest(classes=ServiceTestApplication.class)
public class DemoTest {
    @Autowired
    private CourseService courseService;

    @Test
    public void  getCourseTest()
    {
       TenantFliter tenantFliter=new TenantFliter(true,"123"); //设置租户id为123
       //Course course= courseService.selectById(1065449475645718530L);
    }
}
