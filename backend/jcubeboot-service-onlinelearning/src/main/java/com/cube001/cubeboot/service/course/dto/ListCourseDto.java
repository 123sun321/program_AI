package com.cube001.cubeboot.service.course.dto;

import lombok.Data;

import javax.validation.Valid;
import java.util.List;

/**
 * CourseDto List
 * 此类无其他作用 只为用来验证List<CourseDto>
 * @author anonymous
 * @since 2018-11-26
 */
@Data
public class ListCourseDto {
    @Valid
    private List<CourseDto> data;
}
