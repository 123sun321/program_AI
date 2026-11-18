package com.cube001.cubeboot.service.base.event;

import lombok.Data;
import lombok.Getter;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: com.cube001.cubeboot.service.base.event
 * 创建学习事件
 *
 * @author 陈乐乐
 * @version 1.0
 * @date 2025/2/18 13:47
 */
@Getter
public class CreateSchoolEvent {
    private Long schoolId;

    public CreateSchoolEvent(Long schoolId) {
        this.schoolId = schoolId;
    }
}
