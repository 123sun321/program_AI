package com.cube001.cubeboot.service.course.dto;

import com.cube001.cubeboot.service.training.dto.TrainingDto;
import lombok.Data;

import java.util.Date;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: ExerciseJoinDetailDto
 * 实验练习参与详情业务逻辑详情类
 *
 * @author Juci
 * @version 1.0
 * @date 2025/3/18 17:31
 */
@Data
public class ExerciseJoinDetailDto {
    /**
     * 编号
     */
    private Long id;
    /**
     * 实验编号
     */
    private Long exerciseId;
    /**
     * 学生编号
     */
    private Long studentId;
    /**
     * 练习报告
     */
    private String joinReport;
    /**
     * 对应练习
     */
    private Long trainingId;
    /**创建时间*/
    private Date creationTime;
    /**练习*/
    private TrainingDto trainingDto;

}
