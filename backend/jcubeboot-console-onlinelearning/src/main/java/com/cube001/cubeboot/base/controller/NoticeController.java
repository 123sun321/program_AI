package com.cube001.cubeboot.base.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.base.controller.dto.NotificationText;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.core.notification.entity.NotificationMessage;
import com.cube001.cubeboot.core.notification.entity.NotificationMessagetext;
import com.cube001.cubeboot.core.notification.service.NotificationMessageService;
import com.cube001.cubeboot.core.notification.service.NotificationTextService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: com.cube001.cubeboot.base.controller
 * 通知管理controller
 *
 * @author 陈乐乐
 * @version 1.0
 * @date 2025/2/13 17:38
 */
@RestController
@Api(value = "通知管理controller")
@RequestMapping("${cubeboot.app-manager-path}/notice")
public class NoticeController implements IApplicationService {
    @Autowired
    private NotificationMessageService notificationMessageService;
    @Autowired
    private NotificationTextService notificationTextService;


    @ApiOperation(value = "分页")
    @PostMapping(value = "page")
    public Page<NotificationMessagetext> page(@RequestBody CommonSearchDto<NotificationMessagetext> dto) {
        Page<NotificationMessagetext> rtn = notificationMessageService.pageListNotice(dto.getPlusPage(), dto.formToEntityWrapper("", null));
        return rtn;
    }

    @ApiOperation(value = "获取单个")
    @GetMapping(value = "get")
    public NotificationMessagetext get(@RequestParam("id") long id) {

        Page<NotificationMessagetext> page = new Page<>(1, 10);
        Wrapper<NotificationMessagetext> wrapper = new EntityWrapper<>();
        wrapper.eq("id", id);
        page = notificationMessageService.pageListNotice(page, wrapper);
        if (page.getRecords().size() > 0) {
            return page.getRecords().get(0);
        } else {
            return null;
        }

    }

    @ApiOperation(value = "添加")
    @PostMapping("add")
    public boolean add(@RequestBody @Validated  NotificationText notificationText) {
        notificationMessageService.sendMessageToAll(ShiroKit.getUserId(), notificationText.getTitle(), notificationText.getContent());
        return true;
    }

    @ApiOperation(value = "delete")
    @PostMapping("del")
    @Transactional(rollbackFor = Exception.class)
    public boolean del(@RequestBody List<Long> ids) {
        if (ids != null && ids.size() > 0) {
            notificationTextService.deleteBatchIds(ids);
            Wrapper<NotificationMessage> wrapper = new EntityWrapper<>();
            wrapper.in("messageId", ids);
            notificationMessageService.delete(wrapper);
        }
        return true;

    }

}
