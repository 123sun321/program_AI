package com.cube001.cubeboot.service.course.dto.ExerciseJoinRecord;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: ExerciseJoinRecordDto
 * ExerciseJoinRecordDto
 *
 * @author Juci
 * @version 1.0
 * @date 2025/4/8 13:41
 */
@Data
public class ExerciseJoinRecordDto {
    private static final long serialVersionUID = 1L;
    private Long id;
    /**
     * 学生编号
     */
    private Long studentId;
    /**
     * 参与实验编号
     */
    private Long exerciseJoinId;
    /**
     * 实验编号
     */
    @NotNull
    private Long exerciseId;
    /**
     * 实验时间（分钟）
     */
    @NotNull
    private Integer minute;
    /**创建时间*/
    private Date creationTime;
}
