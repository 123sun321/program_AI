package com.cube001.cubeboot.service.training.mapper;

import com.cube001.cubeboot.training.entity.TrainingPath;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.* FileName: TrainingPathMapper.java
* TrainingPathMapper类的详细说明
*
* @author anonymous
* @date 2025/3/14 17:29
* @version 1.0
*/
public interface TrainingPathMapper extends BaseMapper<TrainingPath> {
    /**
     * @description 2.1 得到某个学生对应的根目录下的所有文件夹
     * @author anonymous
     * @date 2025/3/18 10:35
     * @param studentId 学生id
     * @return 
     */
    List<TrainingPath> getListByHome(Long studentId);
    /**
     * @description 2.2根据dbId 得到某个学生对应的文件夹下 的所有练习目录
     * @author anonymous
     * @date 2025/3/18 10:02
     * @param studentId 学生id
     * @param dbId 文件夹id
     * @return 
     */
    List<TrainingPath> getFolderListById(@Param("studentId") Long studentId,@Param("dbId") Long dbId);
    /**
     * @description 3.在某个文件夹下添加一个新的文件夹
     * @author anonymous
     * @date 2025/3/17 17:09
     * @param dbId 文件夹id
     * @param folderName 文件夹名称
     * @param studentId 学生id
     * @return void
     */
    void addTrainingPath(@Param("folderName") String folderName,
                         @Param("dbId") Long dbId,@Param("studentId") Long studentId);
}
