package com.cube001.cubeboot.service.base.dto;

import lombok.Data;

import javax.validation.Valid;
import java.util.List;

/**
* Copyright (C), 2025 - 2026, zxy.
* FileName: ListProfessionDto.java
* 专业dto： 只为用来批量导入时验证List<ProfessionDto>
*
* @author 马勇超
* @date 2025/12/4 14:51
* @version 1.0
*/
@Data
public class ListProfessionDto {
    @Valid
    private List<ProfessionDto> data;
}
