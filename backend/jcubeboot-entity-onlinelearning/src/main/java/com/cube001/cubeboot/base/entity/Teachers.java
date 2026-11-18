package com.cube001.cubeboot.base.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.core.authorization.entity.User;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntity;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
* Copyright (C), 2025 - 2026, zxy.
* FileName: Teachers.java
* 教师管理的实体类
*
* @author 马勇超
* @date 2025/12/4 15:00
* @version 1.0
*/
@Data
@Accessors(chain = true)
@TableName("t_base_teacher")
public class Teachers extends FullAuditedEntityNPk<Teachers> {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.ID_WORKER)
    private Long id;

    /** 教师工号 */
    private  String code;

    /** 姓名 */
    private String name;

    /** 身份证号 */
    @TableField("car_no")
    private String carNo;

    /** 联系方式 */
    private String contact;

    /** 微信 */
    private String wechat;

    /** 邮箱 */
    private String email;

    /** 性别 */
    private String sex;

    /** 教师类型（0全职 1大校 2外聘） */
    private Integer type;

    /** 头像 */
    private String imgUrl;

    /** 用户Id */
    private Long userId;

    /** 学校管理员id */
    @TableField(exist = false)
    private Long adminStatus;

    /**教师对应的用户*/
    @TableField(exist = false)
    private User user;
}
