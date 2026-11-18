package com.cube001.cubeboot.course.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/** 
* Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd. 
* FileName:  Course
* 课程实体类
* 
* @author anonymous 
* @date   2019/3/11 15:17
* @version 1.0 
*/
@Data
@TableName("t_base_course")
@Accessors(chain = true)
public class Course extends FullAuditedEntityNPk<Course> {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.ID_WORKER)
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
     * 课程名称
     */
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
     * 标签
     */
    @TableField(exist = false)
    private List<String> tags;
    /**
     * 课程评价
     */
    private Integer commentNumber;
    /**
     * 课程作者
     */
    private  String author;
    /**
     * 添加收藏夹实体, 判断当前用户是否收藏当前课程
     */
    @TableField(exist = false)
    private CourseCollect courseCollect;
    /**
     * 课程参与
     */
    @TableField(exist = false)
    private CourseJoin courseJoin;
    /**
     * 课程的推荐度
     */
    private Integer recommendDegree;
}
