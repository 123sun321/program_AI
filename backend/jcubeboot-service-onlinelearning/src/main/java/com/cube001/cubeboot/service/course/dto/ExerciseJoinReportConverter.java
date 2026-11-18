package com.cube001.cubeboot.service.course.dto;

import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.course.entity.ExerciseJoinReport;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Copyright (C),2018-2019, ZhengZhouChuangZhi. Co.,Ltd.
 * FileName: ExerciseJoinReportConverter
 * ExerciseJoinReportConverter双向转换类接口
 *
 * @author Juci
 * @date 2025/03/28 18:11
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface ExerciseJoinReportConverter {
    ExerciseJoinReport dtoToEntity(ExerciseJoinReportDto exerciseJoinReportDto);
    ExerciseJoinReport editDtoToEntity(ExerciseJoinReportDto exerciseResourceDto);
    ExerciseJoinReportDto entityToDto(ExerciseJoinReport exerciseJoinReport);
    List<ExerciseJoinReportDto> listEntityToDto(List<ExerciseJoinReport> exerciseJoinReports);
    Page<ExerciseJoinReportDto> pageEntityToDto(Page<ExerciseJoinReport> page);
}
