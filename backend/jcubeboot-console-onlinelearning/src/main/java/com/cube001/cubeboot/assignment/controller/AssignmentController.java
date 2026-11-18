package com.cube001.cubeboot.assignment.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.assignment.entity.Assignment;

import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.authorization.shiro.ShiroUser;
import com.cube001.cubeboot.core.domain.IApplicationService;

import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.core.validateGroup.EditChecks;
import com.cube001.cubeboot.service.assignment.AssignmentService;
import com.cube001.cubeboot.service.assignment.AssignmentSubmitService;
import com.cube001.cubeboot.service.assignment.dto.AssignmentConverter;
import com.cube001.cubeboot.service.assignment.dto.AssignmentDto;

import com.cube001.cubeboot.service.assignment.dto.ListAssignmentDto;
import com.cube001.cubeboot.vo.course.CommonSearchExtendVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* Copyright (C), 2018 - 2019, ZhengZho uChuangZhi. Co., Ltd
* FileName:
* 作业类展示页面：
 *
 *
*
* @author 张人杰
* @date   2019/3/21 8:52
* @version 1.0
*/


@Api(value = "assignment controller ",description = "作业 controller")
@RestController
@RequestMapping(value = "${cubeboot.app-manager-path}/assignment")
@RequiresPermissions("assignment")
public class AssignmentController implements  IApplicationService {

    @Autowired
   private AssignmentService assignmentService;
    @Autowired
   private AssignmentConverter assignmentConverter;
   /*
     * @description
     * @author 张人杰
     * @date 2025/3/21 8:49
     * @Param   assignmentid
     * @return 友好异常成功或者失败
     */

    @ApiOperation(value = "删除",notes = "根据作业ID删除内容")
    @RequestMapping(value = "/del",method = RequestMethod.GET)
    @RequiresPermissions("assignment:del")
    public void del(@RequestParam("id")  Long id){
            assignmentService.delete(id);
    }
    @ApiOperation(value = "批量删除",notes = "根据作业ID批量删除内容")
    @RequestMapping(value = "/delBatch",method = RequestMethod.POST)
    // @RequiresPermissions("assignment:delete")
    public Boolean delBatch(@RequestBody List<Long> ids){
          return   assignmentService.deleteBatchids(ids);
    }

    @ApiOperation(value = "分页展示",notes = "作业的分页展示")
    @RequestMapping(value = "page", method = RequestMethod.POST)
    @RequiresPermissions("assignment:page")
    public Page<ListAssignmentDto> page(@RequestBody CommonSearchDto<Assignment> searchDto) {
        Map<String,String> map = new HashMap<>();
        //根据学年查询
        map.put("year","bg.");
        //根据班级名称查询
        map.put("name","bg.");
        //根据课程查询
        map.put("name","bc.");
        //根据作业标题查询
        map.put("title","am.");
        map.put("creationTime","am.");
        //根据作业类型查询
        map.put("team_category","am.");
        Page<Assignment>    page = assignmentService.getStuRewardPunishList( searchDto.getPlusPage(), searchDto.formToEntityWrapperWithSearch(new String[]{"course_id"},"am.",map));
        return assignmentConverter.pageEntityToDto(page) ;
    }
    @ApiOperation(value = "添加",notes = "添加一条作业")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @RequiresPermissions("assignment:add")
    public void add(@RequestBody @Validated AssignmentDto assignmentDto) {
      try {
          assignmentService.add(assignmentDto);
      }catch (Exception E){
          throw  new FriendException("添加失败");
      }
    }
    @ApiOperation(value = "编辑",notes = "作业编辑")
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @RequiresPermissions("assignment:edit")
    public void edit(@RequestBody @Validated({Default.class, EditChecks.class}) AssignmentDto assignmentDto) {

         assignmentService.update(assignmentDto);
    }
    @ApiOperation(value = "验证",notes = "查询排序是否重复")
    @RequestMapping(value = "validate", method = RequestMethod.POST)
    @RequiresPermissions("assignment:validate")
    public  boolean getsortid(Long id,@RequestParam("col") String col,@RequestParam("value") String value, Long courseid){
        if (id==null){
            //如果编号id不存在
            Wrapper<Assignment> assignmentWrapper =new EntityWrapper<>();
            assignmentWrapper.eq(col,value).eq("course_id",courseid);
            int assignmentSort = assignmentService.selectCount(assignmentWrapper);
            if (assignmentSort>0){
                return false;
            }

        }else {
            //如果编号id存在
            Wrapper<Assignment> assignmentWrapper =new EntityWrapper<>();
            assignmentWrapper.eq(col,value).notIn("id",id).eq("course_id",courseid);
            int i = assignmentService.selectCount(assignmentWrapper);
            if (i>0){
                return false;
            }
        }
    return  true;
    }
    @ApiOperation(value = "验证",notes = "课程新增作业字段验证")
    @RequestMapping(value = "assignmentValidate", method = RequestMethod.POST)
    @RequiresPermissions("assignment:validate")
    public  boolean validate(Long id,@RequestParam("col") String col,@RequestParam("value") String value,Long courseid){
        if (id==null){
            //如果编号id不存在
            Wrapper<Assignment> assignmentWrapper =new EntityWrapper<>();
            assignmentWrapper.eq(col,value).eq("course_id",courseid);
            int i = assignmentService.selectCount(assignmentWrapper);
            if (i>0){
                return false;
            }

        }else {
            //如果编号id存在
            Wrapper<Assignment> assignmentWrapper =new EntityWrapper<>();
            assignmentWrapper.eq(col,value).notIn("id",id).eq("course_id",courseid);
            int i = assignmentService.selectCount(assignmentWrapper);
            if (i>0){
                return false;
            }
        }
        return  true;
    }

