package com.cube001.cubeboot.service.training.dto;

import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.training.entity.Training;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;


/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: TrainingConverter
 * Training的双向枚举类
 *
 * @author 宋泽山
 * @version 1.0
 * @date 2025/3/16 15:22
 */
@Mapper(componentModel = "spring")
public interface TrainingConverter {
    Training dtoToEntity(TrainingDto trainingDto);
    TrainingDto entityToDto(Training training);
    List<TrainingDto> listEntityToDto(List<Training> training);
    /**pageToDto 也是支持的*/
    Page<TrainingDto> pageToDto(Page<Training> trainingPage);
    @Mappings({
            @Mapping(source = "training.student.imgUrl",target = "imgUrl"),
            @Mapping(source = "training.student.name",target = "studentName"),
            @Mapping(source = "training.grade.name",target = "gradeName"),
            @Mapping(source = "training.student.id",target = "stuId"),
            @Mapping(source = "training.student.integralScore",target = "integralScore"),
            @Mapping(source = "training.student.userId",target = "userId")
    })
    TrainingPageDto entityToPageDto(Training training);

   Page<TrainingPageDto> pageToPageDto(Page<Training> training);
}
