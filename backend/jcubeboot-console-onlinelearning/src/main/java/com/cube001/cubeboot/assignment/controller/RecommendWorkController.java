package com.cube001.cubeboot.assignment.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.assignment.entity.AssignmentSubmit;
import com.cube001.cubeboot.assignment.entity.RecommendWork;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.service.assignment.AssignmentSubmitService;
import com.cube001.cubeboot.service.assignment.RecommendWorkService;
import com.cube001.cubeboot.service.assignment.dto.RecommendWorkConverter;
import com.cube001.cubeboot.service.assignment.dto.RecommendWorkDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* Copyright (C), 2025 - 2026, zxy.
* FileName: RecommendWorkController.java
* 优秀作品展示控制类
*
* @author 马勇超
* @date 2025/4/29 18:08
* @version 1.0
*/
@RestController
@Api(value = "优秀作品Controller",description = "优秀作品Controller")
@RequestMapping(value = "${cubeboot.app-manager-path}/recommendWork")
@RequiresPermissions("recommendWork")
public class RecommendWorkController implements IApplicationService {

    @Autowired
    private RecommendWorkService service;

    @Autowired
    private RecommendWorkConverter converter;
    @Autowired
    private AssignmentSubmitService assignmentSubmitService;

    /**
     * @description 添加推荐优秀作品
     * @author 马勇超
     * @date 2025/4/22 11:16
     * @param recommendWorkDto
     */
    @ApiOperation(value = "添加推荐优秀作品")
    @PostMapping(value = "add")
    @RequiresPermissions("recommendWork:add")
    public void add(@RequestBody RecommendWorkDto recommendWorkDto){
        service.insert(converter.dtoToEntity(recommendWorkDto));
    }
    /**
     * @description  通过学生id和作业标题取消推荐
     * @author 马勇超
     * @date 2025/4/22 10:49
     * @param   studentId,title
     */
    @ApiOperation(value = "取消推荐优秀作品")
    @GetMapping(value = "delByStudentIdWithTitle")
    @RequiresPermissions("recommendWork:del")
    public void delByStudentIdWithTitle(@RequestParam("studentId") Long studentId,@RequestParam("title")String title){
        service.delByStudentIdWithTitle(studentId,title);
    }

    /**
    * @description  优秀作品分页展示
    * @author 马勇超
    * @date 2025/4/22 11:26
    * @param   searchDto
    * @return   recommendWorkDto
    */
    @ApiOperation(value = "优秀作品分页展示")
    @PostMapping(value = "recommendWorkPage")
    @RequiresPermissions("recommendWork:page")
    public Page<RecommendWorkDto> recommendWorkPage(@RequestBody CommonSearchDto<RecommendWork> searchDto){
        Map<String,String> map = new HashMap<>();
        map.put("title","a.");
        map.put("recommender_name","a.");
        map.put("creationTime","a.");
        Long userId = ShiroKit.getUserId();
        Page<RecommendWork> page = service.recommendWorkPage(userId,searchDto.getPlusPage(),searchDto.formToEntityWrapper(null,map));
        return converter.pageToDto(page);
    }

    /**
     * @description  删除
     * @author 马勇超
     * @date 2025/5/14 9:41
     * @param   id
     * @return
     */
    @ApiOperation(value = "删除")
    @PostMapping(value = "del")
    @Transactional(rollbackFor = Exception.class)
    @RequiresPermissions("recommendWork:del")
    public void delSingle(@RequestParam("id") Long id){
        RecommendWork recommendWork = service.selectById(id);
        if(null!=recommendWork){
            if(service.deleteById(id)){
                AssignmentSubmit submit = assignmentSubmitService.selectById(recommendWork.getAssignmentSubmitId());
                submit.setRecommend(0);
                if(!assignmentSubmitService.updateById(submit)){
                    throw new FriendException("删除失败！");
                }
            } else {
                throw new FriendException("删除失败！");
            }
        }
    }

    /**
    * @description  批量删除
    * @author 马勇超
    * @date 2025/5/14 9:41
    * @param   ids
    * @return
    */
    @ApiOperation(value = "批量删除")
    @PostMapping(value = "deleteBatches")
    @RequiresPermissions("recommendWork:del")
    public void deleteBatches(@RequestBody List<Long> ids){
        if(!service.deleteBatchIds(ids)){
            throw new FriendException("删除失败！");
        }
    }

    /**
     * 通过编号查询优秀作品
     * @author Juci
     * @param id
     * @return
     */
    @ApiOperation(value = "通过编号查询优秀作品")
    @GetMapping(value = "get")
    public RecommendWorkDto get(@RequestParam("id") Long id){
        return service.getRecommendWorkAndCode(id);
    }
}

