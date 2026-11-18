package com.cube001.cubeboot.file.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.cube001.cubeboot.community.entity.SignRecord;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.file.entity.Directory;
import com.cube001.cubeboot.file.entity.FileResource;
import com.cube001.cubeboot.service.file.DirectoryService;
import com.cube001.cubeboot.service.file.FileResourceService;
import com.cube001.cubeboot.service.file.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
* Copyright (C), 2025 - 2026, zxy.
* FileName: DirectoryController.java
* 类的详细说明 文件夹管理控制类
*
* @author 马勇超
* @date 2025/3/14 15:20
* @version 1.0
*/
@RestController
@RequestMapping("${cubeboot.app-manager-path}/directory")
@Api(value = "directory-controller",description = "文件夹管理控制类")
@RequiresPermissions("directory")
public class DirectoryController implements IApplicationService {

    @Autowired
    private DirectoryConverter converter;

    @Autowired
    private DirectoryService service;

    @Autowired
    private FileResourceService resourceService;

    /**
    * @description  新建文件夹
    * @author 马勇超
    * @date 2025/3/15 15:48
    * @param directoryDto
    * @return Boolean
    */
    @ApiOperation(value = "新建文件夹")
    @PostMapping(value = "add")
    @RequiresPermissions("directory:add")
    public void add(@RequestBody DirectoryDto directoryDto){
        Long userId = ShiroKit.getUserId();
        directoryDto.setUserId(userId);
        if(directoryDto.getParentId() == 0) {
            directoryDto.setParentId(null);
        }
        if(!service.insert(converter.dtoToEntity(directoryDto))){
            throw new FriendException("新建文件夹失败!");
        }
    }

    @ApiOperation(value = "文件夹名存在数量")
    @GetMapping(value = "getCount")
    @RequiresPermissions("directory:get")
    public Integer getCountName(@RequestParam(value = "parentId",required = false) Long parentId,@RequestParam("name")String name){
        List<Directory> directoryList = new ArrayList<>();
        Long userId = ShiroKit.getUserId();
        if(parentId == 0){
            directoryList = service.selectList(new EntityWrapper<Directory>().like("name",name).isNull("parent_id").eq("user_id",userId));
        }else {
            directoryList = service.selectList(new EntityWrapper<Directory>().like("name",name).eq("parent_id",parentId).eq("user_id",userId));
        }
        return directoryList.size();
    }

    /**
    * @description  文件重命名
    * @author 马勇超
    * @date 2025/4/1 17:38
    * @param   id,reName
    */
    @ApiOperation(value = "文件重命名")
    @PostMapping(value = "reName")
    @RequiresPermissions("directory:edit")
    public void editReName(@RequestParam("id")Long id,@RequestParam("reName") String reName){
        Directory directory = service.selectById(id);
        FileResource fileResource = resourceService.selectById(id);
        if(directory != null) {
            directory.setName(reName);
            service.updateById(directory);
        }else if(fileResource != null){
            fileResource.setName(reName);
            resourceService.updateById(fileResource);
        }else {
            throw new FriendException("该文件夹或文件不存在");
        }
    }

    /**
    * @description  删除文件夹及资源
    * @author 马勇超
    * @date 2025/3/20 10:41
    * @param   ids
    * @return   Boolean
    */
    @ApiOperation(value = "删除文件夹及资源")
    @PostMapping(value = "deleteBatches")
    @ResponseBody
    @RequiresPermissions("directory:del")
    public void del(@RequestBody List<Long> ids){

        List<Directory> directoryList = service.selectList(new EntityWrapper<Directory>().in("id",ids));
        List<FileResource> fileResourceList = resourceService.selectList(new EntityWrapper<FileResource>().in("id",ids));
        if(directoryList.size() > 0){
            List<Directory> directories = service.selectList(new EntityWrapper<Directory>().in("parent_id",ids));
            List<FileResource> fileResources = resourceService.selectList(new EntityWrapper<FileResource>().in("directory_id",ids));
            if(fileResources.size() > 0 || directories.size() > 0){
                throw new FriendException("文件夹中存在文件，谨慎删除！！！");
            }else {
                    service.deleteBatchIds(ids);
            }
        }else {
            resourceService.deleteBatchIds(ids);
        }
    }

    /**  
    * @description  获取文件夹列表
    * @author 马勇超
    * @date 2025/3/20 10:39
    * @param   parentId
    * @return   List<DirectoryWithFileResourceDto>
    */
    @ApiOperation(value = "获取文件夹列表")
    @GetMapping(value = "list")
    @RequiresPermissions("directory:list")
    public List<DirectoryWithFileResourceDto> getAll(@RequestParam(value = "parentId",required = false) Long parentId){
        List<DirectoryWithFileResourceDto> resources = service.listWithFileResource(parentId);
        return resources;
    }

    /**
    * @description  获取树形列表
    * @author 马勇超
    * @date 2025/3/19 16:05
    * @return list
    */
    @ApiOperation(value = "获取树形列表")
    @GetMapping(value = "treeList")
    @RequiresPermissions("directory:list")
    public List<DirectoryDto> getList(){
        Long userId = ShiroKit.getUserId();
        List<Directory> directoryList = service.selectList(new EntityWrapper().isNull("parent_id").eq("user_id",userId));
        List<DirectoryDto> directoryDtoList = service.treeListWithFile(directoryList);
        return directoryDtoList;
    }

    /**
    * @description  文件夹的移动
    * @author 马勇超
    * @date 2025/3/20 10:38
    * @param   removeFile
    * @return   Boolean
    */
    @ApiOperation(value = "移动文件夹")
    @PostMapping(value = "removeFile")
    @RequiresPermissions("directory:edit")
    public Boolean removeFile(@RequestBody RemoveFile removeFile){
        Boolean success = true;
        List<Long> ids = removeFile.getChildrenIds();
        List<Directory> directoryList = service.selectList(new EntityWrapper<Directory>().in("id",ids));
        List<FileResource> fileResources = resourceService.selectList(new EntityWrapper<FileResource>().in("id",ids));
        // 如果文件列表不是空
        if(directoryList.size() != 0) {
            for (Directory d : directoryList) {
                d.setParentId(removeFile.getParentId());
                success = service.updateById(d);
            }
        }
        // 如果资源列表不为空
        if(fileResources.size()!=0){
            for(FileResource f:fileResources){
                f.setDirectoryId(removeFile.getParentId());
                success = resourceService.updateById(f);
            }
        }
        return success;
    }

    @ApiOperation(value = "验证数据重复")
    @RequestMapping(value = "validate", method = RequestMethod.GET)
    @ResponseBody
    public Boolean validRepeat(@RequestParam(value = "id",required = false) Long id,@RequestParam(value = "parentId",required = false)Long parentId,@RequestParam("col") String col, @RequestParam("value") String value) {
        Boolean pass = false;
        Wrapper<Directory> wrapper = new EntityWrapper<>();
        wrapper.eq(col,value).eq("user_id",ShiroKit.getUserId());
        if(null != parentId){
            wrapper.eq("parent_id",parentId);;
        }
        int count = service.selectCount(wrapper);
        Directory directory = service.selectOne(wrapper);
        if(count == 0 && directory == null){
            pass = true;
        }
        if(count ==1 && directory.getId().equals(id)){
            pass = true;
        }
        if(directory!=null && !directory.getId().equals(id)){
            pass = false;
        }
        return pass;
    }

}

