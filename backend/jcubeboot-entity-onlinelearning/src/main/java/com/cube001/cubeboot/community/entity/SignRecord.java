package com.cube001.cubeboot.community.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
 * <p>
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: SignRecord
 * 签到记录实体类
 * </p>
 *
 * @author anonymous
 * @since 2019-03-21
 */
@Data
@Accessors(chain = true)
@TableName("t_sign_record")
public class SignRecord extends FullAuditedEntityNPk<SignRecord> {

    private static final long serialVersionUID = 1L;

    /**主键*/
    @TableId(type = IdType.ID_WORKER)
    private Long id;

    /**签到时间*/
    private Date signTime;

    /**班级名称*/
    private String gradeName;

    /**班级编号*/
    private Long gradeId;

    /**学生编号*/
    private Long studentId;

    /**学生名称*/
    private String studentName;

    /**用户编号*/
    private Long userId;

    @TableField(exist = false)
    private Integer count;


}
