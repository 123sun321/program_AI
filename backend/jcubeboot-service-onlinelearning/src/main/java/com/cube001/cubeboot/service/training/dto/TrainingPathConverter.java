package com.cube001.cubeboot.service.training.dto;

import com.cube001.cubeboot.training.entity.TrainingPath;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: ${FILENAME}
 * TrainingPath类的双向转换说明
 *
 * @author 宋泽山
 * @version 1.0
 * @date 2025/3/17 14:17
 */
@Mapper(componentModel = "spring")
public interface TrainingPathConverter {
    TrainingPathDto entityToDto(TrainingPath trainingPath);
    List<TrainingPathDto> listEntityToDto(List<TrainingPath> trainingPath);
}
