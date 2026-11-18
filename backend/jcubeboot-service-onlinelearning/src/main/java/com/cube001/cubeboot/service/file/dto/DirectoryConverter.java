package com.cube001.cubeboot.service.file.dto;

import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.file.entity.Directory;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [ 文件名 ]
 * 类的详细说明 文件夹转换类
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/3/14$ 15:25$
 */
@Mapper(componentModel = "spring")
public interface DirectoryConverter {

    Directory dtoToEntity(DirectoryDto directoryDto);
    DirectoryDto entity2Dto(Directory directory);
    List<Directory> listDtoToEntity(List<DirectoryDto> directoryDtos);
    List<DirectoryDto> lisyEntity2Dto(List<Directory> directories);

    Page<DirectoryDto> entity2PageDto(Page<Directory> page);

}
