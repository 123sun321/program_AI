package com.cube001.cubeboot.person.controller;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.core.notification.entity.NotificationMessage;
import com.cube001.cubeboot.core.notification.service.NotificationMessageService;
import com.cube001.cubeboot.person.controller.dto.MessageDtoConverter;
import com.cube001.cubeboot.person.controller.dto.MessageListDto;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: SiteMessageController.java
 * 站内信管理controller
 *
 * @author 张向宇
 * @version 1.0
 * @date 2025/01/08 11:12
 */
@Api(value = "SiteMessageController", description = "站内信")
@RestController
@RequestMapping(value = "${cubeboot.app-manager-path}/message")
public class SiteMessageController implements IApplicationService {

    @Autowired
    private NotificationMessageService notificationMessageService;

    @Autowired
    private MessageDtoConverter messageDtoConverter;

    /**
     * 获取个人的站内信列表(分页)
     * @description
     * @author 张向宇
     * @date 2025/01/08 13:14
     * @param searchDto 查询条件
     * @return
     */
    @PostMapping(value = "/get")
    public Page<MessageListDto> getMessage(@RequestBody CommonSearchDto<NotificationMessage> searchDto)
    {
        Long userId= ShiroKit.getUserId();
        Page<NotificationMessage> page = searchDto.getPlusPage();
        Wrapper<NotificationMessage> searchWrapper = searchDto.formToEntityWrapper(null);
        searchWrapper.eq("receiverId",userId);
        Page<NotificationMessage> messageList=  notificationMessageService.pageListUserReceiveMessage(page,searchWrapper);
        Page<MessageListDto> result=messageDtoConverter.fromList(messageList);
        return  result;
    }

    /**
     * 标记站内信已读状态
     * @description
     * @author 张向宇
     * @date 2025/01/08 14:54
     * @param messageId 站内信id
     * @return 返回最新的未读消息数
     */
    @GetMapping(value = "/read")
    public Integer read(Long messageId)
    {
        Long userId=ShiroKit.getUserId();
        NotificationMessage message=notificationMessageService.selectById(messageId);
        if(!message.getReceiverId().equals(userId))
        {
            throw new FriendException("非法访问，无权设置该条信息的状态!");
        }
        if(message.getReadFlag())
        {
            throw new FriendException("不能重复设置");
        }
        notificationMessageService.read(messageId);
        return notificationMessageService.countNoReadMessage(userId);
    }
    /**
     * 获取未读消息数
     * @description
     * @author 张向宇
     * @date 2025/01/08 15:41
     * @return
     */
    @GetMapping(value = "/countNoRead")
    public Integer getNoReadCount()
    {
        Long userId=ShiroKit.getUserId();
        Integer count= notificationMessageService.countNoReadMessage(userId);
        return count;
    }
}