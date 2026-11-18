package com.cube001.cubeboot.service.file.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [ 文件名 ]
 * 类的详细说明 文件夹dto
 *
 * @author 马勇超
 * @version 1.0
 * @date 2025/3/16$ 16:15$
 */
@Data
public class DirectoryWithFileResourceDto {
    Long id;
    String name;
    String type;
    String size;
    Date creationTime;
    @TableField(exist = false)
    String path;
}
