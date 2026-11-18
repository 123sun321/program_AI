package com.cube001.cubeboot.service.base.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [StudenthonorEditDto ]
 * 编辑学生奖惩Dto类
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/11/30
 */
@Data
public class StudenthonorEditDto {
    private Long id;

    /** 学生编号 */
    private Long studentId;

    /** 荣誉证书原图 */
    private String masterUrl;

    /** 荣誉证书缩略图 */
    private String thumbnailUrl;

    /** 简介 */
    @Length(max = 500,message = "学校名称超过500个字符")
    private String abstracts;

    /** 奖惩表id */
    @NotNull
    private Long rewardPunishCategoryId;

    /** 学年 */
    private String SchoolYear;





}
