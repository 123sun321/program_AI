package com.cube001.cubeboot.assignment.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import lombok.Data;
import lombok.experimental.Accessors;

/*
* Copyright (C), 2018 - 2019, ZhengZho uChuangZhi. Co., Ltd
* FileName: AssignmentResource
* 类的详细说明
*   作业资源实体类
* @author anonymous
* @date   2019/4/23 14:35
* @version 1.0
*/

@Data
@Accessors(chain = true)
@TableName("t_assignment_resource")
public class AssignmentResource extends FullAuditedEntityNPk<AssignmentResource> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(type= IdType.ID_WORKER)
    private Long id;
    /**
     * 课程编号
     */
    private Long assignmentId;
    /**
     * 名称
     */
    private String name;
    /**
     * 资源链接
     */
    private String url;
    /**
     * 排序
     */
    private Integer sort;


}
