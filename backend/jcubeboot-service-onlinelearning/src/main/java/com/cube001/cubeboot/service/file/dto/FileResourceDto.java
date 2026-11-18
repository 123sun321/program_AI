package com.cube001.cubeboot.service.file.dto;

import lombok.Data;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [ 文件名 ]
 * 类的详细说明 资源列表dto
 *
 * @author 马勇超
 * @version 1.0
 * @date 2025/3/14$ 9:48$
 */
@Data
public class FileResourceDto {
    /**
     * 编号
     */
    private Long id;
    /** 用户id */
    private Long userId;
    /**
     * 文件名
     */
    private String name;
    /**
     * 文件大小
     */
    private String size;
    /**
     * 文件类型
     */
    private String type;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 文件路径
     */
    private String path;
    /**
     * 文件所在目录
     */
    private Long directoryId;
    /**
     * 备用字段
     */
    private String reserve;
}
