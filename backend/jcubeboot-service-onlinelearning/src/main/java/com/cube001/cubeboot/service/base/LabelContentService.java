package com.cube001.cubeboot.service.base;

import com.baomidou.mybatisplus.service.IService;
import com.cube001.cubeboot.base.entity.LabelContent;

import java.util.List;

/**
* Copyright (C), 2025 - 2026, zxy.
* FileName: LabelContentService.java
* 类的详细说明
*
* @author 马勇超
* @date 2025/3/12 9:31
* @version 1.0
*/
public interface LabelContentService extends IService<LabelContent> {
    /**
     * 通过关联编号获取标签名称列表
     * @param id
     * @return
     */
    List<String> getNameByLinkId(Long id);
}
