package com.cube001.cubeboot.service.file;

import com.baomidou.mybatisplus.service.IService;
import com.cube001.cubeboot.file.entity.Directory;
import com.cube001.cubeboot.service.file.dto.DirectoryDto;
import com.cube001.cubeboot.service.file.dto.DirectoryWithFileResourceDto;

import java.util.List;

/**
* Copyright (C), 2025 - 2026, zxy.
* FileName: DirectoryService.java
* 类的详细说明 文件夹service
*
* @author anonymous
* @date 2025/3/14 15:23
* @version 1.0
*/
public interface DirectoryService extends IService<Directory> {

    /**
    * @description  展示文件夹及资源
    * @author anonymous
    * @date 2025/3/21 12:49
    * @param   parentId
    * @return   list
    */
    List<DirectoryWithFileResourceDto> listWithFileResource(Long parentId);

    /**
    * @description  树形列表
    * @author anonymous
    * @date 2025/3/21 12:50
    * @param   directories
    * @return   List<DirectoryDto>
    */
    List<DirectoryDto> treeListWithFile(List<Directory> directories);
}
