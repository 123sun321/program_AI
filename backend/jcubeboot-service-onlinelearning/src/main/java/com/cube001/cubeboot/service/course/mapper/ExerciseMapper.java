package com.cube001.cubeboot.service.course.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.course.entity.Exercise;
import com.cube001.cubeboot.service.course.dto.ExerciseDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: ExerciseMapper
 * 实验练习mapper
 *
 * @author anonymous
 * @date 2025/03/11 15:04
 * @version 1.0
 */
public interface ExerciseMapper extends BaseMapper<Exercise> {
    /**
     * 分页获取实验列表
     * @author anonymous
     * @param page
     * @param wrapper
     * @return
     */
    List<ExerciseDto> searchPage(Page<ExerciseDto> page, @Param("ew") Wrapper<ExerciseDto> wrapper);

    /**
     * @description  获取实验列表
     * @author anonymous
     * @date 2025/4/9 15:40
     * @param   wrapper
     * @return   list
     */
    List<ExerciseDto> selectWithCourse(Page<Exercise> page,@Param("ew") Wrapper<Exercise> wrapper);
}
