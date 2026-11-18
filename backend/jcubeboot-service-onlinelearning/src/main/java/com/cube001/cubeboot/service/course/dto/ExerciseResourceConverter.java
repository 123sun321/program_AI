package com.cube001.cubeboot.service.course.dto;


import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.course.entity.ExerciseResource;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Copyright (C),2018-2019, ZhengZhouChuangZhi. Co.,Ltd.
 * FileName: ExerciseResourceConverter
 * ExerciseResourceConverter双向转换类接口
 *
 * @author Juci
 * @date 2025/03/11 15:11
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface ExerciseResourceConverter {
    ExerciseResource dtoToEntity(ExerciseResourceDto exerciseResourceDto);
    ExerciseResource editDtoToEntity(ExerciseResourceDto exerciseResourceDto);
    ExerciseResourceDto entityToDto(ExerciseResource exerciseResource);
    List<ExerciseResourceDto> listEntityToDto(List<ExerciseResource> exerciseResources);
    Page<ExerciseResourceDto> pageEntityToDto(Page<ExerciseResource> page);
}
