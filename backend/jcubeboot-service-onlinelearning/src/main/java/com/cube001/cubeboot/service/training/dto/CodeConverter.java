package com.cube001.cubeboot.service.training.dto;

import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.training.entity.Code;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Copyright (C),2018-2019, ZhengZhouChuangZhi. Co.,Ltd.
 * FileName: CodeConverter
 * CodeConverter双向转换类接口
 *
 * @author anonymous
 * @date 2025/03/11 16:11
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface CodeConverter {
    Code dtoToEntity(CodeDto codeDto);
    Code editDtoToEntity(CodeDto codeDto);
    CodeDto entityToDto(Code code);
    List<CodeDto> listEntityToDto(List<Code> codes);
    Page<CodeDto> pageEntityToDto(Page<Code> page);
}
