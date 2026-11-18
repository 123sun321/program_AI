package com.cube001.cubeboot.service.base.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cube001.cubeboot.base.entity.Label;
import com.cube001.cubeboot.base.entity.LabelContent;

import java.util.List;

/**
* Copyright (C), 2025 - 2026, zxy.
* FileName: LabelContentMapper.java
* 类的详细说明
*
* @author anonymous
* @date 2025/3/12 9:32
* @version 1.0
*/
public interface LabelContentMapper extends BaseMapper<LabelContent> {
    /**
     * 通过关联编号获取标签列表
     * @author anonymous
     * @param id
     * @return
     */
    List<Label> getLabelByLinkId(Long id);
}
