package com.cube001.cubeboot.service.base.impl;


import com.cube001.cubeboot.base.entity.Label;
import com.cube001.cubeboot.base.entity.LabelContent;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.service.base.LabelContentService;
import com.cube001.cubeboot.service.base.mapper.LabelContentMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* Copyright (C), 2025 - 2026, zxy.
* FileName: LabelContentServiceImpl.java
* 类的详细说明
*
* @author anonymous
* @date 2025/3/12 9:31
* @version 1.0
*/
@Service
public class LabelContentServiceImpl extends ServiceImpl<LabelContentMapper, LabelContent> implements LabelContentService {

    @Override
    public List<String> getNameByLinkId(Long id){
        List<Label> labelList = this.baseMapper.getLabelByLinkId(id);
        List<String> strs = labelList.stream().map(m->m.getName()).collect(Collectors.toList());
        return  strs;
    }
}
