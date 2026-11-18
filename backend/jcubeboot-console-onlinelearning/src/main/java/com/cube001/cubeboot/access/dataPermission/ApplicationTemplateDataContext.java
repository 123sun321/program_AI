package com.cube001.cubeboot.access.dataPermission;

import com.cube001.cubeboot.core.permission.template.TemplateDataContext;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: com.cube001.cubeboot.access.dataPermission
 * 类的详细说明
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/1/7 15:09
 */
@Data
public class ApplicationTemplateDataContext extends TemplateDataContext {
    /**
     * 租户Id
     */
    private Long tenantId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 班级id
     */
    private List<Long> gradeIds;

    /**
     * 学院id
     */
    private Long departmentId;

    public ApplicationTemplateDataContext() {
        gradeIds = new ArrayList<>();
    }
}
