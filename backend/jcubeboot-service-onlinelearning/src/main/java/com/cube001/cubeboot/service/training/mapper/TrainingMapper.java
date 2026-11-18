package com.cube001.cubeboot.service.training.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.training.entity.Training;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
* Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
* FileName: TrainingMapper.java
* 练习表mapper类的详细说明
*
* @author anonymous
* @date 2025/3/15 18:25
* @version 1.0
*/
public interface TrainingMapper extends BaseMapper<Training> {
  /**
   * @description 功能#792: 实现分页查询所有人的练习表
   * @author anonymous
   * @date 2025/3/27 14:18
   * @param  plusPage 分页
   * @param  wrapper 条件构造器
   * @param  userId 当前用户id
   * @return 所有人的分页详情
   */
    List<Training> pageAllSelf(Page<Training> plusPage,@Param("ew") Wrapper<Training> wrapper,@Param("userId")Long userId);
    /**
     * @description 功能2:实现个人练习表的分页
     * @author anonymous
     * @date 2025/3/27 14:18
     * @param plusPage 初始的分页结果
     * @param wrapper 条件构造器
     * @param studentId 学生id
     * @return 练习表的分页
     */
    List<Training> pageOneself(Page<Training> plusPage,@Param("ew") Wrapper<Training> wrapper,
                               @Param("studentId")Long studentId);

    /**
     * @description showAll
     * @author anonymous
     * @date 2025/3/15 18:23
     * @param stuID: 学生id
     * @return 练习列表
     */
    List<Training> showAll(Long stuID);
    /**
     * @description 按文件夹dbId查询内部的所有练习表
     * @author anonymous
     * @date 2025/3/28 16:15
     * @param studentId 学生ID
     * @param dbId 文件夹的id
     * @return 练习列表
     */
    List<Training> getFolderListById(@Param("studentId") Long studentId,@Param("dbId") Long dbId);
  /**
   * @description 根据dbId 得到某个学生对应的文件夹下 的所有练习 以及 练习目录
   * @author anonymous
   * @date 2025/3/18 10:16
   * @param studentId 学生ID
   * @return 练习列表
   */
    List<Training> getListByHome(@Param("studentId") Long studentId);

  /**
    * @description
    * @author anonymous
    * @date 2025/4/12 17:03
    * @Param Long id练习ID
    * @return Training 联系内容和练习代码
    */
  Training  getTrainingAndCode(Long id);
}
