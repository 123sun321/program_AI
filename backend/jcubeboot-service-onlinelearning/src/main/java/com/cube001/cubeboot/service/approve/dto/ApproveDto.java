package com.cube001.cubeboot.service.approve.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cube001.cubeboot.approve.entity.enums.ApproveEnum;
import com.cube001.cubeboot.core.util.CustomEnumDeserializer;
import lombok.Data;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [ 文件名 ]
 * 类的详细说明
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/4/22$ 13:45$
 */
@Data
public class ApproveDto {
    /**
     * 主键
     */
    private Long id;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 关联的相关id, 例如用户点赞的练习列表的id
     */
    private Long linkId;
    /**
     * 该点赞类型,0-练习列表点赞,
     */
    @JSONField(serialzeFeatures = SerializerFeature.WriteEnumUsingToString,deserializeUsing = CustomEnumDeserializer.class)
    private ApproveEnum category;
}
