package com.cube001.cubeboot.service.base.dto;

import lombok.Data;

/**
* Copyright (C), 2025 - 2026, zxy.
* FileName: ImportTeacherDto.java
* 教师Dto：用以教师信息Excel表解析时的验证
*
* @author anonymous
* @date 2025/12/4 14:54
* @version 1.0
*/
@Data
public class ImportTeacherDto extends TeachersDto {
    private String tips;
    private Boolean success;
}
