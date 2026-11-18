package com.cube001.cubeboot.community.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.assignment.entity.Assignment;
import com.cube001.cubeboot.community.entity.CommunityPlate;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.service.community.CommunityPlateService;
import com.cube001.cubeboot.service.community.dto.CommunityPlateConverter;
import com.cube001.cubeboot.service.community.dto.CommunityPlateDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/***
* Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
* FileName: CommunityPlateController
* 社区板块controller
*
* @author anonymous
* @date   2019/3/19 16:15
* @version 1.0
*/
@Api(value = "CommunityPlateController",description = "社区板块Controller")
@RestController
@RequestMapping(value = "${cubeboot.app-manager-path}/communityPlate")
@RequiresPermissions("communityPlate")
public class CommunityPlateController implements IApplicationService {
    @Autowired
    private CommunityPlateService communityPlateService;
    @Autowired
    private CommunityPlateConverter communityPlateConverter;
    /**  
     * @description 社区板块通过name搜索/分页
     * @author anonymous 
     * @date 2025年3月19日 17:31:50
     * @param
     * @return  communityPlatePage
    */
    @ApiOperation(value = "社区板块搜索/分页",notes = "社区板块搜索/分页")
    @RequestMapping(value = "/page",method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("communityPlate:page")
    public Page<CommunityPlateDto> communityPlatePage(@RequestBody CommonSearchDto<CommunityPlate> commonSearchDto){
        Page<CommunityPlate> communityPlatePage = communityPlateService.selectPage
                (commonSearchDto.getPlusPage(),commonSearchDto.formToEntityWrapper(null).orderBy("sort"));
        return  communityPlateConverter.pageCommunityPlateDto(communityPlatePage);
    }
    /**
     * @description 删除/批量删除社区板块
     * @author anonymous
     * @date  2019年3月19日 17:36:08
     * @param  ids
     * @return  result
    */
    @ApiOperation(value = "删除/批量删除社区板块")
    @PostMapping("/delete")
    @RequiresPermissions("communityPlate:del")
    public void deleteCommunityPlate(@RequestBody List<Long> ids){
        communityPlateService.delete(ids);
    }
    /**
     * @description 修改社区板块
     * @author anonymous
     * @date  2019年3月19日 17:45:19
     * @param communityPlateDto
     * @return Boolean
    */
    @ApiOperation(value = "编辑/修改社区板块")
    @PostMapping(value = "/edit")
    @RequiresPermissions("communityPlate:edit")
    public void editCommunityPlate(@RequestBody @Validated CommunityPlateDto communityPlateDto){
        communityPlateService.edit(communityPlateDto);
    }
    /**
     * @description 添加社区板块
     * @author anonymous
     * @date  2019年3月20日 15:12:20
     * @param  communityPlateDto
     * @return  Boolean
    */
    @ApiOperation(value = "添加社区板块")
    @PostMapping(value = "/add")
    @RequiresPermissions("communityPlate:add")
    public void addCommunityPlate(@RequestBody @Validated CommunityPlateDto communityPlateDto){
        communityPlateService.add(communityPlateDto);
    }

    /**
    * @description  获取所有社区板块
    * @author anonymous
    * @date 2025/4/24 17:34
    * @return   list
    */
    @ApiOperation(value = "获取所有社区板块")
    @GetMapping(value = "list")
    @RequiresPermissions("communityPlate:list")
    public List<CommunityPlateDto> list(){
        List<CommunityPlate> communityPlateList = communityPlateService.selectList(new EntityWrapper<>());
        return communityPlateConverter.dtoToListCommunityPlate(communityPlateList);
    }

    /**
     * @description 通过角色获取权限社区板块
     * @author anonymous
     * @date 2025年3月22日 13:45:31
     * @param
     * @return communityPlateList
    */
    @ApiOperation(value = "通过角色获取权限社区板块")
    @PostMapping("/getPlateByRole")
    @RequiresPermissions("communityPlate:get")
    public Map<Object,String> getPlateByRole(){
        //获取当前登录用户ID
        Long userId = ShiroKit.getUserId();
        Map<Object,String> communityPlateList = communityPlateService.getPlateByRole(userId);
        return communityPlateList;
    }

    /**
     * @description 验证排序是否重复
     * @author anonymous
     * @date 2025/3/22 16:21
     * @param  id,col,value
     * @return Boolean
    */
    @ApiOperation(value = "验证排序是否重复")
    @GetMapping(value = "validate")
    @RequiresPermissions("communityPlate:validate")
    public  Boolean validRepeat(Long id,@RequestParam("col") String col,@RequestParam("value") String value) {
        if (id == null) {
            //如果编号id不存在
            Integer communityPlateSort = communityPlateService.getCommunityPlateSort(col, value);
            if (communityPlateSort > 0) {
                return false;
            }
        } else {
            //如果编号id存在
            Wrapper<CommunityPlate> communityPlateEntityWrapper =new EntityWrapper<>();
            communityPlateEntityWrapper.eq(col,value).notIn("id",id);
            Integer communityPlateSort = communityPlateService.selectCount(communityPlateEntityWrapper);
            if (communityPlateSort > 0) {
                return false;
            }
        }
        return true;
    }
   /**
     * @description通过ID查询数据
     * @author anonymous
     * @date 2025/3/28 14:33
     * @Param
     * @return
     */
    @ApiOperation(value = "查询id",notes = "通过ID获取本条数据")
    @GetMapping(value = "getById")
    @RequiresPermissions("communityPlate:get")
    public  CommunityPlateDto  getById(@RequestParam("id") Long id) {
        CommunityPlate communityPlate = communityPlateService.selectById(id);
        CommunityPlateDto communityPlateDto = communityPlateConverter.communityPlateDto(communityPlate);
        return communityPlateDto;
    }
}

