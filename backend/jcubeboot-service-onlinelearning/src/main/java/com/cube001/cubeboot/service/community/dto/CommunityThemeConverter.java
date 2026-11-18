package com.cube001.cubeboot.service.community.dto;

import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.community.entity.CommunityTheme;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: CommunityThemeConverter
 * 社区主题管理转换类
 *
 * @author 张浩杰
 * @version 1.0
 * @date 2025/3/2115:23
 */
@Mapper(componentModel = "spring")
public interface CommunityThemeConverter {

    CommunityTheme dtoToCommunityTheme(CommunityThemeDto communityThemeDto);
    CommunityThemeDto communityThemeDto(CommunityTheme communityTheme);
    List<CommunityThemeDto> dtoToListCommunityTheme(List<CommunityTheme> communityThemeList);
    Page<CommunityThemeDto> pageCommunityThemeDto(Page<CommunityTheme> page);
    List<CommunityTheme> communityThemeDtoList(List<CommunityThemeDto> communityThemeDtoList);
}
