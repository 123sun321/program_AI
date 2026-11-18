package com.cube001.cubeboot.service.course.dto;

import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.course.entity.Exercise;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Copyright (C),2018-2019, ZhengZhouChuangZhi. Co.,Ltd.
 * FileName: ExerciseConverter
 * ExerciseConverter双向转换类接口
 *
 * @author Juci
 * @date 2025/03/11 15:11
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface ExerciseConverter {
    Exercise dtoToEntity(ExerciseDto exerciseDto);
    Exercise editDtoToEntity(ExerciseDto exerciseDto);
    ExerciseDto entityToDto(Exercise exercise);
    List<ExerciseDto> listEntityToDto(List<Exercise> exercises);
    Page<ExerciseDto> pageEntityToDto(Page<Exercise> page);
}
