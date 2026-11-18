package com.cube001.cubeboot.service.base.dto;

import lombok.Data;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: BaseLibraryDto
 * BaseLibraryDto
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/7/29 16:35
 */
@Data
public class BaseLibraryDto {
    /** id */
    private Long id;
    /**名称*/
    private String name;
    /**js资源*/
    private String jsLibrary;
    /**css资源*/
    private String cssLibrary;
}
