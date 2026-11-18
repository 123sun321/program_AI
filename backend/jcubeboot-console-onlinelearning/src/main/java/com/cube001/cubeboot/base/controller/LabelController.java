package com.cube001.cubeboot.base.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.base.controller.util.ChineseToPinYin;
import com.cube001.cubeboot.base.entity.Label;
import com.cube001.cubeboot.base.entity.Profession;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.core.util.PingYinUtil;
import com.cube001.cubeboot.service.base.LabelService;
import com.cube001.cubeboot.service.base.dto.base.LabelConverter;
import com.cube001.cubeboot.service.base.dto.base.LabelDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.security.Principal;
import java.util.List;

/**
* Copyright (C), 2025 - 2026, zxy.
* FileName: LabelController.java
* 类的详细说明 标签类控制类
*
* @author anonymous
* @date 2025/3/8 17:51
* @version 1.0
*/
@Api(value = "LabelController",description = "标签Controller")
@RestController
@RequestMapping(value = "${cubeboot.app-manager-path}/label")
@RequiresPermissions("label")
public class LabelController implements IApplicationService {

    @Autowired
    private LabelConverter converter;

    @Autowired
    private LabelService service;

    /**
    * @description  标签添加
    * @author anonymous
    * @date 2025/3/20 10:45
    * @param   labelDto
    * @return   Boolean
    */
    @ApiOperation(value = "标签添加")
    @PostMapping(value = "add")
    @RequiresPermissions("label:add")
    public Boolean add(@RequestBody LabelDto labelDto){
        Boolean success = false;
        ChineseToPinYin chinese = new ChineseToPinYin();
        Label label = converter.dtoToEntity(labelDto);
        List<Label> labels = service.selectList(new EntityWrapper<Label>().eq("name",label.getName()));
        if(labels.size()!=0){
            return success;
        }else {
            String name = label.getName();
            String pinyin = chinese.getPinyinString(name);
            if(pinyin == "no"){
                label.setName(name.toUpperCase());
            }else {
                label.setPinyin(pinyin);
            }
            return service.insert(label);
        }
    }


    @ApiOperation(value = "pinyin")
    @PostMapping(value = "pinyin/{pin}")
    public String pinyin(@PathVariable("pin") String str){
        ChineseToPinYin chinese = new ChineseToPinYin();
        return chinese.toHanyuPinyin(str);
    }



    /**
    * @description  标签删除
    * @author anonymous
    * @date 2025/3/20 10:46
    * @param   id
    * @return   Boolean
    */
    @ApiOperation(value = "标签删除")
    @PostMapping(value = "del")
    @RequiresPermissions("label:del")
    public Boolean del(@RequestParam("id") Long id){
        return service.deleteById(id);
    }

    /**
    * @description  批量标签删除
    * @author anonymous
    * @date 2025/3/20 10:46
    * @param   ids
    * @return   Boolean
    */
    @ApiOperation(value = "批量标签删除")
    @PostMapping(value = "delBatches")
    @RequiresPermissions("label:del")
    public Boolean delBatches(@RequestBody List<Long> ids){
        return service.deleteBatchIds(ids);
    }

    /**
    * @description  修改标签
    * @author anonymous
    * @date 2025/3/20 10:47
    * @param   labelDto
    * @return   Boolean
    */
    @ApiOperation(value = "修改标签")
    @PostMapping(value = "edit")
    @RequiresPermissions("label:edit")
    public Boolean edit(@RequestBody LabelDto labelDto){
        Boolean success = false;
        ChineseToPinYin chinese = new ChineseToPinYin();
        List<Label> labels = service.selectList(new EntityWrapper<Label>().eq("name",labelDto.getName()));
        if(labels.size() > 1 ){
            return success;
        }else {
            String name = labelDto.getName();
            String pinyin = chinese.getPinyinString(name);
            if(pinyin == "no"){
                labelDto.setName(name.toUpperCase());
            }else {
                labelDto.setPinyin(pinyin);
            }
            return service.updateById(converter.dtoToEntity(labelDto));
        }
    }

    /**
    * @description  查询单条标签
    * @author anonymous
    * @date 2025/3/20 10:47
    * @param   id
    * @return   LabelDto
    */
    @ApiOperation(value = "查询单条")
    @GetMapping(value = "get")
    @RequiresPermissions("label:get")
    public LabelDto get(@RequestParam("id")Long id){
        Label label = service.selectById(id);
        return converter.entityToDto(label);
    }

    /**
    * @description  查询标签列表
    * @author anonymous
    * @date 2025/3/20 10:47
    * @return   List<LabelDto>
    */
    @ApiOperation(value = "查询标签列表")
    @GetMapping(value = "/list")
    @ResponseBody
    @RequiresPermissions("label:list")
    public List<LabelDto> getAll(){
        List<Label> labels = service.selectList(new EntityWrapper<>());
        return converter.listEntityToDto(labels);
    }

    /**
    * @description  通过关键字查询标签列表
    * @author anonymous
    * @date 2025/3/20 10:47
    * @param   str
    * @return   List<LabelDto>
    */
    @ApiOperation(value = "通过关键字查询标签列表")
    @GetMapping(value = "/chooseList")
    @RequiresPermissions("label:list")
    public List<LabelDto> chooseList(@RequestParam("str") String str){
        List<Label> labels = service.selectListWithKeyWord(str);
        return converter.listEntityToDto(labels);
    }

    /**
    * @description  通过标签名字查询标签列表
    * @author anonymous
    * @date 2025/3/20 10:48
    * @param   name
    * @return   List<LabelDto>
    */
    @ApiOperation(value = "通过标签名字查询标签列表")
    @GetMapping(value = "/getByName")
    @RequiresPermissions("label:list")
    public List<LabelDto> getByName(@RequestParam("name") String name){
        List<Label> labels = service.selectListByName(name);
        return converter.listEntityToDto(labels);
    }

    /**
    * @description  标签分页
    * @author anonymous
    * @date 2025/3/20 10:48
    * @param   searchDto
    * @return   page
    */
    @ApiOperation(value = "标签分页")
    @PostMapping(value = "/page")
    @RequiresPermissions("label:page")
    public Page<LabelDto> page(@RequestBody CommonSearchDto<Label> searchDto){
        Page<Label> page = service.pageWith(searchDto.getPlusPage(),searchDto.formToEntityWrapperWithSearch(new String[]{"name"},""));
        return converter.pageToDto(page);
    }

    /**
    * @description  实现标签的远方校验
    * @author anonymous
    * @date 2025/3/13 13:58
    * @param id,col,value
    * @return boolean
    */
    @ApiOperation(value = "验证数据重复")
    @RequestMapping(value = "validate", method = RequestMethod.GET)
    @ResponseBody
    @RequiresPermissions("label:validate")
    public Boolean validRepeat(@RequestParam(value = "id",required = false) Long id,@RequestParam("col") String col, @RequestParam("value") String value) {
        Boolean pass = false;
        Wrapper<Label> wrapper = new EntityWrapper<>();
        wrapper.eq(col,value);
        int count = service.selectCount(wrapper);
        Label label = service.selectOne(wrapper);
        if(count == 0 && label == null){
            pass = true;
        }
        if(count ==1 && label.getId().equals(id)){
            pass = true;
        }
        if(label!=null && !label.getId().equals(id)){
            pass = false;
        }
        return pass;
    }



}

