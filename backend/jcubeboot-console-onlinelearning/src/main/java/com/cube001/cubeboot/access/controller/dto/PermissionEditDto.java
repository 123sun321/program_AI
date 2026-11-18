package com.cube001.cubeboot.access.controller.dto;

import com.cube001.cubeboot.core.validateGroup.EditChecks;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.security.acl.Group;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: com.cube001.cubeboot.access.dto
 * 类的详细说明
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/12/26 14:20
 */
@Data
public class PermissionEditDto {
    private static final long serialVersionUID = 1L;

    private String parentName;

    @NotNull(groups = EditChecks.class)
    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    private String displayName;
    private String description;
    private Boolean isGrantedByDefault;
    private String multiTenancySides;
    private String featureDependency;
    private Integer dataType;
}
