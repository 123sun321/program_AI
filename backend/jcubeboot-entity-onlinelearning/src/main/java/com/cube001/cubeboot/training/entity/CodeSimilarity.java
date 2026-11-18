package com.cube001.cubeboot.training.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.Version;

import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.assignment.entity.AssignmentSubmit;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
* Copyright (C), 2018 - 2019, ZhengZho uChuangZhi. Co., Ltd.
* FileName: CodeSimilarity.java
* 类的详细说明
*
* @author anonymous
* @date 2025/6/25 18:28
* @version 1.0
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_code_similarity")
public class CodeSimilarity extends FullAuditedEntityNPk<CodeSimilarity> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(type = IdType.ID_WORKER)
    private Long id;
    /**
     * 提交者作业id
     */
    private Long submitterWorkId;
    /**
     * 对比者作业id
     */
    private Long comparatorWorkId;
    /**
     * 代码相似度
     */
    private String similarity;
    /** 作业编号 */
    private Long assignmentId;
    /** 学生编号 */
    private Long studentId;
    //是否抄袭
    private boolean plagiarize;
    @TableField(exist = false)
    private Student student;
    @TableField(exist = false)
    private AssignmentSubmit assignmentSubmit;


}
