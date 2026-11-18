package com.cube001.cubeboot.service.base.dto.base;

import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.base.entity.Label;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [ 文件名 ]
 * 类的详细说明
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/3/8$ 17:59$
 */
@Mapper(componentModel = "spring")
public interface LabelConverter {

    Label dtoToEntity(LabelDto labelDto);
    List<Label> listDtoToEntity(List<LabelDto> labelDtos);

    LabelDto entityToDto(Label label);
    List<LabelDto> listEntityToDto(List<Label> labels);
    Page<LabelDto> pageToDto(Page<Label> labelPage);

}
