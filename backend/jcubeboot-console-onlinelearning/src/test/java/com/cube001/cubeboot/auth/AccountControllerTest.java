package com.cube001.cubeboot.auth;

import com.cube001.cubeboot.AbstractWebShiroTest;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.Factory;
import org.apache.shiro.util.ThreadContext;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;


import static org.easymock.EasyMock.createNiceMock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [文件名]
 * 测试用户信息控制器
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/12/18 16:37
 */
@RunWith(SpringRunner.class)
@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerTest extends AbstractWebShiroTest {

    private MockMvc mockMvc; // 模拟MVC对象，通过MockMvcBuilders.webAppContextSetup(this.wac).build()初始化。

    @Autowired
    private WebApplicationContext wac; // 注入WebApplicationContext

    @Autowired
    @Value("${cubeboot.app-manager-path}")
    private String appMangerPath;

    @BeforeClass
    public static void beforeClass() {
    }

    @Before // 在测试开始前初始化工作
    public void setup() {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
//        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:test.shiro.ini");
//        setSecurityManager(factory.getInstance());
//        login("admin", "12345");
//        ThreadContext.bind(subject);
    }

    @Test
    //@Transactional
    public void getUserInfoTest()
    {
//       try {
//           MvcResult result = mockMvc.perform(get("/account/userinfo"))
//                   .andExpect(status().isOk())// 模拟向testRest发送get请求
//                   .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
//                   .andExpect(jsonPath("$.roles").isArray())
//                   .andReturn();// 返回执行请求的结果
//
//           System.out.println(result.getResponse().getContentAsString());
//       }catch (Exception ex)
//       {
//           ex.printStackTrace();
//       }

    }



}
