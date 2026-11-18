package com.cube001.cubeboot.service.community.dto;

import com.cube001.cubeboot.core.validateGroup.EditChecks;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: CommunityPlateDto
 * 社区板块dto
 *
 * @author 张浩杰
 * @version 1.0
 * @date 2025/3/19 16:40
 */
@Data
public class CommunityPlateDto {

    @NotNull(groups = EditChecks.class)
    private Long id;
    /**
     * 名称
     */
    @NotBlank
    @Length(min=1,max = 20,message = "社区板块名称为1-20个字符")
    private String name;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 简介
     */
    private String summary;
    /**
     * 角色编号（逗号分隔）
     */
    private String roles;
    /**
     * 创建时间
     */
    private Date creationTime;

}
