package com.cube001.cubeboot.service.training;


import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.cube001.cubeboot.assignment.entity.Assignment;
import com.cube001.cubeboot.training.entity.CodeSimilarity;

/**
* Copyright (C), 2018 - 2019, ZhengZho uChuangZhi. Co., Ltd.
* FileName: CodeSimilarityService.java
* 类的详细说明
*
* @author 张人杰
* @date 2025/6/25 18:20
* @version 1.0
*/
public interface CodeSimilarityService extends IService<CodeSimilarity> {
    Page<CodeSimilarity> getStuRewardPunishList(Page<CodeSimilarity> page, Wrapper<CodeSimilarity> formToEntityWrapperWithSearch);
}
