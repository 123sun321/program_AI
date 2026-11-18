package com.cube001.cubeboot.service.base.dto;

import com.cube001.cubeboot.core.validateGroup.EditChecks;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
* Copyright (C), 2025 - 2026, zxy.
* FileName: TeachersDto.java
* 教师dto：添加、修改、展示、删除
*
* @author anonymous
* @date 2025/12/4 14:52
* @version 1.0
*/
@Data
public class TeachersDto {

    @NotNull(groups = EditChecks.class)
    private Long id;

    /** 教师工号 */
    @NotEmpty
    @Length(max = 20,message = "教师工号超过20个字符")
    private  String code;

    /** 姓名 */
    @NotEmpty
    @Length(max = 20,message = "教师姓名超过20个字符")
    private String name;

    /** 身份证号 */
    private String carNo;

    /** 联系方式 */
    private String contact;

    /** 微信 */
    private String wechat;

    /** 邮箱 */
    private String email;

    /** 性别 */
    @NotEmpty
    @Length(max = 5,message = "性别超过5个字符")
    private String sex;

    /** 教师类型（0全职 1大校 2外聘） */
    private Integer type;

    /** 头像 */
    private String imgUrl;

    /** 用户Id */
    private Long userId;
    /** 学校管理员表id */
    private Long adminStatus;

    private String schoolName;

    /**用户名*/
    private String userName;

    /**用户表中的电子邮箱*/
    private String emailAddress;

    private Boolean emailConfirmed;
    /**用户表中的手机号*/
    private String phoneNumber;

    private Boolean phoneNumberConfirmed;
}
