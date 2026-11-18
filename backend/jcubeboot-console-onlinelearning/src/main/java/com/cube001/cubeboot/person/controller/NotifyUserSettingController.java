package com.cube001.cubeboot.person.controller;

import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.notification.service.NotificationUserSettingService;
import com.cube001.cubeboot.core.notification.service.dto.NotificationUserSettingDto;
import com.cube001.cubeboot.person.controller.dto.NotifyChangeDto;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: NotifyUserSettingController.java
 * 用户消息接收设置controller
 *
 * @author 张向宇
 * @version 1.0
 * @date 2025/01/06 16:09
 */
@Api(value = "NotifyUserSettingController", description = "用户消息设置")
@RestController
@RequestMapping(value = "${cubeboot.app-manager-path}/notifySetting")
public class NotifyUserSettingController implements IApplicationService {

    @Autowired
    private NotificationUserSettingService notificationUserSettingService;

    /**
     * 获取当前登录用户的消息接收配置信息
     * @description
     * @author 张向宇
     * @date 2025/01/06 16:12
     * @return
     */
    @GetMapping(value = "/get")
    public List<NotificationUserSettingDto> getUserSetting()
    {
        Long userId= ShiroKit.getUserId();
        List<NotificationUserSettingDto> settingDtos= notificationUserSettingService.getUserSettingGrouped(userId);
        return settingDtos;
    }

    /**
     * 设置用户消息配置状态
     * @description
     * @author 张向宇
     * @date 2025/01/07 16:09
     * @param notifiChangeDto 设置信息
     * @return
     */
    @PostMapping(value = "/set")
    public void setUserSetting(@RequestBody NotifyChangeDto notifiChangeDto)
    {
        Long userId= ShiroKit.getUserId();
        notificationUserSettingService.setUserSetting(userId,notifiChangeDto.getModuleName(),notifiChangeDto.getPlatformName(),notifiChangeDto.getUserSelect());
    }
}
