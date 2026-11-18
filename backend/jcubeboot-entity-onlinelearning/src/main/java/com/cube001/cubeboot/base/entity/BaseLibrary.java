package com.cube001.cubeboot.base.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: BaseLibrary
 * 资源库
 *
 * @author Juci
 * @version 1.0
 * @date 2025/7/29 16:27
 */
@Data
@Accessors(chain = true)
@TableName("t_base_library")
public class BaseLibrary extends FullAuditedEntityNPk<BaseLibrary> {

    @TableId(type = IdType.ID_WORKER)

    /** id */
    private Long id;
    /**名称*/
    private String name;
    /**js资源*/
    private String jsLibrary;
    /**css资源*/
    private String cssLibrary;
}
