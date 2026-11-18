package com.cube001.cubeboot.service.file.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.file.entity.Directory;
import com.cube001.cubeboot.file.entity.FileResource;
import com.cube001.cubeboot.service.file.DirectoryService;
import com.cube001.cubeboot.service.file.FileResourceService;
import com.cube001.cubeboot.service.file.dto.*;
import com.cube001.cubeboot.service.file.mapper.DirectoryMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* Copyright (C), 2025 - 2026, zxy.
* FileName: DirectoryServiceImpl.java
* 类的详细说明
*
* @author 马勇超
* @date 2025/3/14 15:23
* @version 1.0
*/
@Service
public class DirectoryServiceImpl extends ServiceImpl<DirectoryMapper, Directory> implements DirectoryService {

    @Autowired
    private FileResourceService resourceService;

    @Autowired
    private DFConverter converter;

    @Autowired
    private DirectoryConverter directoryConverter;

    @Autowired
    private DirectoryService service;

    /**
    * @description  查询资源列表
    * @author 马勇超
    * @date 2025/3/16 16:10
    * @param
    * @return
    */
    @Override
    public List<DirectoryWithFileResourceDto> listWithFileResource(Long parentId) {

        // 获取当前用户id
        Long userId = ShiroKit.getUserId();
        List<DirectoryWithFileResourceDto> directoryWithFileResourceDtos1 = new ArrayList<>();
        List<DirectoryWithFileResourceDto> directoryWithFileResourceDtos2 = new ArrayList<>();

        // 如果是没有父目录，则取根目录的文件
        if(parentId == 0){
            List<Directory> directoryList = baseMapper.selectList(new EntityWrapper<Directory>().isNull("parent_id").eq("user_id",userId));
            List<DirectoryDto> directoryDtoList = directoryConverter.lisyEntity2Dto(directoryList);
            for (DirectoryDto d:directoryDtoList) {
                d.setType("1");
            }
            // 获取资源根目录内容
            List<FileResource> resourceList = resourceService.selectList(new EntityWrapper<FileResource>().isNull("directory_id").eq("user_id",userId));
            directoryWithFileResourceDtos1 = converter.directorty2Dto(directoryDtoList);
            directoryWithFileResourceDtos2 = converter.file2Dto(resourceList);
            for (DirectoryWithFileResourceDto f:directoryWithFileResourceDtos2) {
                directoryWithFileResourceDtos1.add(f);
            }
        }
        // 如果父目录id不为空，则取自该目录的文件
        else{
            List<Directory> directoryList = baseMapper.selectList(new EntityWrapper<Directory>().eq("parent_id",parentId).eq("user_id",userId));
            List<DirectoryDto> directoryDtoList = directoryConverter.lisyEntity2Dto(directoryList);
            for (DirectoryDto d:directoryDtoList) {
                d.setType("1");
            }
            List<FileResource> resourceList = resourceService.selectList(new EntityWrapper<FileResource>().eq("directory_id",parentId).eq("user_id",userId));
            directoryWithFileResourceDtos1 = converter.directorty2Dto(directoryDtoList);
            directoryWithFileResourceDtos2 = converter.file2Dto(resourceList);
            for (DirectoryWithFileResourceDto f:directoryWithFileResourceDtos2) {
                directoryWithFileResourceDtos1.add(f);
            }
        }
        return directoryWithFileResourceDtos1;
    }


    /**
    * @description  查询文件夹树形列表
    * @author 马勇超
    * @date 2025/3/20 13:09
    * @param
    * @return
    */
    @Override
    public List<DirectoryDto> treeListWithFile(List<Directory> directories) {
        List<DirectoryDto> directoryDtoList = directoryConverter.lisyEntity2Dto(directories);
        List<DirectoryDto> directoryDtos = new ArrayList<>();
        for (DirectoryDto dto:directoryDtoList) {
            DirectoryDto dto1 = new DirectoryDto();
            dto1 = treeList(dto.getId());
            directoryDtos.add(dto1);
        }
        return directoryDtos;
    }

    /**
    * @description  递归算法
    * @author 马勇超
    * @date 2025/3/21 12:51
    * @param   pId
    * @return  DirectoryDto
    */
    public DirectoryDto treeList(Long pId){
        Long userId = ShiroKit.getUserId();
        Directory directory = service.selectById(pId);
        DirectoryDto directoryDto = directoryConverter.entity2Dto(directory);
        directoryDto.setType("1");
        List<Directory> directoryList = service.selectList(new EntityWrapper<Directory>().eq("parent_id",pId).eq("user_id",userId));
        List<DirectoryDto> directoryDtoList = directoryConverter.lisyEntity2Dto(directoryList);

        for (DirectoryDto d:directoryDtoList) {
            DirectoryDto dto = treeList(d.getId());
            directoryDto.getChildren().add(dto);
        }
        return directoryDto;
    }

}
