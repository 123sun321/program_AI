package com.cube001.cubeboot.file.entity;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.annotations.Version;

import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
* Copyright (C), 2025 - 2026, zxy.
* FileName: Directory.java
* 类的详细说明 文件夹实体类
*
* @author 马勇超
* @date 2025/3/14 15:21
* @version 1.0
*/
@Data
@Accessors(chain = true)
@TableName("f_directory")
public class Directory extends FullAuditedEntityNPk<Directory> {

    private static final long serialVersionUID = 1L;

    /**
     * 文件夹编号
     */
    @TableId(type = IdType.ID_WORKER)
    private Long id;
    /**
     * 目录名
     */
    private String name;
    /**
     * 父目录
     */
    private Long parentId;

    /** 用户id */
    private Long userId;

}
