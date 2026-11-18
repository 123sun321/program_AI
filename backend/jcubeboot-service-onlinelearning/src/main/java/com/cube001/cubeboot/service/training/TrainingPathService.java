package com.cube001.cubeboot.service.training;

import com.cube001.cubeboot.service.file.dto.DirectoryDto;
import com.cube001.cubeboot.service.training.dto.TrainingPathDto;
import com.cube001.cubeboot.training.entity.TrainingPath;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
* Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.* FileName: TrainingPathService.java
* 练习目录Service类的详细说明
*
* @author anonymous
* @date 2025/3/14 17:30
* @version 1.0
*/
public interface TrainingPathService extends IService<TrainingPath> {
    /**
     * @description 7: 返回练习目录的文件夹的树形图
     * @author anonymous
     * @date 2025/4/5 11:04
     * @return 修改结果,true或者false
     */
    List<TrainingPathDto> getTrainingPathTreeList();
    /**
     * @description 批量移动某个文件夹或者文件
     * @author anonymous
     * @date 2025/4/30 14:16
     * @param folderIdList 文件或者文件夹的ID列表
     * @param targetId 目的地id
     * @return
     */
    Boolean moveFoldersAndFiles(List<Long> folderIdList,Long targetId);
    /**
     * @description  功能2 根据dbId 得到某个学生对应的文件夹id 下的所有 练习目录
     * @author anonymous
     * @date 2025/3/18 9:54
     * @param studentId 学生id
     * @param dbId 文件夹id
     * @return 文件夹列表
     */
    List<TrainingPath> getFolderListById(Long studentId,Long dbId);

    /**
     * @description 3.在某个文件夹下添加一个新的文件夹
     * @author anonymous
     * @date 2025/3/17 17:09
     * @param dbId 文件夹id
     * @param folderName 文件夹名称
     * @param studentId 学生id
     * @return void
     */
    void addTrainingPath(String folderName,Long dbId,Long studentId);


}
