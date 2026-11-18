package com.cube001.cubeboot.service.course.dto;

import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.course.entity.CourseGroup;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: CourseGroupConverter
 * CourseGroupConverter
 *
 * @author Juci
 * @version 1.0
 * @date 2025/7/5 13:32
 */
@Mapper(componentModel = "spring")
public interface CourseGroupConverter {
    CourseGroup dtoToEntity(CourseGroupDto dto);
    CourseGroupDto entityToDto(CourseGroup courseCollect);
    List<CourseGroupDto> listEntityToDto(List<CourseGroup> courseCollects);
    List<CourseGroup> listDtoToEntity(List<CourseGroupDto> courseCollectDtos);
    Page<CourseGroupDto> pageEntityToDto(Page<CourseGroup> page);
}
