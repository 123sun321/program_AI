package com.cube001.cubeboot.approve.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.approve.entity.enums.ApproveEnum;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import com.cube001.cubeboot.core.util.CustomEnumDeserializer;
import com.cube001.cubeboot.interact.entity.enums.CategoryEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
* Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
* FileName: Approve.java
*  点赞实体类
*
* @author anonymous
* @date 2025/4/2 15:28
* @version 1.0
*/
@Data
@Accessors(chain = true)
@TableName("t_approve")
public class Approve extends FullAuditedEntityNPk<Approve> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ID_WORKER)
    private Long id;
    /**
     * 用户编号
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 关联的相关id, 例如用户点赞的练习列表的id
     */
    @TableField("link_id")
    private Long linkId;
    /**
     * 该点赞类型,0-练习列表点赞,
     */
    @JSONField(serialzeFeatures = SerializerFeature.WriteEnumUsingToString,deserializeUsing = CustomEnumDeserializer.class)
    private ApproveEnum category;

}
