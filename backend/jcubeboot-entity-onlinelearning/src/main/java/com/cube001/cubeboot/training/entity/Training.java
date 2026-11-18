package com.cube001.cubeboot.training.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.base.entity.Grade;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.core.authorization.entity.User;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: $FILENAME
 * 练习表的 实体类
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/3/13 17:50
 */
@Data
@Accessors(chain = true)
@TableName("t_training")
public class Training extends FullAuditedEntityNPk<Training> {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ID_WORKER)
    private Long id;

    /**标题 */
    @TableField
    private String title;

    /**简介 */
    private String summary;

    /**类型 */
    private String type;

    /**存储目录结构 */
    @TableField("training_path_id")
    private Long trainingPathId;

    /**是否私有,私有1,非私有0 */
    @TableField("private_or_not")
    private Boolean privateOrNot;

    /**学生编号 */
    @TableField("student_id")
    private Long studentId;

    /**代码编号 */
    @TableField("code_id")
    private Long codeId;

    /**点赞数量 */
    @TableField("like_number")
    private Integer likeNumber;

    /** 练习的难易程度*/
    @TableField("difficult_level")
    private Integer difficultLevel;
    /**是否可编辑代码*/
    private Boolean canEdit;
    /**最后的提交时间*/
    @TableField("last_modification_time")
    private Date submitTime;

    /** 学生实体,使用img_url,name 字段*/
    @TableField(exist = false)
    private Student student;

    /** 年级实体,使用 grade_name字段*/
    @TableField(exist = false)
    private Grade grade;

    /**模糊查询的 起始查询时间*/
    @TableField(exist = false)
    private Date beginTime;

    /**模糊查询的 终止查询时间*/
    @TableField(exist = false)
    private Date endTime;
    /**当前用户是否对目前作品点赞*/
    @TableField(exist = false)
    private Boolean isApprove;
    /**代码详情*/
    @TableField(exist = false)
    private Code code;
    /**用户实体*/
    @TableField(exist = false)
    private User user;
}
