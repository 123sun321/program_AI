package com.cube001.cubeboot.service.training.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.assignment.entity.AssignmentSubmit;
import com.cube001.cubeboot.training.entity.CodeSimilarity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Copyright (C), 2018 - 2019, ZhengZho uChuangZhi. Co., Ltd.
* FileName: CodeSimilarityMapper.java
* 类的详细说明
*
* @author anonymous
* @date 2025/6/26 14:04
* @version 1.0
*/
public interface CodeSimilarityMapper extends BaseMapper<CodeSimilarity> {
    List<CodeSimilarity> getCodeSimilarityList(Page<CodeSimilarity> page,  @Param("ew") Wrapper<CodeSimilarity> wrapper);
}