    @ApiOperation(value = "查询",notes = "查询作业ID")
    @RequestMapping(value = "getAssignmentID", method = RequestMethod.GET)
    @RequiresPermissions("assignment:get")
    public AssignmentDto getAssignmentID(@RequestParam("id") Long assignmentID){
        Assignment assignment = assignmentService.selectById(assignmentID);
        return   assignmentConverter.entityToDto(assignment);
    }

    /**
     * 查询学生作业详情getStudentAssignment
     * @author Juci
     * @param id
     * @return
     */
    @ApiOperation(value = "查询学生作业详情",notes = "查询学生作业详情")
    @RequestMapping(value = "getStudentAssignment", method = RequestMethod.GET)
    @RequiresPermissions("assignment:get")
    public AssignmentDto getStudentAssignment(@RequestParam("id") Long id){
        return this.assignmentService.getStudentAssignment(ShiroKit.getUserId(),id);
    }

    /**
     * 分页获取学生班级作业
     * @Author Juci
     * @param searchDto
     * @return
     */
    @ApiOperation(value = "分页获取学生班级作业")
    @RequestMapping(value = "gradPage", method = RequestMethod.POST)
    public Page<AssignmentDto> gradPage(@RequestBody CommonSearchExtendVo<AssignmentDto> searchDto) {
        Long userId = ShiroKit.getUserId();
        Page<AssignmentDto> page = searchDto.getPlusPage();
        HashMap<String,String> map = new HashMap<>();
        map.put("course_id","g.");
        Wrapper<AssignmentDto> wrapper = searchDto.formToEntityWrapperWithSearch(new String[]{"title"},"g.",map);
        //已提交
        if(searchDto.getStatus()==1){
            wrapper.isNotNull("f.id");
        }
        //未提交
        if(searchDto.getStatus()==-1){
            wrapper.isNull("f.id");
        }
        return this.assignmentService.searchGradPage(page, wrapper,userId);
    }

    /**
     * 分页获取学生小组作业
     * @Author Juci
     * @param searchDto
     * @return
     */
    @ApiOperation(value = "分页获取学生小组作业")
    @RequestMapping(value = "groupPage", method = RequestMethod.POST)
    public Page<AssignmentDto> groupPage(@RequestBody CommonSearchExtendVo<AssignmentDto> searchDto) {
        Long userId = ShiroKit.getUserId();
        Page<AssignmentDto> page = searchDto.getPlusPage();
        HashMap<String,String> map = new HashMap<>();
        map.put("course_id","g.");
        Wrapper<AssignmentDto> wrapper = searchDto.formToEntityWrapperWithSearch(new String[]{"title"},"g.",map);
        //已提交
        if(searchDto.getStatus()==1){
            wrapper.isNotNull("f.id");
        }
        //未提交
        if(searchDto.getStatus()==-1){
            wrapper.isNull("f.id");
        }
        return this.assignmentService.searchGroupPage(page, wrapper,userId);
    }
}
