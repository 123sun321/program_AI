package com.cube001.cubeboot.file.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.Version;

import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
* Copyright (C), 2025 - 2026, zxy.
* FileName: FileResource.java
* 类的详细说明 资源列表实体类
*
* @author anonymous
* @date 2025/3/14 9:49
* @version 1.0
*/
@Data
@Accessors(chain = true)
@TableName("f_file_resource")
public class FileResource extends FullAuditedEntityNPk<FileResource> {

    @TableId(type = IdType.ID_WORKER)

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
