package com.cube001.cubeboot.service.interact.Dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cube001.cubeboot.core.authorization.entity.User;
import com.cube001.cubeboot.core.util.CustomEnumDeserializer;
import com.cube001.cubeboot.core.validateGroup.EditChecks;
import com.cube001.cubeboot.course.entity.Course;
import com.cube001.cubeboot.interact.entity.Comment;
import com.cube001.cubeboot.interact.entity.enums.CategoryEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Copyright (C),2018-2019, ZhengZhouChuangZhi. Co.,Ltd.
 * FileName: [文件名]
 * 类的详细说明
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/3/18 16:21
 */
@Data
public class CommentDto {
    /**主键*/
    @NotNull(groups = EditChecks.class)
    private Long id;

    /**用户编号*/
    private Long userId;

    /**用户名*/
    private String userName;

    /**内容*/
    @NotBlank
    private String content;

    /**点赞*/
    private Integer likeNumber;

    /**评价类型(1-社区回帖;2-课程评价;3-路径评价;4-报告评价)*/
    @JSONField(serialzeFeatures = SerializerFeature.WriteEnumUsingToString,deserializeUsing = CustomEnumDeserializer.class)
    private CategoryEnum category;

    /**关联编号*/
    private Long linkId;

    /**是否回复*/
    @NotNull
    private Boolean reply;
    /**回复用户编号*/
    private Long replyUserId;
    /**回复用户名*/
    private String replyUserName;
    /**课程*/
    private Course course;
    /**创建时间*/
    private Date creationTime;
    /**点赞编号*/
    private Long likeId;
    /**用户表*/
    private User user;
    /** 用户回复内容列表 */
    private List<Comment> commentList;
    /** 判断用户是否点赞 */
    private Boolean isAppreciate;
}
