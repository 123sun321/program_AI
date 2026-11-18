package com.cube001.cubeboot.service.course.dto.ExerciseJoinRecord;

import lombok.Data;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: ExerciseStatisticNumberDto
 * 实验数据统计
 *
 * @author Juci
 * @version 1.0
 * @date 2025/4/8 14:41
 */
@Data
public class ExerciseStatisticNumberDto {
    /**
     * 累计实验次数
     */
    private Integer ExerciseCount;
    /**
     * 累计实验分钟数
     */
    private Integer ExerciseTime;
    /**
     * 连续学习天数
     */
    private Integer ContinuousDay;
    /**
     * 最大连续学习天数
     */
    private Integer MaxDay;
    /**
     * 总学习天数
     */
    private Integer SumDay;
}
