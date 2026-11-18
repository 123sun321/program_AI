package com.cube001.cubeboot.service.console.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: BindUserDto
 * 绑定用户Dto
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/01/16 13:37
 */
@Data
public class BindUserDto {
    /**绑定类型 1-教师 2-学生 3-企业*/
    @NotEmpty
    private Integer type;
    /**用户名*/
    @NotEmpty
    private String userName;
    /**密码*/
    @NotEmpty
    private String password;
    /**判断扫码规则*/
    private String strType;
}
