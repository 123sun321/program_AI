package com.cube001.cubeboot.service.course.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.cube001.cubeboot.core.validateGroup.EditChecks;
import com.cube001.cubeboot.course.entity.CourseJoin;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
* Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
* FileName:  CourseDto
* 课程dto
*
* @author anonymous
* @date   2019/3/11 15:25
* @version 1.0
*/
@Data
public class CourseDto {

    @NotNull(groups = EditChecks.class)
    private Long id;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 课程类别
     */
    private String category;
    /**
     * 名称
     */
    @NotBlank
    @Length(min=1,max = 20,message = "课程名称为1-20个字符")
    private String name;
    /**
     * 难易程度
     */
    private Integer complexity;
    /**
     * 课程阶段
     */
    private String stage;
    /**
     * 课程简介
     */
    private String summary;
    /**
     * 课程Logo
     */
    private String logo;
    /**
     * 课程类型（0-公开课1-教师课程）
     */
    private Integer type;
    /**
     * 实验个数
     */
    private Integer exerciseNumber;
    /**
     * 参与学习人数
     */
    private Integer joinNumber;
    /**
     * 课程邀请码
     */
    private String code;
    /**
     * 课程评价
     */
    private Integer commentNumber;
    /**
     * 标签
     */
    private List<String> tags;
    /**
     * 创建时间
     */
    private Date creationTime;
    /**
     * 课程作者
     */
    private  String author;
    /**
     * 关联查询是否存在该courseId,表示是否收藏
     */
    private Long courseId;
    /**
     * 课程参与表,判断是否参加了该课程
     */
    private CourseJoin courseJoin;
    /**
     * 课程推荐度
     */
    private Integer recommendDegree;
    /**
     * 该课程收藏总数量
     */
    private Integer collectedNumber;
}
