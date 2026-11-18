package com.cube001.cubeboot.service.course.dto;

import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.course.entity.ExerciseJoin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [ 文件名 ]
 * 类的详细说明
 *
 * @author 马勇超
 * @version 1.0
 * @date 2025/3/12$ 14:54$
 */
@Mapper(componentModel = "spring")
public interface ExerciseJoinConvter {

    ExerciseJoin dtoToEntity(ExerciseJoinDto exerciseJoinDto);
    @Mappings({
            @Mapping(source = "exerciseJoin.exercise",target = "exercise"),
            @Mapping(source = "exerciseJoin.student",target = "student")
    })
    ExerciseJoinDto entityToDto(ExerciseJoin exerciseJoin);

    List<ExerciseJoinDto> list2Dto(List<ExerciseJoin> exerciseJoins);

    Page<ExerciseJoinDto>  pageToDto(Page<ExerciseJoin> page);

}
