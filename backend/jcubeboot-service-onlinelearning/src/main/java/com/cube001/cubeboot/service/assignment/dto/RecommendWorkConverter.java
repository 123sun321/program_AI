package com.cube001.cubeboot.service.assignment.dto;

import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.assignment.entity.RecommendWork;
import org.mapstruct.Mapper;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [ 文件名 ] RecommendWorkConverter
 * RecommendWorkConverter 作品展示的Dto转换
 *
 * @author 马勇超
 * @version 1.0
 * @date 2025/4/22$ 11:23$
 */
@Mapper( componentModel ="spring")
public interface RecommendWorkConverter {

    RecommendWork dtoToEntity(RecommendWorkDto recommendWorkDto);

    Page<RecommendWorkDto> pageToDto(Page<RecommendWork> recommendWorkPage);

}
