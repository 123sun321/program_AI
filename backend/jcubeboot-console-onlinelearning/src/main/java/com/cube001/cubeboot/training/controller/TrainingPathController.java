package com.cube001.cubeboot.training.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.service.base.StudentService;
import com.cube001.cubeboot.service.file.dto.DirectoryDto;
import com.cube001.cubeboot.service.file.dto.RemoveFile;
import com.cube001.cubeboot.service.training.dto.TrainingPathDto;
import com.cube001.cubeboot.training.entity.Training;
import com.cube001.cubeboot.training.entity.TrainingPath;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.service.training.TrainingPathService;
import com.cube001.cubeboot.service.training.TrainingService;
import com.cube001.cubeboot.service.training.dto.TrainingConverter;
import com.cube001.cubeboot.service.training.dto.TrainingPathConverter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
* Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.* FileName: TrainingPathController.java
* 练习目录的controller,全部功能
* @author anonymous
* @date 2025/3/14 17:27
* @version 1.0
*/
@Api(value = "training-path controller", description = "练习目录controller")
@RestController
@RequestMapping(value = "${cubeboot.app-manager-path}/training-path")
//@RequiresPermissions("trainingPath")
public class TrainingPathController implements IApplicationService {

    @Autowired
    private TrainingPathService trainingPathService;
    @Autowired
    private TrainingPathConverter trainingPathConverter;

    @Autowired
    private TrainingService trainingService;
    @Autowired
    private TrainingConverter trainingConverter;
    @Autowired
    private StudentService studentService;

    /**
     * @description 1 得到某个学生对应的文件夹下的所有练习以及文件夹
     * @author anonymous
     * @date 2025/3/18 13:13
     * @param id 文件夹id
     * @return 文件+文件夹列表
     */
    @ApiOperation(value = "功能1: 根据目录id 得到某个学生对应的文件夹下的所有练习以及文件夹")
    @GetMapping(value = "/getFolderList")
//    @RequiresPermissions("trainingPath:getFolderList")
    public List getFolderListById(@RequestParam("dataId")Long id) {

        /**后台获取当前用户的userId */
        Long userId = ShiroKit.getUserId();
        Long studentId = studentService.selectOne(new EntityWrapper<Student>().eq("user_id", userId)).getId();
        /**将id变会正值*/
        if(id<0){
            id=-id;
        }

        List<Object>list =new ArrayList<>(10);
        list.add(trainingPathConverter.listEntityToDto(trainingPathService.getFolderListById(studentId, id)));
        list.add(trainingConverter.listEntityToDto(trainingService.getFolderListById(studentId, id)));
        return list;
    }

