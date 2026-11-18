package com.cube001.cubeboot.vo.course;

import com.cube001.cubeboot.core.dto.CommonSearchDto;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: CommonSearchExtendUserIdVo
 * 分页组件扩展UserId
 *
 * @author Juci
 * @version 1.0
 * @date 2025/4/22 10:39
 */
@Data
public class CommonSearchExtendUserIdVo<T> extends CommonSearchDto<T> {

    /**用户编号*/
    private Long userId;
}
