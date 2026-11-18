package com.cube001.cubeboot.training.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.service.assignment.dto.ListAssignmentDto;
import com.cube001.cubeboot.service.training.CodeSimilarityService;
import com.cube001.cubeboot.training.entity.CodeSimilarity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
* Copyright (C), 2018 - 2019, ZhengZho uChuangZhi. Co., Ltd.
* FileName: CodeSimilarityController.java
* 类的详细说明
*
* @author 张人杰
* @date 2025/6/25 18:22
* @version 1.0
*/
@Api(value = "CodeSimilarityController", description = "代码相似度controller")
@RestController
@RequestMapping(value = "${cubeboot.app-manager-path}/code")
public class CodeSimilarityController implements IApplicationService {
    @Autowired
    private CodeSimilarityService codeSimilarityService;

    @ApiOperation(value = "分页展示",notes = "代码相似度的分页展示")
    @RequestMapping(value = "page", method = RequestMethod.POST)
//    @RequiresPermissions("assignment:page")
    public Page<CodeSimilarity> page(@RequestBody CommonSearchDto<CodeSimilarity> searchDto) {
        Map<String,String> map = new HashMap<>();
        map.put("submitter_work_id","a.");

        Page<CodeSimilarity> page = codeSimilarityService.getStuRewardPunishList( searchDto.getPlusPage(), searchDto.formToEntityWrapper(null,map));
        return page ;
    }

}

