package com.cube001.cubeboot.service.base.dto;

import lombok.Data;

import javax.validation.Valid;
import java.util.List;

/**
* Copyright (C), 2025 - 2026, zxy.
* FileName: ListTeacherDto.java
* 教师Dto：只为用来批量导入时验证List<TeachersDto>
 *
* @author anonymous
* @date 2025/11/28 10:39
* @version 1.0
*/
@Data
public class ListTeacherDto {
    @Valid
    private List<TeachersDto> data;
}
