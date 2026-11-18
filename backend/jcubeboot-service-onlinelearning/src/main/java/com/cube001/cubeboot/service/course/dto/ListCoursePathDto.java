package com.cube001.cubeboot.service.course.dto;

import lombok.Data;

import javax.validation.Valid;
import java.util.List;

/**
 * Copyright (C),2018-2019, ZhengZhouChuangZhi. Co.,Ltd.
 * FileName: [文件名]
 * 类的详细说明
 *此类无其他作用 只为用来验证List<CoursePathDto>
 * @author anonymous
 * @version 1.0
 * @date 2025/3/14 11:21
 */
@Data
public class ListCoursePathDto {
    @Valid
    private List<CoursePathDto> data;
}
