package com.cube001.cubeboot.service.base;

import com.baomidou.mybatisplus.service.IService;
import com.cube001.cubeboot.base.entity.RewardPunishCategory;

import java.util.List;

/**
 * Copyright (C),2018-2019, ZhengZhouChuangZhi. Co.,Ltd.
 * FileName: [RewardPunishCategoryService]
 * RewardPunishCategoryService服务层接口
 *
 * @author anonymous
 * @Date 2018-11-27
 * @version 1.0
 */
public interface RewardPunishCategoryService extends IService<RewardPunishCategory> {
    /**
     * 功能：获取全部奖惩信息
     * @return 全部奖惩信息
     */
    List<RewardPunishCategory> getRewardPunish();
}
