package com.cube001.cubeboot.service.base.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.cube001.cubeboot.core.validateGroup.AddChecks;
import com.cube001.cubeboot.core.validateGroup.EditChecks;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [StudenthonorDto ]
 * 添加学生奖惩dto类
 *
 * @author HuangPengju
 * @version 1.0
 * @date 2025/11/30
 */
@Data
public class StudenthonorDto {
    @NotNull(groups = EditChecks.class)
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
    @NotNull(groups = AddChecks.class)
    private Long rewardPunishCategoryId;

    /** 学年 */
    private String SchoolYear;

    /** 学生学号 */
    private String code;

    /** 学生姓名 */
    private String studentName;

    /** 奖惩名称 */
    private String rewardPunishCategoryName;

    /** 奖惩类型 */
    private String type;

    /** 奖惩级别 */
    private String level;

    /** 奖惩logog */
    private String logo;

    /** 多个学生id */
    @NotNull(groups = AddChecks.class)
    private List<Long> ids;

    /** 学生头像 */
    private String stuImg;
}
