package com.cube001.cubeboot.service.community.dto;

import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.community.entity.CommunityPlate;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [ 文件名 ]
 * 类的详细说明
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/3/1916:59
 */
@Mapper(componentModel = "spring")
public interface CommunityPlateConverter {

    CommunityPlate dtoToCommunityPlate(CommunityPlateDto communityPlateDto);
    CommunityPlateDto communityPlateDto(CommunityPlate communityPlate);
    List<CommunityPlateDto> dtoToListCommunityPlate(List<CommunityPlate> communityPlateList);
    Page<CommunityPlateDto> pageCommunityPlateDto(Page<CommunityPlate> page);
    List<CommunityPlate> communityPlateDtoList(List<CommunityPlateDto> communityPlateDtoList);
}
