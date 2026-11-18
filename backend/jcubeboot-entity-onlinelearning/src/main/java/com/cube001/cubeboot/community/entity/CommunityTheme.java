package com.cube001.cubeboot.community.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotations.Version;

import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.core.authorization.entity.User;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import com.cube001.cubeboot.interact.entity.Comment;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/***
* Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
* FileName: CommunityTheme
* 社区主题管理实体类
*
* @author 张浩杰
* @date   2019/3/21 15:01
* @version 1.0
*/
@Data
@Accessors(chain = true)
@TableName("t_community_theme")
public class CommunityTheme extends FullAuditedEntityNPk<CommunityTheme> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**主键*/
    @TableId(type = IdType.ID_WORKER)
    private Long id;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 板块
     */
    private Long plate;
    /**
     * 查看
     */
    private Integer click;
    /**
     * 回复
     */
    private Integer commentNumber;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /** 用户信息 */
    @TableField(exist = false)
    private User user;
    /** 社区板块 */
    @TableField(exist = false)
    private String plateName;
    /** 评论列表 */
    @TableField(exist = false)
    private List<Comment> commentList;
}
