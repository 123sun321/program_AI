package com.cube001.cubeboot.base.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.cube001.cubeboot.base.entity.BaseLibrary;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.service.base.BaseLibraryService;
import com.cube001.cubeboot.service.base.dto.BaseLibraryConverter;
import com.cube001.cubeboot.service.base.dto.BaseLibraryDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: BaseLibraryController
 * BaseLibraryController
 *
 * @author Juci
 * @version 1.0
 * @date 2025/7/29 16:40
 */
@Api(value = "BaseLibraryController",description = "资源控制器")
@RestController
@RequestMapping(value = "${cubeboot.app-manager-path}/baselibrary")
public class BaseLibraryController implements IApplicationService {

    @Autowired
    private BaseLibraryService baseLibraryService;
    @Autowired
    private BaseLibraryConverter baseLibraryConverter;

    /**
     * 添加资源库
     * @author Juci
     * @param baseLibraryDto
     */
    @ApiOperation(value = "添加资源库")
    @PostMapping("/add")
    public void add(@RequestBody @Validated BaseLibraryDto baseLibraryDto) {
        BaseLibrary baseLibrary = this.baseLibraryConverter.dtoToEntity(baseLibraryDto);
        this.baseLibraryService.insert(baseLibrary);
    }


    /**
     * 获取资源库
     * @author Juci
     * @param id
     * @return
     */
    @ApiOperation(value = "获取")
    @RequestMapping(value = "get", method = RequestMethod.GET)
    public BaseLibraryDto get(@RequestParam("id") Long id) {
        return  this.baseLibraryConverter.entityToDto(this.baseLibraryService.selectById(id));
    }
    /**
     * 获取资源库列表
     * @author Juci
     * @return
     */
    @ApiOperation(value = "获取列表")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public List<BaseLibraryDto> list(){
        Wrapper<BaseLibrary> wrapper = new EntityWrapper<>();
        List<BaseLibrary> baseLibraries = this.baseLibraryService.selectList(wrapper);
        return this.baseLibraryConverter.dtoList(baseLibraries);
    }

    /**
     * 删除资源库
     * @author Juci
     * @param id
     * @return
     */
    @ApiOperation(value = "删除资源库")
    @RequestMapping(value = "del", method = RequestMethod.GET)
    public void del(@RequestParam("id") Long id) {
        this.baseLibraryService.deleteById(id);
    }
}
