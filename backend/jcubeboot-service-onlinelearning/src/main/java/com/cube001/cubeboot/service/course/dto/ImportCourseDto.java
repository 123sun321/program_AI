package com.cube001.cubeboot.service.course.dto;

import lombok.Data;

@Data
public class ImportCourseDto extends CourseDto {
    private String tips;
    private Boolean success;
}
