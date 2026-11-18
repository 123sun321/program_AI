package com.cube001.cubeboot.course.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: Exercise
 * 实验练习实体类
 * @author anonymous
 * @date 2025/03/11 14:48
 * @version 1.0
 */
@Data
@Accessors(chain = true)
@TableName("t_exercise")
public class Exercise extends FullAuditedEntityNPk<Exercise> {

    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.ID_WORKER)
    private Long id;
    /** 用户编号*/
    public Long userId;
    /**
     * 所属课程
     */
    private Long courseId;
    /**
     * 标题
     */
    private String name;
    /**
     * 知识点
     */
    private String knowledge;
    /**
     * 技术类型
     */
    private String category;
    /**
     * 难易程度
     */
    private Integer complexity;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * Logo
     */
    private String logo;
    /**
     * 参与实验人数
     */
    private Integer joinNumber;
    /**
     * 实验详情
     */
    private String content;
    /**
     * 实验类型(0-课程实验 1-单个练习)
     */
    private Integer type;
    /**
     * 文件格式（0- 单文件形式  1-多文件形式）
     */
    private Integer fileType;
    /**
     * 实验数据
     */
    private String mockData;
    /**
     * 是否有初始数据
     */
    private Boolean haveCode;
    /**
     * 初始代码编号
     */
    private Long codeId;
    /**作者*/
    private String author;
    /** 推荐度 */
    private Integer recommendDegree;
}
