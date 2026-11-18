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
 * FileName: ExerciseJoinReport
 * 实验参与报告
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/3/28 17:45
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_exercise_join_report")
public class ExerciseJoinReport extends FullAuditedEntityNPk<ExerciseJoinReport> {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.ID_WORKER)
    /** 编号*/
    private Long id;
    /**报告名称*/
    private String content;
    /**是否私有报告*/
    private Boolean privateReport;
    /**参与实验编号*/
    private Long exerciseJoinId;
    /**评论数*/
    private Integer commentNumber;
    /**查看数*/
    private Integer clickNumber;
    /**字数*/
    private Integer wordNumber;
    /**点赞数*/
    private Integer likeNumber;
}
