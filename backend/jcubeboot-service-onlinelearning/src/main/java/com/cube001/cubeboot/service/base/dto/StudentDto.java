package com.cube001.cubeboot.service.base.dto;

import com.cube001.cubeboot.base.entity.Grade;
import com.cube001.cubeboot.base.entity.Profession;
import com.cube001.cubeboot.core.validateGroup.EditChecks;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [ StudentDto ]
 * 学生Dto ：添加、修改、展示
 *
 * @author 郭广明
 * @version 1.0
 * @date 2025/11/2813:37
 */
@Data
public class StudentDto {

    @NotNull(groups = EditChecks.class)
    private Long id;
    /**
     * 学号
     */
    @NotBlank(message = "学号不能为空")
    @Length(min=1,max = 20,message = "学号为1-20个字符")
    private String code;
    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空")
    @Length(min=1,max = 20,message = "姓名为1-20个字符")
    private String name;
    /**
     * 所属班级
     */
    @NotNull(message = "所属班级不可为空")
    private Long gradeId;
    /**
     * 性别
     */
    @NotBlank(message = "性别不可为空")
    private String sex;
    /**
     * 联系方式
     */
    private String contact;
    /**
     * 出生日期
     */
    private Date birthday;
    /**
     * 电子邮件
     */
    private String email;
    /**
     * 所属专业
     */
    @NotNull(message = "所属专业不可为空")
    private Long professionId;
    /**
     * 家庭住址
     */
    private String address;
    /**
     * 民族
     */
    private String nation;
    /**
     * 籍贯
     */
    private String nationality;
    /**
     * 政治面貌
     */
    private String politics;
    /**
     * 学历
     */
    private String education;
    /**
     * 证件号码
     */
    @NotBlank(message = "证件号码不可为空")
    private String idnumber;
    /**
     * 学制
     */
    private String schoolingLength;
    /**
     * 头像
     */
    private String imgUrl;
    /**
     * 紧急联系人
     */
    private String linkman;
    /**
     * 紧急联系电话
     */
    private String linkmanContact;
    /**
     * 与紧急联系关系
     */
    private String linkmanRelation;

    private String year;
    private Grade grade;
    private Profession profession;
    private Long userId;

    /**用户名*/
    private String userName;

    /**用户表中的电子邮箱*/
    private String emailAddress;

    private Boolean emailConfirmed;
    /**用户表中的手机号*/
    private String phoneNumber;

    private Boolean phoneNumberConfirmed;
}
