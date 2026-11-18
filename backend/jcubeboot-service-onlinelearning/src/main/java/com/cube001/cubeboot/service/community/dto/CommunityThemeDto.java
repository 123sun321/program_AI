package com.cube001.cubeboot.service.community.dto;

import com.cube001.cubeboot.community.entity.CommunityPlate;
import com.cube001.cubeboot.core.authorization.entity.User;
import com.cube001.cubeboot.core.validateGroup.EditChecks;
import com.cube001.cubeboot.interact.entity.Comment;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: CommunityThemeDto
 * 社区主题管理dto
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/3/2115:13
 */
@Data
public class CommunityThemeDto {
    @NotNull(groups = EditChecks.class)
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
    @NotBlank
    @Length(min=1,max = 20,message = "标题名称为1-20个字符")
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 创建时间
     */
    private Date creationTime;
    /** 用户信息 */
    private User user;
    /** 社区板块 */
    private String  plateName;
    /** 评论列表 */
    private List<Comment> commentList;
}
