package com.cube001.cubeboot.base.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.annotations.Version;

import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntity;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * Copyright (C),2018-2019, ZhengZhouChuangZhi. Co.,Ltd.
 * FileName: [Studenthonor]
 * 学生奖惩实体类
 *
 * @author anonymous
 * @Date 2018-11-30
 * @version 1.0
 */
@Data
@Accessors(chain = true)
@TableName("t_base_studentHonor")
public class Studenthonor extends FullAuditedEntityNPk<Studenthonor> {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.ID_WORKER)
    private Long id;
    /** 学生编号 */
    private Long studentId;

    /** 荣誉证书原图 */
    private String masterUrl;

    /** 荣誉证书缩略图 */
    private String thumbnailUrl;

    /** 简介 */
    @TableField(value = "abstract")
    private String abstracts;

    /** 奖惩表id */
    private Long rewardPunishCategoryId;

    /** 学年 */
    private String schoolYear;

    /** 学生实体 */
    @TableField(exist = false)
    private Student student;

    /** 奖惩类别实体 */
    @TableField(exist = false)
    private RewardPunishCategory rewardPunishCategory;
}
