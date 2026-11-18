package com.cube001.cubeboot.service.course;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.cube001.cubeboot.base.entity.Label;
import com.cube001.cubeboot.course.entity.Exercise;
import com.cube001.cubeboot.service.course.dto.ExerciseDto;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: ExerciseService
 * 实验练习Service
 *
 * @author Juci
 * @date 2025/03/11 15:05
 * @version 1.0
 */
public interface ExerciseService extends IService<Exercise> {
    /**
     * 创建练习
     * @author Juci
     * @param exerciseDto
     * @return
     */
    void insert(ExerciseDto exerciseDto);

    /**
     * 更新练习
     * @author Juci
     * @param exerciseDto
     */
    void update(ExerciseDto exerciseDto);

    /**
     * 获取实验信息
     * @author Juci
     * @param exerciseId
     * @return
     */
    ExerciseDto get(Long exerciseId);

    /**
     * 开始实验
     * @param exerciseJoinId
     * @param userId
     * @param exerciseId
     * @return
     */
//    ExerciseDto start(Long exerciseJoinId ,Long userId,Long exerciseId);

    /**
     * 获取实验信息
     * @param userId
     * @param exerciseId
     * @param type
     * @return
     */
    ExerciseDto getExerciseInfo(Long userId,Long exerciseId,Integer type);
    /**
     * 批量删除
     * @author Juci
     * @param ids
     */
    void delete(List<Long> ids);

    /**
     * 分页获取实验列表
     * @author Juci
     * @param page
     * @param wrapper
     * @return
     */
    Page<ExerciseDto> searchPage(Page<ExerciseDto> page,Wrapper<ExerciseDto> wrapper);

    /**
     * @description  获取实验列表
     * @author 马勇超
     * @date 2025/4/9 15:40
     * @param   wrapper
     * @return   list
     */
    Page<ExerciseDto> selectWithCourse(Page page,Wrapper<Exercise> wrapper);
}
