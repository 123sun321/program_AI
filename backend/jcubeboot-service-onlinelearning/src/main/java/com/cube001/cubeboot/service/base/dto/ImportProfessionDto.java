package com.cube001.cubeboot.service.base.dto;

import lombok.Data;

/**
* Copyright (C), 2025 - 2026, zxy. 
* FileName: ImportProfessionDto.java 
* 专业dto ：用于专业的Excel表解析时的验证
* 
* @author 马勇超
* @date 2025/12/4 14:42
* @version 1.0 
*/
@Data
public class ImportProfessionDto extends ProfessionDto {
    private String tips;
    private Boolean success;
}
