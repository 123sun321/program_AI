package com.cube001.cubeboot.approve.controller;


import com.cube001.cubeboot.approve.entity.Approve;
import com.cube001.cubeboot.approve.entity.enums.ApproveEnum;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.service.approve.ApproveService;
import com.cube001.cubeboot.service.approve.dto.ApproveConverter;
import com.cube001.cubeboot.service.approve.dto.ApproveDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
* Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
* FileName: ApproveController.java
* 通用点赞类 的控制器
*
* @author anonymous
* @date 2025/4/3 16:31
* @version 1.0
*/
@RestController
@Api(value = "approve Controller",description = "练习和作品点赞 Controller")
@RequestMapping(value = "${cubeboot.app-manager-path}/approve")
public class ApproveController implements IApplicationService {

    @Autowired
    private ApproveService service;
    @Autowired
    private ApproveConverter converter;

    /**
    * @description 点赞优秀作品
    * @author anonymous
    * @date 2025/4/22 13:46
    * @param id
    */
    @ApiOperation(value = "点赞优秀作品")
    @PostMapping(value = "add")
    public void add(@RequestParam("id") Long id){
        Approve approve = new Approve();
        Long userId = ShiroKit.getUserId();
        approve.setUserId(userId);
        approve.setLinkId(id);
        approve.setCategory(ApproveEnum.ExcellentWorkApprove);
        service.insert(approve);
    }


}

