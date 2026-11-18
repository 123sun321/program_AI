package com.cube001.cubeboot.training.controller;

import com.cube001.cubeboot.base.entity.Grade;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.validateGroup.EditChecks;
import com.cube001.cubeboot.service.base.dto.GradeDto;
import com.cube001.cubeboot.service.training.CodeService;
import com.cube001.cubeboot.service.training.dto.CodeConverter;
import com.cube001.cubeboot.service.training.dto.CodeDto;
import com.cube001.cubeboot.training.entity.Code;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: CodeController
 * CodeController
 *
 * @author Juci
 * @version 1.0
 * @date 2025/6/18 13:35
 */
@Api(value = "CodeController", description = "代码controller")
@RestController
@RequestMapping(value = "${cubeboot.app-manager-path}/code")
public class CodeController implements IApplicationService {
    @Autowired
    private CodeService codeService;
    @Autowired
    private CodeConverter codeConverter;

    /**
     * 根据CodeId查询代码
     * @author Juci
     * @param id
     * @return
     */
    @ApiOperation(value = "根据CodeId查询代码", notes = "根据CodeId查询代码")
    @GetMapping(value = "/get")
    public CodeDto get(@RequestParam("id") Long id) {
        Code training = codeService.selectById(id);
        return codeConverter.entityToDto(training);
    }

    /**
     * 修改代码
     * @author Juci
     * @param codeDto
     */
    @ApiOperation(value = "修改代码",notes = "修改代码")
    @PostMapping("/update")
    public void update(@RequestBody @Validated CodeDto codeDto) {
       codeService.updateCodeDto(ShiroKit.getUserId(),codeDto);
    }
}
