package com.cube001.cubeboot.community.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import lombok.Data;
import lombok.experimental.Accessors;

/***
* Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
* FileName: CommunityPlate
* 社区板块实体类
*
* @author anonymous
* @date   2019/3/19 16:12
* @version 1.0
*/
@Data
@Accessors(chain = true)
@TableName("t_community_plate")
public class CommunityPlate extends FullAuditedEntityNPk<CommunityPlate> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键*/
    @TableId(type = IdType.ID_WORKER)
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 简介
     */
    private String summary;
    /**
     * 角色编号（逗号分隔）
     */
    private String roles;

}
