package com.cube001.cubeboot.service.training;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.course.entity.ExerciseJoin;
import com.cube001.cubeboot.service.assignment.dto.RecommendWorkDto;
import com.cube001.cubeboot.service.training.dto.CodeDto;
import com.cube001.cubeboot.service.training.dto.TrainingDto;
import com.cube001.cubeboot.training.entity.Training;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.ParseException;
import java.util.List;

/**
* Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
* FileName: TrainingService.java
* TrainingService类的详细说明
*
* @author anonymous
* @date 2025/3/15 18:04
* @version 1.0
*/
public interface TrainingService extends IService<Training> {
    /**
     * @param searchDto 分页查询
     * @return 分页查询对应的实体wrapper
     * @throws ParseException 处理日期转换发生的异常
     * @description 根据模糊查询的条件设置wrapper
     * @author anonymous
     * @date 2025/3/29 19:48
     */
    EntityWrapper<Training> setWrapper(CommonSearchDto<Training> searchDto) throws ParseException;

    /**
     * @param page    分页
     * @param wrapper 条件构造器
     * @param userId  当前用户id
     * @return 所有人的分页详情
     * @description 功能#792: 实现分页查询所有人的练习表
     * @author anonymous
     * @date 2025/3/27 14:18
     */
    Page<Training> pageAllSelf(Page<Training> page, Wrapper<Training> wrapper, Long userId);

    /**
     * @param plusPage  初始的分页结果
     * @param wrapper   条件构造器
     * @param studentId 学生id
     * @return 单人分页详情
     * @description 功能2: 实现个人练习表的分页
     * @author anonymous
     * @date 2025/3/27 14:18
     */
    Page<Training> pageOneself(Page<Training> plusPage, Wrapper<Training> wrapper, Long studentId);

    /**
     * @param studentId: 学生id
     * @return 练习列表
     * @description 全部显示作业功能
     * @author anonymous
     * @date 2025/3/16 14:19
     */
    List<Training> showAll(Long studentId);

    /**
     * @param studentId 学生ID
     * @param dbId      文件夹的id
     * @return 练习列表
     * @description 根据文件夹的id 得到对应的文件夹下 的所有练习 以及 练习目录
     * @author anonymous
     * @date 2025/3/18 10:16
     */

    List<Training> getFolderListById(Long studentId, Long dbId);


    /**
     * @description
     * @author anonymous
     * @date 2025/4/12 17:03
     * @Param Long id练习ID
     * @return Training 联系内容和练习代码
     */
    Training getTrainingAndCodeById(Long id);

    /**
     * 创建实验练习
     * @author anonymous
     * @param exerciseJoin 参与实验
     * @param codeDto 代码
     * @param type 类型 0-创建新练习 1-修改已有练习
     */
    void createExerciseTraing(ExerciseJoin exerciseJoin, CodeDto codeDto, Integer type);
    /**
      * @description
      * @author anonymous
      * @date 2025/4/29 15:57
      * @Param
      * @return
      */
    List<Training> getTrainingList();

    /**
     * 将优秀作品Fork成自己的作品
     * @author anonymous
     * @date 2025/5/10
     * @param recommendWorkDto
     * @param userId
     */
    Long fork(RecommendWorkDto recommendWorkDto,Long userId);

    /**
     * 保存代码
     * @author anonymous
     * @date 2025/5/13
     * @param trainingDto
     */
    void save(TrainingDto trainingDto);

    /**
     * 保存实验代码
     * @author anonymous
     * @param userId
     * @param trainingDto
     */
    void saveTraining(Long userId,TrainingDto trainingDto);
}