package com.cube001.cubeboot.service.file.dto;

import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.file.entity.FileResource;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [ 文件名 ]
 * 类的详细说明 资源列表转换类
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/3/14$ 9:56$
 */
@Mapper(componentModel = "spring")
public interface FileResourceConverter {

    FileResource dtoToEntity(FileResourceDto resourceDto);
    List<FileResource> listDtoToEntity(List<FileResourceDto> resourceDtos);

    FileResourceDto entityToDto(FileResource resource);
    List<FileResourceDto> listEntityToDto(List<FileResource> resources);

    Page<FileResourceDto> EntityToPageDto(Page<FileResource> resource);

}
