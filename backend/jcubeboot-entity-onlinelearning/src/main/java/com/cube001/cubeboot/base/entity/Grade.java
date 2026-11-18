package com.cube001.cubeboot.base.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
* Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
* FileName: Grade
* 班级do
*
* @author 郭广明
* @date   2018/12/4 14:48
* @version 1.0
*/
@Data
@Accessors(chain = true)
@TableName("t_base_grade")
public class Grade extends FullAuditedEntityNPk<Grade> {
    @TableId(type = IdType.ID_WORKER)

    /** id */
    private Long id;

    /** 班级名称 */
    private String name;

    /** 学年 */
    private String year;

    /** 学制 */
    @TableField("school_length")
    private String schoolLength;

    /** 导员 */
    private String counsellor;

    /** 专业id */
    @TableField("profession_id")
    private Long professionId;

    /** 是否在校 */
    @TableField("in_school")
    private Boolean inSchool;

    /** 入学时间 */
    @TableField("start_date")
    private Date startDate;

    /** 毕业时间 */
    @TableField("graduate_date")
    private Date graduateDate;

    /** 班级人数 */
    @TableField("class_size")
    private Integer classSize;

    /** 专业 */
    @TableField(exist = false)
    private Profession profession;


}
