package com.cube001.cubeboot.base.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotations.TableName;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author anonymous
 * @since 2018-11-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_base_studentPicture")
public class Studentpicture extends FullAuditedEntity<Studentpicture,Long> implements Serializable {
    /**
     * 学生编号
     */
    @TableField("studentId")
    private Long studentId;
    /**
     * 照片地址
     */
    @TableField("imgUrl")
    private String imgUrl;
    /**
     * 简介
     */
    @TableField("abstract")
    private String abstracts;
}
