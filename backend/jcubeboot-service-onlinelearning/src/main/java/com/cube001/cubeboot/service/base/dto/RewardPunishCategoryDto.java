package com.cube001.cubeboot.service.base.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * Copyright (C),2018-2019, ZhengZhouChuangZhi. Co.,Ltd.
 * FileName: [RewardPunishCategoryDto]
 * 奖惩Dto
 *
 * @author anonymous
 * @Date 2018-11-27
 * @version 1.0
 */
@Data
public class RewardPunishCategoryDto {

    /** 奖惩id */
    private Long id;

    /** 奖惩编号 */
    @NotEmpty
    @NotBlank
    @Length(max = 20,message = "奖惩编号超过20个字符")
    private String number;

    /** 奖惩名称 */
    @NotEmpty
    @NotBlank
    @Length(max = 20,message = "奖惩名称超过20个字符")
    private String name;

    /** 学校id */
    private Long schoolId;

    /** 类型 */
    private String type;

    /** 级别 */
    private String level;

    /** logo */
    private String logo;

    /** 描述 */
    @Length(max = 200,message = "描述超过200个字符")
    private String desc;
}
