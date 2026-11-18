package com.cube001.cubeboot.service.course;

import com.baomidou.mybatisplus.service.IService;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.course.entity.ExerciseJoinRecord;
import com.cube001.cubeboot.service.course.dto.ExerciseJoinRecord.ExerciseJoinRecordDto;
import com.cube001.cubeboot.service.course.dto.ExerciseJoinRecord.ExerciseRecordStatisticsDto;
import com.cube001.cubeboot.service.course.dto.ExerciseJoinRecord.ExerciseStatisticNumberDto;

import java.util.List;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: ExerciseJoinRecordService
 * 实验练习参与记录Service
 *
 * @author anonymous
 * @date 2025/04/08 13:33
 * @version 1.0
 */
public interface ExerciseJoinRecordService extends IService<ExerciseJoinRecord> {
    /**
     * 创建实验参与记录
     * @author anonymous
     * @param exerciseJoinRecordDto
     * @param userId
     */
    void insert(ExerciseJoinRecordDto exerciseJoinRecordDto,Long userId);

    /**
     * 学生参与练习次数相关统计
     * @author anonymous
     * @param userId 用户编号
     * @return
     */
    ExerciseStatisticNumberDto statisticNumber(Long userId);

    /**
     * 学习记录统计
     * @author anonymous
     * @param userId 用户编号
     * @return
     */
    List<ExerciseRecordStatisticsDto> exerciseRecordStatistics(Long userId);

    /**
     * 更新学生学习天数
     * @author anonymous
     * @param student
     */
    void updateContinuousDay(Student student);
}
