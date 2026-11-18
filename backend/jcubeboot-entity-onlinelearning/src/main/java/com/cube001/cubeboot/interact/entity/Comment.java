package com.cube001.cubeboot.interact.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.core.authorization.entity.User;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import com.cube001.cubeboot.core.util.CustomEnumDeserializer;
import com.cube001.cubeboot.course.entity.Course;
import com.cube001.cubeboot.interact.entity.enums.CategoryEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: Comment
 * 路径评论实体类
 * </p>
 *
 * @author qw
 * @since 2019-03-12
 */
@Data
@Accessors(chain = true)
@TableName("t_comment")
public class Comment extends FullAuditedEntityNPk<Comment> {

    private static final long serialVersionUID = 1L;

    /**主键*/
    @TableId(type = IdType.ID_WORKER)
    private Long id;

    /**用户编号*/
    private Long userId;

    /**用户名*/
    private String userName;

    /**内容*/
    private String content;

    /**点赞*/
    private Integer likeNumber;

    /**评价类型(0-社区回帖，1-课程评价，2-路径评价)*/
    @JSONField(serialzeFeatures = SerializerFeature.WriteEnumUsingToString,deserializeUsing = CustomEnumDeserializer.class)
    private CategoryEnum category;

    /**关联编号*/
    private Long linkId;

    /**是否回复*/
    private Boolean reply;

    /**回复用户编号*/
    private Long replyUserId;

    /**回复用户名*/
    private String replyUserName;
    @TableField(exist = false)
    private Course course;
    /**用户表*/
    @TableField(exist = false)
    private User user;
    /** 判断用户是否点赞 */
    @TableField(exist = false)
    private Boolean isAppreciate;

}
