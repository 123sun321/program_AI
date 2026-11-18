package com.cube001.cubeboot.service.course.dto.ExerciseJoinRecord;

import com.cube001.cubeboot.service.training.dto.CodeDto;
import com.cube001.cubeboot.training.entity.Code;
import lombok.Data;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: JoinExerciseDto
 * 结束实验实体
 *
 * @author Juci
 * @version 1.0
 * @date 2025/7/29 10:55
 */
@Data
public class JoinExerciseDto {

    /**
     * 参与实验方式 0-新实验 1-继续实验
     */
    public Integer type;
    /**
     * 实验编号
     */
    public Long exerciseId;
    /**
     * 实验代码
     */
    public CodeDto codeDto;
    /**
     * 学习时间
     */
    public Integer studyTime;
}
