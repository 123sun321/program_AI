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
 * FileName: ExerciseJoinRecord
 * 参与实验记录
 *
 * @author Juci
 * @version 1.0
 * @date 2025/4/8 9:00
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_exercise_join_record")
public class ExerciseJoinRecord  extends FullAuditedEntityNPk<ExerciseJoinRecord> {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.ID_WORKER)
    private Long id;
    /**
     * 学生编号
     */
    private Long studentId;
    /**
     * 参与实验编号
     */
    private Long exerciseJoinId;
    /**
     * 实验时间（分钟）
     */
    private Integer minute;
}
