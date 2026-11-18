package com.cube001.cubeboot.person.controller.dto;

import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.core.notification.entity.NotificationMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: MessageDtoConverter.java
 * 站内信dto 转换类
 *
 * @author 张向宇
 * @version 1.0
 * @date 2025/01/08 13:15
 */
@Mapper(componentModel = "spring")
public interface MessageDtoConverter {
    /**
     * NotificationMessage to MessageListDto
     * @description
     * @author 张向宇
     * @date 2025/01/08 13:21
     * @param message
     * @return
     */
    @Mappings({
            @Mapping(source = "message.messagetext.title",target = "title"),
            @Mapping(source = "message.messagetext.messageText",target = "messageText"),
            @Mapping(source = "message.messagetext.sendTime",target = "sendTime"),
            @Mapping(source = "message.messagetext.receiveType",target = "receiveType")
    })
    MessageListDto from(NotificationMessage message);

    /**
     * messageList to MessageListDto
     * @description
     * @author 张向宇
     * @date 2025/01/08 13:22
     * @param messageList
     * @return
     */
    List<MessageListDto> fromList(List<NotificationMessage> messageList);

    Page<MessageListDto> fromList(Page<NotificationMessage> messageList);
}
