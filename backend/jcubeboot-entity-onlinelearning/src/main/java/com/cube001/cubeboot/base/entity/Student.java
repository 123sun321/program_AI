package com.cube001.cubeboot.base.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotations.Version;

import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.core.authorization.entity.User;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntity;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
* Copyright (C), 2025 - 2026, zxy.
* FileName: Student.java
* 类的详细说明
*
* @author anonymous
* @date 2025/3/8 11:14
* @version 1.0
*/
@Data
@Accessors(chain = true)
@TableName("t_base_student")
public class Student extends FullAuditedEntityNPk<Student> {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ID_WORKER)
    private Long id;

    /** 编号 */
    private String code;

    /** 姓名 */
    private String name;

    /** 所属班级 */
    @TableField("grade_id")
    private Long gradeId;
    /**
     * 性别
     */
    private String sex;

    /** 联系方式 */
    private String contact;

    /** 出生日期 */
    private Date birthday;


    /** 电子邮件 */
    private String email;

    /** 所属专业id */
    @TableField("profession_id")
    private Long professionId;

    /** 家庭住址 */
    private String address;

    /** 民族 */
    private String nation;

    /** 籍贯 */
    private String nationality;

    /** 政治面貌 */
    private String politics;

    /** 学历 */
    private String education;

    /** 证件号码 */
    private String idnumber;

    /** 学制 */
    @TableField("schooling_length")
    private String schoolingLength;

    /** 头像 */
    @TableField("img_url")
    private String imgUrl;

    /** 紧急联系人 */
    private String linkman;

    /** 紧急联系电话 */
    @TableField("linkman_contact")
    private String linkmanContact;

    /** 与紧急联系关系*/
    @TableField("linkman_relation")
    private String linkmanRelation;
    /**当前连续学习天数*/
    private Integer continuousDay;
    /**最大连续天数*/
    private Integer maxDay;
    /** 班级 */
    @TableField(exist = false)
    private Grade grade;

    /**学生的个人积分总和 */
    @TableField("integral_score")
    private Integer integralScore;

    /** 专业 */
    @TableField(exist = false)
    private Profession profession;

    /** 用户id */
    @TableField("user_id")
    private Long userId;

    /**关联的用户信息*/
    @TableField(exist = false)
    private User user;
}
