package com.cube001.cubeboot.service.approve.dto;

import com.cube001.cubeboot.approve.entity.Approve;
import org.mapstruct.Mapper;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [ 文件名 ]
 * 类的详细说明
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/4/22$ 13:49$
 */
@Mapper(componentModel = "spring")
public interface ApproveConverter {

    Approve dtoToEntity(ApproveDto approveDto);

}
