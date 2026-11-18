package com.cube001.cubeboot.console.controller.dto;

import lombok.Data;

import java.util.Date;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: UserPersonInfoDto.java
 * 用户个人资料dto
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/01/10 13:46
 */
@Data
public class UserPersonInfoDto {
    private Long id;
    private String alipayAccount;
    private String avatar;
    private String[] roles;
    /**登录用户名*/
    private String userName;
    /**显示名称*/
    private String name;
    /**用户头像*/
    private String userPic;
    /**学校名称*/
    private String schoolName;
    /**院系名称*/
    private String departmentName;
    /**专业名称*/
    private String professionName;
    /**班级名称*/
    private String className;
    /**用户类型0-管理员 1-教师 2-学生*/
    private Integer userType;
    /**学号/工号*/
    private String schoolIdNumber;
    /**注册时间*/
    private Date creationTime;
    /**电子邮件地址*/

    private String emailAddress;
    /**电子邮件是否确认*/
    private Boolean emailConfirmed;
    /**手机号*/
    private String phoneNumber;
    /**手机是否确认*/
    private Boolean phoneNumberConfirmed;
    /**微信昵称*/
    private String weiXinNickName;
    /**微信头像*/
    private String weiXinAvatar;

    private Long tenantId;
}
