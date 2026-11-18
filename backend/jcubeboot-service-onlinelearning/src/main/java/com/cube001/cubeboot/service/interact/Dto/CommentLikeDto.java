package com.cube001.cubeboot.service.interact.Dto;

import lombok.Data;

/**
 * Copyright (C),2018-2019, ZhengZhouChuangZhi. Co.,Ltd.
 * FileName: 点赞Dto
 * 类的详细说明
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/3/21 9:53
 */
@Data
public class CommentLikeDto {
    private Long id;
    private Long userId;
    private Long commentId;
    private Long lid;
}
