package com.cube001.cubeboot.service.base.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.base.entity.RewardPunishCategory;
import com.cube001.cubeboot.service.base.RewardPunishCategoryService;
import com.cube001.cubeboot.service.base.mapper.RewardPunishCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Copyright (C),2018-2019, ZhengZhouChuangZhi. Co.,Ltd.
 * FileName: [RewardPunishCategoryServiceImpl]
 * RewardPunishCategoryServiceImpl是服务层接口实现类
 *
 * @author anonymous
 * @Date 2018-11-27
 * @version 1.0
 */
@Service
public class RewardPunishCategoryServiceImpl extends ServiceImpl<RewardPunishCategoryMapper, RewardPunishCategory> implements RewardPunishCategoryService {
    @Autowired
    private RewardPunishCategoryService rewardPunishCategoryService;
    /**
     * @description  获取全部没有删除的奖惩信息
     * @author anonymous
     * @date 2025/12/6 10:20
     * @param
     * @return java.util.List<com.cube001.cubeboot.base.entity.RewardPunishCategory>  奖惩信息列表
    */
    @Override
    public List<RewardPunishCategory> getRewardPunish() {
        Wrapper<RewardPunishCategory> rewardPunishCategoryWrapper = new EntityWrapper<>();
        rewardPunishCategoryWrapper = rewardPunishCategoryWrapper.setSqlSelect("id" ,"name","type","level");
        List<RewardPunishCategory> rewardPunishCategories = rewardPunishCategoryService.selectList(rewardPunishCategoryWrapper);
        return rewardPunishCategories;
    }
}
