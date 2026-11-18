package com.cube001.cubeboot.base.controller;


import com.cube001.cubeboot.core.domain.IApplicationService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
* Copyright (C), 2025 - 2026, zxy.
* FileName: LabelContentController.java
* 类的详细说明
*
* @author anonymous
* @date 2025/3/12 9:30
* @version 1.0
*/
@RestController
@RequestMapping(value = "${cubeboot.app-manager-path}/labelContent")
@Api(description = "内容标签管理")
public class LabelContentController implements IApplicationService {

}

