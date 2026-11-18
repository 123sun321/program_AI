package com.cube001.cubeboot.base.controller.dto;

import com.cube001.cubeboot.core.validateGroup.EditChecks;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: com.cube001.cubeboot.base.controller.dto
 * 消息
 *
 * @author 陈乐乐
 * @version 1.0
 * @date 2025/2/14 13:12
 */
@Data
public class NotificationText {
    @NotEmpty
    private String title;
    @Size(min = 1, max = 500)
    private String content;
    @NotNull(groups = EditChecks.class)
    private Long id;
}
