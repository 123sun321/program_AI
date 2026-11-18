package com.cube001.cubeboot.service.file.dto;

import lombok.Data;

import java.util.List;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [ 文件名 ]
 * 类的详细说明 文件夹移动字段dto
 *
 * @author 马勇超
 * @version 1.0
 * @date 2025/3/20$ 9:10$
 */
@Data
public class RemoveFile {
    List<Long> childrenIds;
    Long parentId;
}
