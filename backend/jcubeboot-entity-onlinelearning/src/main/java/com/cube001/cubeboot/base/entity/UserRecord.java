package com.cube001.cubeboot.base.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: UserRecord
 * 用户浏览记录
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/4/22 13:39
 */
@Data
@Accessors(chain = true)
@TableName("sys_user_record")
public class UserRecord extends FullAuditedEntityNPk<UserRecord> {
    @TableId(type = IdType.ID_WORKER)

    /** id */
    private Long id;
    /**浏览者*/
    private Long visitor;
    /**被浏览者*/
    private Long areVisitor;
}
