package com.cube001.cubeboot.service.community.dto;

import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.community.entity.SignRecord;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Copyright (C),2018-2019, ZhengZhouChuangZhi. Co.,Ltd.
 * FileName: [文件名]
 * 类的详细说明
 *
 * @author HuangPengju
 * @version 1.0
 * @date 2025/3/21 13:49
 */
@Mapper(componentModel = "spring")
public interface SignRecordConverter {
    SignRecord dtoToEntity(SignRecordDto signRecordDto);
    SignRecord editDtoToEntity(SignRecordDto signRecordDto);
    SignRecordDto entityToDto(SignRecord signRecord);
    List<SignRecordDto> listEntityToDto(List<SignRecord> codes);
    Page<SignRecordDto> pageEntityToDto(Page<SignRecordDto> page);
}
