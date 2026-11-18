package com.cube001.cubeboot.service.course.dto;

import com.cube001.cubeboot.core.validateGroup.EditChecks;
import com.cube001.cubeboot.course.entity.Course;
import com.cube001.cubeboot.service.training.dto.CodeDto;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: ExerciseDto
 * 实验练习业务逻辑数据交互类
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/3/11 15:07
 */
@Data
public class ExerciseDto {

    @NotNull(groups = EditChecks.class)
    private Long id;
    /** 用户编号*/
    public Long userId;
    /**
     * 所属课程
     */
    private Long courseId;
    /**
     * 课程名称
     */
    private String courseName;
    /** 标题*/
    @NotBlank
    @Length(min=1,max = 50,message = "实验名称为1-50个字符")
    private String name;
    /**
     * 知识点
     */
    @NotBlank
    private String knowledge;
    /**
     * 技术类型
     */
    @NotBlank
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
    /**标签*/
    private List<String> tags;
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
    /**注册时间*/
    private Date creationTime;
    /**课程信息*/
    private Course course;
    /** 实验资源*/
    private List<ExerciseResourceDto> exerciseResourceDtoList;
    /** 初始代码*/
    private CodeDto codeDto;
}
