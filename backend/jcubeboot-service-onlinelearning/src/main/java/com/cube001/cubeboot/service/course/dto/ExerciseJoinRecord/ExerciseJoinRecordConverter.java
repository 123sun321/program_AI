package com.cube001.cubeboot.service.course.dto.ExerciseJoinRecord;

import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.course.entity.ExerciseJoinRecord;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Copyright (C),2018-2019, ZhengZhouChuangZhi. Co.,Ltd.
 * FileName: ExerciseJoinRecordConverter
 * ExerciseJoinRecordConverter双向转换类接口
 *
 * @author anonymous
 * @date 2025/04/08 13:43
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface ExerciseJoinRecordConverter {
    ExerciseJoinRecord dtoToEntity(ExerciseJoinRecordDto exerciseJoinRecordDto);
    ExerciseJoinRecord editDtoToEntity(ExerciseJoinRecordDto exerciseJoinRecordDto);
    ExerciseJoinRecordDto entityToDto(ExerciseJoinRecord exerciseJoinRecord);
    List<ExerciseJoinRecordDto> listEntityToDto(List<ExerciseJoinRecord> exerciseJoinRecords);
    Page<ExerciseJoinRecordDto> pageEntityToDto(Page<ExerciseJoinRecord> page);
}
