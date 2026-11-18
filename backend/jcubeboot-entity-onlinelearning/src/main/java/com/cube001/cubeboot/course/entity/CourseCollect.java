package com.cube001.cubeboot.course.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author anonymous
 * @since 2019-03-11
 */
@Data
@Accessors(chain = true)
@TableName("t_course_collect")
public class CourseCollect extends FullAuditedEntityNPk<CourseCollect> {

    private static final long serialVersionUID = 1L;

    /**主键*/
    @TableId(type = IdType.ID_WORKER)
    private Long id;

    /**用户编号*/
    private Long userId;

    /**课程编号*/
    private Long courseId;


}
