package com.cube001.cubeboot.service.community.dto;

import lombok.Data;

import java.util.Date;

/**
 * Copyright (C),2018-2019, ZhengZhouChuangZhi. Co.,Ltd.
 * FileName: SignRecordDto
 * 签到记录Dto
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/3/21 11:15
 */
@Data
public class SignRecordDto {
    private Long id;
    private String studentName;
    private String gradeName;
    private Date signTime;
    private Integer count;
}
