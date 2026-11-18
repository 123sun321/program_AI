package com.cube001.cubeboot.service.base.dto;

import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.base.entity.RewardPunishCategory;
import org.mapstruct.Mapper;

import java.util.List;


/**
 * Copyright (C),2018-2019, ZhengZhouChuangZhi. Co.,Ltd.
 * FileName: [RewardPunishCategoryConverter]
 * RewardPunishCategoryConverter双向转换类接口
 *
 * @author anonymous
 * @Date 2018-11-27
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface RewardPunishCategoryConverter {
    RewardPunishCategory dtoToEntity(RewardPunishCategoryDto dto);
    RewardPunishCategoryDto entityToDto(RewardPunishCategory rewardPunish);
    List<RewardPunishCategoryDto> listEntityToDto(List<RewardPunishCategory> rewardPunishCategories);
    Page<RewardPunishCategoryDto> pageEntityToDto(Page<RewardPunishCategory> page);
}
