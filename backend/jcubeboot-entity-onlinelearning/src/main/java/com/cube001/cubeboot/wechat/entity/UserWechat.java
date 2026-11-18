package com.cube001.cubeboot.wechat.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: Schedule
 * 用户微信信息实体
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/01/09 09:35
 */
@Data
@Accessors(chain = true)
@TableName("sys_user_wechat")
public class UserWechat {
    private static final long serialVersionUID = 1L;
    /**主键*/
    @TableId(type = IdType.ID_WORKER)
    private Long id;
    /**用户编号*/
    private Long userId;
    /**是否订阅*/
    private Boolean subscribe;
    /**openid*/
    private String openId;
    /**昵称*/
    private String nickname;
    /**性别*/
    private String sex;
    /**语言*/
    private String language;
    /**城市*/
    private String city;
    /**省份*/
    private String province;
    /**国家*/
    private String country;
    /**头像*/
    private String headImgUrl;
    /**unionId*/
    private String unionId;
    /**简介*/
    private String remark;
}
