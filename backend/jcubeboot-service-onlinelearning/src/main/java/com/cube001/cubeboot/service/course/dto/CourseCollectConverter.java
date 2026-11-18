package com.cube001.cubeboot.service.course.dto;

import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.course.entity.Course;
import com.cube001.cubeboot.course.entity.CourseCollect;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: CourseCollectConverter
 * 课程收藏双向转换
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/3/28 10:25
 */
@Mapper(componentModel = "spring")
public interface CourseCollectConverter {
    CourseCollect dtoToEntity(CourseCollectDto dto);
    CourseCollectDto entityToDto(CourseCollect courseCollect);
    List<CourseCollectDto> listEntityToDto(List<CourseCollect> courseCollects);
    List<CourseCollect> listDtoToEntity(List<CourseCollectDto> courseCollectDtos);
    Page<CourseCollectDto> pageEntityToDto(Page<CourseCollect> page);
}
