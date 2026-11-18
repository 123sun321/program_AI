package com.cube001.cubeboot.service.interact.Dto;

import lombok.Data;

import java.util.Date;

/**
 * Copyright (C),2018-2019, ZhengZhouChuangZhi. Co.,Ltd.
 * FileName: [文件名]
 * 类的详细说明
 *
 * @author qw
 * @version 1.0
 * @date 2025/3/19 11:07
 */
@Data
public class PathCommentDto {
    private Long id;
    private Long userId;
    private String userName;
    private String content;
    private Date creationTime;
    private String imgUrl;
    private Boolean reply;
    private Long replyUserId;
    private String replyUserName;
}
