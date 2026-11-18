package com.cube001.cubeboot.service.course.dto;

import com.cube001.cubeboot.course.entity.ExerciseJoin;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: ExerciseJoinReportDto
 * ExerciseJoinReportDto
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/3/28 18:12
 */
@Data
public class ExerciseJoinReportDto {
    /** 编号*/
    private Long id;
    /**报告名称*/
    @NotBlank
    private String content;
    /**是否私有报告*/
    private Boolean privateReport;
    /**实验编号*/
    private Long exerciseId;
    /**参与实验编号*/
    private Long exerciseJoinId;
    /**评论数*/
    private Integer commentNumber;
    /**查看数*/
    private Integer clickNumber;
    /**点赞数*/
    private Integer likeNumber;
    /**单词书*/
    private Integer wordNumber;
    /**创建时间*/
    private Date creationTime;
    /**参与实验*/
    private ExerciseJoinDto exerciseJoinDto;
}
