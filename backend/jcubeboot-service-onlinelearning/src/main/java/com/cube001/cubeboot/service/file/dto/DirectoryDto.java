package com.cube001.cubeboot.service.file.dto;

import com.cube001.cubeboot.core.validateGroup.EditChecks;
import com.cube001.cubeboot.file.entity.FileResource;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [ DirectoryDto ]
 * 文件夹实体dto
 *
 * @author 马勇超
 * @version 1.0
 * @date 2025/3/14$ 15:24$
 */
@Data
public class DirectoryDto {

    /**
     * 文件夹编号
     */
    @NotNull(groups = EditChecks.class)
    private Long id;
    /**
     * 目录名
     */
    @NotBlank
    @Length(max = 20,message = "目录名称超过20个字符")
    private String name;
    /** 用户id */
    private Long userId;
    /**
     * 父目录
     */
    private Long parentId;

    /** 文件类型 */
    private String type;

    private Date creationTime;

    /** 递归子目录 */
    private List children = new ArrayList();

    /** 文件夹图标 */
    private String className = "iconfont icon-wenjianjia";
}
