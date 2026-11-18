package com.cube001.cubeboot.service.course.dto;

import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.course.entity.JoinCoursePath;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Copyright (C),2018-2019, ZhengZhouChuangZhi. Co.,Ltd.
 * FileName: [文件名]
 * 类的详细说明
 *
 * @author HuangPengju
 * @version 1.0
 * @date 2025/3/14 18:20
 */

@Mapper(componentModel = "spring")
public interface JoinCoursePathConverter {

    JoinCoursePath dtoToEntity(JoinCoursePathDto joinCoursePathDto);
    JoinCoursePath editDtoToEntity(JoinCoursePathDto joinCoursePathDto);
    JoinCoursePathDto entityToDto(JoinCoursePath joinCoursePath);
    List<JoinCoursePathDto> listEntityToDto(List<JoinCoursePath> codes);
    Page<JoinCoursePathDto> pageEntityToDto(Page<JoinCoursePathDto> page);
}
