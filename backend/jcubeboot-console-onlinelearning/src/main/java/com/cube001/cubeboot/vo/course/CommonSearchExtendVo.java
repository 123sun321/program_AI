package com.cube001.cubeboot.vo.course;

import com.cube001.cubeboot.core.dto.CommonSearchDto;
import lombok.Data;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: CommonSearchExtendUserIdVo
 * 分页组件扩展UserId
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/7/6 13:39
 */
@Data
public class CommonSearchExtendVo<T> extends CommonSearchDto<T> {

    /**状态*/
    private Integer status;
}
