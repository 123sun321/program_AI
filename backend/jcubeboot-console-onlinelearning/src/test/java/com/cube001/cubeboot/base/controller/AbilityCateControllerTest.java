package com.cube001.cubeboot.base.controller;

import com.alibaba.fastjson.JSONObject;
import com.cube001.cubeboot.AbstractWebShiroTest;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.Factory;
import org.apache.shiro.util.ThreadContext;
import org.junit.Before;
import org.junit.Test;
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

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: AbilityCateControllerTest.java
 * 能力要项测试类
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/12/18 15:58
 */
@RunWith(SpringRunner.class)
@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AbilityCateControllerTest extends AbstractWebShiroTest {

    private MockMvc mockMvc; // 模拟MVC对象，通过MockMvcBuilders.webAppContextSetup(this.wac).build()初始化。

    @Autowired
    private WebApplicationContext wac; // 注入WebApplicationContext

    @Autowired
    @Value("${cubeboot.app-manager-path}")
    private String appMangerPath;

//    @Autowired
//    private MockHttpSession session;// 注入模拟的http session
//
//    @Autowired
//    private MockHttpServletRequest request;// 注入模拟的http request\

    @Before // 在测试开始前初始化工作
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:test.shiro.ini");
        setSecurityManager(factory.getInstance());
        login("admin", "12345");
        ThreadContext.bind(subject);
    }

    //@Test
   // @Transactional
    public void add() {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("category", "2");
            map.put("name", "测试能力要项");
            map.put("sort", 2);
            map.put("summary", "测试简介");
            map.put("upScore", 3);
            map.put("system", true);

            MvcResult result = mockMvc.perform(put("/"+appMangerPath + "/ability/add").contentType(MediaType.APPLICATION_JSON).content(JSONObject.toJSONString(map)))
                    .andExpect(status().isOk())// 模拟向testRest发送get请求
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                   // .andExpect(jsonPath("$.data").value(true))
                    .andReturn();// 返回执行请求的结果

            System.out.println(result.getResponse().getContentAsString());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Test
    public void edit() {
    }

    @Test
    public void get() {
    }

    @Test
    public void listAll() {
    }

    @Test
    public void del() {
    }

    @Test
    public void listAbility() {
    }

    @Test
    public void listAbilityByGcId() {
    }
}