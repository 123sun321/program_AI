package com.cube001.cubeboot.service.course.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cube001.cubeboot.course.entity.ExerciseJoinRecord;
import com.cube001.cubeboot.service.course.dto.ExerciseJoinRecord.ExerciseRecordStatisticsDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: ExerciseMapper
 * 实验练习参与记录
 *
 * @author Juci
 * @date 2025/04/08 13:29
 * @version 1.0
 */
public interface ExerciseJoinRecordMapper extends BaseMapper<ExerciseJoinRecord> {

    /**
     * 获取参与实验总时长
     * @param studentId
     * @return
     */
    Integer sumMinute(@Param("studentId") Long studentId);

    /**
     * 总学习天数
     * @param studentId 学生编号
     * @return
     */
    Integer sumDay(@Param("studentId") Long studentId);

    /**
     * 学习记录统计
     * @param studentId 学生编号
     * @return
     */
    List<ExerciseRecordStatisticsDto> exerciseRecordStatistics(@Param("studentId") Long studentId);

    /**
     * 获取当前连续签到天数
     * @author Juci
     * @param studentId
     * @return
     */
    Integer getContinuousDay(@Param("studentId") Long studentId);
    /**
     * 获取最大连续签到天数
     * @author Juci
     * @param studentId
     * @return
     */
    Integer maxDay(@Param("studentId") Long studentId);
}
