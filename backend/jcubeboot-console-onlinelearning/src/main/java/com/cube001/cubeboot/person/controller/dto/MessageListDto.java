package com.cube001.cubeboot.person.controller.dto;

import com.cube001.cubeboot.core.notification.entity.enums.SiteMessageTypeEnum;
import lombok.Data;

import java.util.Date;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: MessageListDto.java
 * 站内信列表展示dto
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/01/08 13:07
 */
@Data
public class MessageListDto {
    /**消息编号*/
    private Long id;
    /**发送人id*/
    private Long senderId;
    /**接收人id*/
    private Long receiverId;
    /**接收时间*/
    private Date receiveTime;
    /**已读标志*/
    private Boolean readFlag;
    /**发送时间*/
    private Date sendTime;
    /**标题*/
    private String title;
    /**内容*/
    private String messageText;
    /**消息类型*/
    private SiteMessageTypeEnum receiveType;
}
