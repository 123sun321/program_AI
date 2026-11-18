package com.cube001.cubeboot.service.course.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: ExerciseResourceDto
 * ExerciseResourceDto
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/3/11 16:23
 */
@Data
public class ExerciseResourceDto {

    private Long id;
    /** 实验编号*/
    private Long exerciseId;
    /**名称*/
    @NotBlank
    @Length(min=1,max = 20,message = "名称为1-20个字符")
    private String name;
    /** 资源链接*/
    @NotBlank
    private String url;
    /**字数*/
    private Integer wordNumber;
    /** 排序*/
    private Integer sort;
    /**创建时间*/
    private Date creationTime;
}
