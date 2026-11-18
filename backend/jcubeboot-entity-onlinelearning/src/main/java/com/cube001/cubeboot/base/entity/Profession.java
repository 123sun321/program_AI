package com.cube001.cubeboot.base.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntity;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
* Copyright (C), 2025 - 2026, zxy.
* FileName: Profession.java
* 专业管理实体类
*
* @author 马勇超
* @date 2025/12/4 15:00
* @version 1.0
*/
@Data
@Accessors(chain = true)
@TableName("t_base_profession")
public class Profession extends FullAuditedEntityNPk<Profession>{

    @TableId(type = IdType.ID_WORKER)
    private Long id;

    /** 名称 */
    private String name;

    /** 排序 */
    private Integer sort;

    /** 专业编号*/
    private String code;

    /** 班级列表 */
    @TableField(exist = false)
    private List<Grade> gradeList;

}
