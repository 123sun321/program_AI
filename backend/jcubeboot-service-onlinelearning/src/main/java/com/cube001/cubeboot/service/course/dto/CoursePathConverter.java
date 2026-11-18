package com.cube001.cubeboot.service.course.dto;

import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.course.entity.CoursePath;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Copyright (C),2018-2019, ZhengZhouChuangZhi. Co.,Ltd.
 * FileName: [文件名]
 * 类的详细说明
 *
 * @author HuangPengju
 * @version 1.0
 * @date 2025/3/14 10:41
 */

@Mapper(componentModel = "spring")
public interface CoursePathConverter {
    CoursePath dtoToEntity(CoursePathDto coursePathDto);
    CoursePathDto entityToDto(CoursePath coursePath);
    List<CoursePath> listDtoToEntity(List<CoursePathDto> courseDtos);
    List<CoursePathDto> listToDto(List<CoursePath> coursePaths);
    Page<CoursePathDto> pageToDto(Page<CoursePath> coursePathPage);
}
