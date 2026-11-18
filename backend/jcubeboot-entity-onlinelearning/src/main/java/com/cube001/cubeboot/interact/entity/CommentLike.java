package com.cube001.cubeboot.interact.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: CommentLike
 * 点赞实体类
 * </p>
 *
 * @author qw
 * @since 2019-03-20
 */
@Data
@Accessors(chain = true)
@TableName("t_comment_like")
public class CommentLike extends FullAuditedEntityNPk<CommentLike>{

    private static final long serialVersionUID = 1L;

    /**主键*/
    @TableId(type = IdType.ID_WORKER)
    private Long id;

    private Long userId;

    private Long commentId;


}
