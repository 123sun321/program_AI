package com.cube001.cubeboot.community.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.Version;

import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntity;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author ZJY
 * @since 2019-03-18
 */
@Data
@Accessors(chain = true)
@TableName("t_integral_score")
public class IntegralScore extends FullAuditedEntityNPk<IntegralScore>{

    private static final long serialVersionUID = 1L;

    /**
     * 积分编号
     */
    @TableId(type = IdType.ID_WORKER)
    private Long id;
    /**
     * 积分类型
     */
    private String name;
    /**
     * 发放积分数
     */
    private Integer num;
    /**
     * 发放日期
     */
    @TableField("issue_time")
    private Date issueTime;
    /**
     * 到期日期
     */
    @TableField("end_time")
    private Date endTime;
    /**
     * 备注
     */
    private String remark;

    /**学生id*/
    @TableField("student_id")
    private Long studentId;

}
