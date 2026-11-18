package com.cube001.cubeboot.service.training.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cube001.cubeboot.training.entity.Code;

import java.util.List;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: CodeMapper
 * 代码mapper
 *
 * @author anonymous
 * @date 2025/03/11 16:52
 * @version 1.0
 */

public interface CodeMapper extends BaseMapper<Code> {
    Code selectByAssignmentSubmitId(Long id);
//    List<Code> selectList();
}