    /**
     * @description 2 在某个文件夹下添加一个新的文件夹
     * @author anonymous
     * @date 2025/3/18 13:14
     * @param parentId 文件夹id
     * @param folderName 文件夹名字
     * @return 新建成功返回true,否则抛出异常或者false
     */
    @ApiOperation(value = "功能3: 在某个文件夹下添加一个新的文件夹")
    @PostMapping(value = "/addFolder")
//    @RequiresPermissions("trainingPath:addFolder")
    public Boolean addTrainingPath(@RequestParam("dataId")Long parentId,@RequestParam("folderName")String folderName){

        /**将id变会正值*/
        if(parentId<0){
            parentId=-parentId;
        }
        /**手动校验两个数据是否合法*/
        if(folderName.indexOf(' ')!=-1 || folderName.indexOf('\t')!=-1 || (folderName.trim().length()==0)){
            throw new FriendException("目录名称不能含有空白字符");
        }
        /**添加校验，取出空格*/
        folderName = folderName.trim();
        int maxLength=20;
        if(folderName.length()<1||folderName.length()>maxLength){
            throw new FriendException("目录名称限制在1-20个字符,请重试");
        }

        /**后台获取当前用户的userId */
        Long userId = ShiroKit.getUserId();
        Long studentId = studentService.selectOne(new EntityWrapper<Student>().eq("user_id", userId)).getId();

        /** 查询是否会有重名的情况,是的话抛出异常*/
        int cnt=trainingPathService.selectCount(new EntityWrapper<TrainingPath>().eq("title", folderName).
                eq("student_id", studentId).eq("parent_id", parentId));
        if(cnt>=1) {
            throw new FriendException("与已有的文件夹重名,请重新添加");
        }
        /**否则没有重名,则进行插入操作*/
        TrainingPath path=new TrainingPath();
        path.setParentId(parentId);
        path.setStudentId(studentId);
        path.setTitle(folderName);
        path.setSystem(false);
        return trainingPathService.insert(path);
    }
    /**
     * @description 3: 在某个文件夹下删除一个或者多个文件夹
     * @author anonymous
     * @date 2025/4/5 11:04
     * @param dataList id列表
     * @return 删除结果,true或者false
     */
    @ApiOperation(value = "功能4: 在某个文件夹下删除一个或者多个文件夹")
    @PostMapping(value = "/delFolder")
//    @RequiresPermissions("trainingPath:del")
    public Boolean delFolders(@RequestBody List<Long> dataList) {

        //在前端传值的时候,如果是文件夹id乘于-1 ,是文件的话id不变;
        for (Long x: dataList){
            //处理是文件夹的情况
            if(x<0){
                x=-x;
                /**后台获取当前用户的userId , 判断是否可以直接删除,如果内部存在子文件则不可删除*/
                Long userId = ShiroKit.getUserId();
                Long studentId = studentService.selectOne(new EntityWrapper<Student>().eq("user_id", userId)).getId();
                List<TrainingPath> trainingPathList = trainingPathService.getFolderListById(studentId, x);
                if(trainingPathList !=null&&trainingPathList.size()!=0){
                    throw new FriendException("当前文件夹内存在子文件夹或其他内容,删除失败");
                }
                List<Training> trainingList = trainingService.getFolderListById(studentId, x);
                if(trainingList !=null && trainingList.size()!=0){
                    throw new FriendException("当前文件夹内存在子文件或其他内容,删除失败");
                }

                boolean result=trainingPathService.deleteById(x);
                if(!result) {
                    throw new FriendException("部分文件夹删除异常,已终止后续删除");
                }
            }
            //处理是文件的情况!!
            else{
                boolean result=trainingService.deleteById(x);
                if(!result) {
                    throw new FriendException("部分练习记录删除异常,已终止后续删除");
                }
            }
        }

        return true;
    }
    /**
     * @description 5: 重命名某个文件夹
     * @author anonymous
     * @date 2025/4/5 11:04
     * @param folderId 文件id
     * @param folderName 文件夹名称
     * @return 修改结果,true或者false
     */
    @ApiOperation(value = "5: 重命名某个文件夹或者文件")
    @PostMapping(value = "/editFolderName")
//    @RequiresPermissions("trainingPath:editFolderName")
    public Boolean editFolderName(@RequestParam("folderId")Long folderId,@RequestParam("folderName")String folderName) {
        /**手动校验两个数据是否合法*/
        if(folderName.indexOf(' ')!=-1 || folderName.indexOf('\t')!=-1 || (folderName.trim().length()==0)){
            throw new FriendException("目录名称不能含有空白字符");
        }
        int maxLength=20;
        if(folderName.length()<1||folderName.length()>maxLength){
            throw new FriendException("目录名称限制在1-20个字符,请重试");
        }
        if(folderId<0){
            folderId=-folderId;
            TrainingPath trainingPath = trainingPathService.selectById(folderId);
            trainingPath.setTitle(folderName);
            return trainingPathService.update(trainingPath,new EntityWrapper<TrainingPath>().eq("id", folderId) );
        }
        else{
            Training training = trainingService.selectById(folderId);
            training.setTitle(folderName);
            return trainingService.update(training,new EntityWrapper<Training>().eq("id", folderId) );
        }
    }
    /**
     * @description 6: 批量移动一批文件夹或者文件
     * @author anonymous
     * @date 2025/4/29 11:04
     * @return 修改结果,true或者false
     */
    @ApiOperation(value = "6: 批量移动一批文件夹或者文件")
    @PostMapping(value = "/moveFoldersAndFiles")
//    @RequiresPermissions("trainingPath:moveFolder")
    public Boolean moveFoldersAndFiles(@RequestBody RemoveFile removeFile) {
        return trainingPathService.moveFoldersAndFiles(removeFile.getChildrenIds(), removeFile.getParentId());
    }
    /**
     * @description 7: 返回练习目录的文件夹的树形图
     * @author anonymous
     * @date 2025/4/30 11:04
     * @return List<TrainingPathDto>
     */
    @ApiOperation(value = "7: 返回练习目录的文件夹的树形图")
    @PostMapping(value = "/getTrainingPathTreeSet")
//    @RequiresPermissions("trainingPath:getTrainingPathTreeSet")
    public List<TrainingPathDto> getTrainingPathTreeSet() {

        return trainingPathService.getTrainingPathTreeList();
    }

}