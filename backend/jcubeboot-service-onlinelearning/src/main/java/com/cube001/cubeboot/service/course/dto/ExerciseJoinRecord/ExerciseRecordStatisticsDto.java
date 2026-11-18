package com.cube001.cubeboot.service.course.dto.ExerciseJoinRecord;

import lombok.Data;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: ExerciseRecordStatisticsDto
 * 学习记录统计
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/4/8 17:37
 */
@Data
public class ExerciseRecordStatisticsDto {

    /**
     * 参与学习日期
     */
    private String time;
    /**
     * 学习分钟数
     */
    private Integer studyTime;
    /**
     * 学习次数
     */
    private Integer studyCount;
}
