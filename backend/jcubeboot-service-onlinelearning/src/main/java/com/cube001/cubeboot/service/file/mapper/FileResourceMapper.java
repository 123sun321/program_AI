package com.cube001.cubeboot.service.file.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.file.entity.FileResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Copyright (C), 2025 - 2026, zxy.
* FileName: FileResourceMapper.java
* 类的详细说明
*
* @author anonymous
* @date 2025/3/14 9:49
* @version 1.0
*/
public interface FileResourceMapper extends BaseMapper<FileResource> {

    /**
    * @description  资源分页
    * @author anonymous
    * @date 2025/3/20 10:34
    * @param
    * @return
    */
    List<FileResource> searchPage(Page<FileResource> page,@Param("ew") Wrapper<FileResource> wrapper);

}
