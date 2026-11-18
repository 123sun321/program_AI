package com.cube001.cubeboot.base.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Copyright (C),2018-2019, ZhengZhouChuangZhi. Co.,Ltd.
 * FileName: [RewardPunishCategory]
 * 奖罚实体类
 *
 * @author HuangPengju
 * @Date 2018-11-27
 * @version 1.0
 */
@Data
@Accessors(chain = true)
@TableName("t_base_reward_punish_category")
public class RewardPunishCategory extends FullAuditedEntityNPk<RewardPunishCategory> {
    /** 实体id */
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.ID_WORKER)
    private Long id;

    /** 奖惩编号 */
    private String number;

    /** 奖惩名称 */
    private String name;

    /** 学校id */
    private Long schoolId;

    /** 类型 */
    private String type;

    /** 级别 */
    private String level;

    /** logo */
    private String logo;

    /** 描述 */
    private String desc;
}

