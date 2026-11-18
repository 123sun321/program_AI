package com.cube001.cubeboot.service.course.dto;

import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.course.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * course双向转换类
 * @author anonymous
 * @since 2018-11-20
 */
@Mapper(componentModel = "spring")
public interface CourseConverter {

    Course dtoToEntity(CourseDto dto);
    @Mappings({
            @Mapping(source ="course.courseCollect.courseId" ,target ="courseId" )
    })
    CourseDto entityToDto(Course course);
    List<CourseDto> listEntityToDto(List<Course> courses);
    List<Course> listDtoToEntity(List<CourseDto> courseDtos);

    Page<CourseDto> pageEntityToDto(Page<Course> page);
}
