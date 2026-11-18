package com.cube001.cubeboot.service.course.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.course.entity.ExerciseJoinReport;
import com.cube001.cubeboot.service.course.dto.ExerciseJoinReportDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Copyright (C), 2025 - 2026, zxy.
 * FileName: ExerciseJoinReportMapper
 * 实验参与报告表
 *
 * @author anonymous
 * @date 2025/3/28 18:05
 * @version 1.0
 */
public interface ExerciseJoinReportMapper extends BaseMapper<ExerciseJoinReport> {
    /**
     * 分页获取实验报告
     * @author anonymous
     * @param page
     * @param ew
     * @return
     */
    List<ExerciseJoinReportDto> searchPage(Page<ExerciseJoinReportDto> page, @Param("ew") Wrapper<ExerciseJoinReportDto> ew);

    /**
     * 通过条件查询第一条
     * @param ew
     * @return
     */
    ExerciseJoinReportDto searchPage(@Param("ew") Wrapper<ExerciseJoinReportDto> ew);

    /**
     * 更新浏览量
     * @author anonymous
     * @param id
     */
    void updateClick(@Param("id") Long id);

    /**
     * 更新评价量
     * @param id
     */
    Integer updateComment(@Param("id") Long id,@Param("count") Integer count);

    /**
     * 通过实验编号获取当前用户报告
     * @author anonymous
     * @param exerciseId
     * @param studentId
     * @return
     */
    ExerciseJoinReport findByExerciseId(@Param("exerciseId") Long exerciseId,@Param("studentId") Long studentId);
}
