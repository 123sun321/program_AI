package com.cube001.cubeboot.access.controller.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: com.cube001.cubeboot.access.controller.dto
 * 数组参数验证
 *
 * @author 陈乐乐
 * @version 1.0
 * @date 2025/12/28 13:29
 */
@Data
public class PermissionListDto {
    @Valid
    @Size(min = 1)
    private List<PermissionEditDto> list;
}
