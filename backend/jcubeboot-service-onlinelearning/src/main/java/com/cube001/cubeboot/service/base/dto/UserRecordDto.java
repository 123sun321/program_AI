package com.cube001.cubeboot.service.base.dto;

import com.cube001.cubeboot.core.authorization.entity.User;
import lombok.Data;

import java.util.Date;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: UserRecordDto
 * 用户浏览记录Dto
 *
 * @author Juci
 * @version 1.0
 * @date 2025/4/22 14:17
 */
@Data
public class UserRecordDto {
    /** id */
    private Long id;
    /**浏览者*/
    private Long visitor;
    /**被浏览者*/
    private Long areVisitor;
    /**访问时间*/
    private Date creationTime;
    /**访问者信息*/
    private User user;
}
