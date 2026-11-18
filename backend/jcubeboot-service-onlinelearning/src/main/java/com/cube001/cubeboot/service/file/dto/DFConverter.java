package com.cube001.cubeboot.service.file.dto;

import com.cube001.cubeboot.file.entity.Directory;
import com.cube001.cubeboot.file.entity.FileResource;
import jdk.nashorn.internal.ir.LiteralNode;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [ 文件名 ]
 * 类的详细说明 文件夹与资源列表合并dto
 *
 * @author 马勇超
 * @version 1.0
 * @date 2025/3/16$ 16:48$
 */
@Mapper(componentModel = "spring")
public interface DFConverter {
    /** 文件夹列表转换 */
    List<DirectoryWithFileResourceDto> directorty2Dto(List<DirectoryDto> directories);
    /** 资源文件转换 */
    List<DirectoryWithFileResourceDto> file2Dto(List<FileResource> fileResources);

}
