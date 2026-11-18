package com.cube001.cubeboot.service.training.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.service.base.StudentService;
import com.cube001.cubeboot.service.file.dto.DirectoryDto;
import com.cube001.cubeboot.service.training.TrainingService;
import com.cube001.cubeboot.service.training.dto.TrainingPathConverter;
import com.cube001.cubeboot.service.training.dto.TrainingPathDto;
import com.cube001.cubeboot.training.entity.Training;
import com.cube001.cubeboot.training.entity.TrainingPath;

import com.cube001.cubeboot.service.training.TrainingPathService;
import com.cube001.cubeboot.service.training.mapper.TrainingPathMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
* Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.* FileName: TrainingPathServiceImpl.java
* 练习目录实现类的详细说明
*
* @author 宋泽山
* @date 2025/3/14 17:31
* @version 1.0
*/
@Service
public class TrainingPathServiceImpl extends ServiceImpl<TrainingPathMapper, TrainingPath> implements TrainingPathService {
    @Autowired
    private TrainingPathService trainingPathService;
    @Autowired
    private TrainingService trainingService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TrainingPathConverter trainingPathConverter;
    /**
     * @description 7: 返回练习目录的文件夹的树形图
     * @author 宋泽山
     * @date 2025/4/5 11:04
     * @return 修改结果,true或者false
     */
    @Override
    public List<TrainingPathDto> getTrainingPathTreeList() {
        Long userId = ShiroKit.getUserId();
        Long studentId = studentService.selectOne(new EntityWrapper<Student>().eq("user_id", userId)).getId();
        List<TrainingPath> trainingPathList = trainingPathService.selectList(new EntityWrapper<TrainingPath>().eq("student_id", studentId).isNotNull("parent_id")
                .eq("is_deleted", 0));
        //处理获得所有的文件夹目录
        List<TrainingPathDto> originPathDtoList = new LinkedList<>(trainingPathConverter.listEntityToDto(trainingPathList));

        //存放树形结构的List数组  --> 最终结果
        List<TrainingPathDto> resultList =  new ArrayList<>(32);

        //先收集一级目录
            Iterator<TrainingPathDto> iterator = originPathDtoList.iterator();
            //计算出父id
            Long levelId = (long)0;
            while(iterator.hasNext()){
                TrainingPathDto next = iterator.next();
                if(next.getParentId().equals(levelId)){
                    //添加进结果List,同时删除原列表中的一项
                    resultList.add(next);
                    iterator.remove();
                }
            }
        //深度优先搜索算法
        for (int i=0;i< resultList.size();i++){
            resultList.set(i,  DFSRecursive(resultList.get(i),originPathDtoList) );
        }
        return resultList;
    }
    /**
     * @description 深度优先搜索算法处理树形文件夹目录
     * @author 宋泽山
     * @date 2025/4/30 19:15
     * @param
     * @return
     */
    public TrainingPathDto DFSRecursive(TrainingPathDto dto,List<TrainingPathDto> originPathDtoList){
        Iterator<TrainingPathDto> iterator = originPathDtoList.iterator();
        //计算出父节点id
        Long parentId = dto.getId();
        while(iterator.hasNext()){
            TrainingPathDto next = iterator.next();
            //找到子节点,添加
            if(next.getParentId().equals(parentId)){
                dto.getChildren().add(next);
                iterator.remove();
            }
        }
        //循环设置子节点
        for (int i=0;i< dto.getChildren().size();i++){
            dto.getChildren().set(i,  DFSRecursive(dto.getChildren().get(i),originPathDtoList) );
        }
        return dto;
    }
    /**
     * @description 批量移动某个文件夹或者文件
     * @author 宋泽山
     * @date 2025/4/30 14:16
     * @param folderIdList 文件或者文件夹的ID列表
     * @param targetId 目的地id
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean moveFoldersAndFiles(List<Long> folderIdList, Long targetId) {
        if(targetId<=0){
            targetId = -targetId;
        }else{
            throw new FriendException("传入的目标文件夹参数值异常,文件夹id应为负值或者不存在!");
        }
        for(Long folderId : folderIdList ){
            //移动文件夹
            if(folderId<0){
                folderId=-folderId;
                TrainingPath trainingPath = trainingPathService.selectById(folderId);
                trainingPath.setParentId(targetId);
                //发生异常,全部回滚
                if(!trainingPathService.update(trainingPath,new EntityWrapper<TrainingPath>().eq("id", folderId) )){
                    throw new FriendException("移动某个文件夹异常");
                }
            }
            //移动文件
            else{
                Training training = trainingService.selectById(folderId);
                training.setTrainingPathId(targetId);
                //发生异常,全部回滚
                if (!trainingService.update(training,new EntityWrapper<Training>().eq("id", folderId) )){
                    throw new FriendException("移动某个文件异常");
                }
            }
        }
        return true;
    }

    /**
     * @description  功能2 根据dbId 得到某个学生对应的文件夹id 下的所有 练习目录
     * @author 宋泽山
     * @date 2025/3/18 9:54
     * @param studentId 学生id
     * @param dbId 文件夹id
     * @return 文件夹列表
     */
    @Override
    public List<TrainingPath> getFolderListById(Long studentId, Long dbId) {
        if(dbId==0){
            return baseMapper.getListByHome(studentId);
        }
        else{
            return baseMapper.getFolderListById(studentId, dbId);
        }
    }
    /**
     * @description 3.在某个文件夹下添加一个新的文件夹
     * @author 宋泽山
     * @date 2025/3/17 17:09
     * @param dbId 文件夹id
     * @param folderName 文件夹名称
     * @param studentId 学生id
     * @return void
     */
    @Override
    public void addTrainingPath( String folderName, Long dbId, Long studentId) {
        baseMapper.addTrainingPath( folderName, dbId, studentId);
    }
}
