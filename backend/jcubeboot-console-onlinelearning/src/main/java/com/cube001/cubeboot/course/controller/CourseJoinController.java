package com.cube001.cubeboot.course.controller;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.assignment.entity.Assignment;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.course.entity.CourseJoin;
import com.cube001.cubeboot.service.assignment.dto.AssignmentConverter;
import com.cube001.cubeboot.service.assignment.dto.AssignmentDto;
import com.cube001.cubeboot.service.base.dto.BaseConverter;
import com.cube001.cubeboot.service.course.CourseJoinService;
import com.cube001.cubeboot.service.course.dto.CourseJoinDto;
import com.cube001.cubeboot.vo.course.CommonSearchExtendUserIdVo;
import com.cube001.cubeboot.vo.course.CourseJoinPageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
* FileName: CourseParticipationDetailsController
* 课程参与详情controller
*
* @author anonymous
* @date   2019/3/12 16:36
* @version 1.0
*/
@Api(value = "CourseJoinController",description = "课程参与详情Controller")
@RestController
@RequestMapping(value = "${cubeboot.app-manager-path}/courseJoin")
public class CourseJoinController implements IApplicationService {
    @Autowired
    private CourseJoinService courseJoinService;
    @Autowired
    private BaseConverter baseConverter;
    
    /**  
     * @description 搜索/分页接口：用已分页数据和根据学生姓名实现模糊查询
     * @author anonymous 
     * @date   2019年3月13日 11:15:35
     * @param  
     * @return page
    */ 
    @ApiOperation(value = "搜索/分页",notes = "搜索/分页")
    @RequestMapping(value = "/page",method = RequestMethod.POST)
    @ResponseBody
    public Page<CourseJoinDto> page(@RequestBody CourseJoinPageVo commonSearchDto){
        Map<String,String> map = new HashMap<>();
        // 根据学生姓名
        map.put("name","tbs.");
        map.put("grade_id","tbs.");
        map.put("course_id","tcpd.");
        map.put("course_group_id","tcpd.");
        Page<CourseJoin> courseParticipationDetailsPage = courseJoinService.searchPage
                (commonSearchDto.getPlusPage(),commonSearchDto.getCourseId(),commonSearchDto.formToEntityWrapper(null,map));
        return baseConverter.courseParticipationDetailsDtoPage(courseParticipationDetailsPage);
    }

    /**
     * 获取学生参与的课程分页
     * @author anonymous
     * @param commonSearchExtendUserIdVo
     * @return
     */
    @ApiOperation(value = "学生参与课程",notes = "学生参与课程")
    @RequestMapping(value = "/studentJoinCourse",method = RequestMethod.POST)
    @ResponseBody
    public Page<CourseJoinDto> studentJoinCourse(@RequestBody CommonSearchExtendUserIdVo<CourseJoinDto> commonSearchExtendUserIdVo){
        if(commonSearchExtendUserIdVo.getUserId()==null){
            commonSearchExtendUserIdVo.setUserId(ShiroKit.getUserId());
        }
        Page page = commonSearchExtendUserIdVo.getPlusPage();
        Wrapper<CourseJoinDto> wrapper = commonSearchExtendUserIdVo.formToEntityWrapper(null,null);
        wrapper.eq("c.user_id",commonSearchExtendUserIdVo.getUserId());
        Page<CourseJoinDto> courseJoinDtoPage = courseJoinService.studentJoinCourse(page,wrapper);
        return courseJoinDtoPage;
    }

    /**
     * @description  学生参与私有课程无分页
     * @author anonymous
     * @date 2025/4/23 9:51
     * @param   commonSearchExtendUserIdVo
     * @return
     */
    @ApiOperation(value = "学生参与私有课程",notes = "学生参与私有课程")
    @RequestMapping(value = "/studentJoinPrivateCourse",method = RequestMethod.POST)
    @ResponseBody
    public List<CourseJoinDto> studentJoinPrivateCourse(@RequestBody CommonSearchExtendUserIdVo<CourseJoinDto> commonSearchExtendUserIdVo){
        if(commonSearchExtendUserIdVo.getUserId()==null){
            commonSearchExtendUserIdVo.setUserId(ShiroKit.getUserId());
        }
        Wrapper<CourseJoinDto> wrapper = commonSearchExtendUserIdVo.formToEntityWrapper(null,null);
        wrapper.eq("c.user_id",commonSearchExtendUserIdVo.getUserId());
        // 显示学生加入的私有课
        wrapper.eq("b.type",1);
        List<CourseJoinDto> courseJoinDtoList = courseJoinService.studentJoinPrivateCourses(wrapper);
        return courseJoinDtoList;
    }

    /**
    * @description  学生参与私有课程的作业分页
    * @author anonymous
    * @date 2025/4/23 9:51
    * @param   commonSearchExtendUserIdVo
    * @return
    */
    @ApiOperation(value = "学生参与私有课程的作业",notes = "学生参与私有课程的作业")
    @RequestMapping(value = "/studentJoinPrivateCourseTask",method = RequestMethod.POST)
    @ResponseBody
    public Page<AssignmentDto> studentJoinPrivateCourseTask(@RequestBody CommonSearchExtendUserIdVo<CourseJoinDto> commonSearchExtendUserIdVo){
        if(commonSearchExtendUserIdVo.getUserId()==null){
            commonSearchExtendUserIdVo.setUserId(ShiroKit.getUserId());
        }
        Page page = commonSearchExtendUserIdVo.getPlusPage();
        Wrapper<CourseJoinDto> wrapper = commonSearchExtendUserIdVo.formToEntityWrapperWithSearch(new String[]{"course_id"},"a.");
        wrapper.eq("c.user_id",commonSearchExtendUserIdVo.getUserId());
        // 显示学生加入的私有课
        wrapper.eq("b.type",1);
        Page<AssignmentDto> assignmentDtoPage = courseJoinService.studentJoinPrivateCourseTask(page,wrapper);
        // 判断作业是否已经提交
        return assignmentDtoPage;
    }


    /**
     * 通过邀请码加入私有课程
     * @author anonymous
     * @param code 邀请码
     */
    @ApiOperation(value = "通过邀请码加入私有课程")
    @GetMapping(value = "/joincourse")
    public void joinCourse(@RequestParam("code") String code){
        Long userId = ShiroKit.getUserId();
        courseJoinService.joinCourse(userId,code);
    }

    /**
     * 批量添加课程小组学生
     * @Author anonymous
     * @param ids
     * @param groupid
     */
    @ApiOperation(value = "批量添加课程小组学生")
    @GetMapping(value = "/join")
    public void joinGroup(@RequestParam("ids")String ids,@RequestParam("groupid") Long groupid){
        String[] idList = ids.split(",");
        for (String strId : idList) {
            Long id  = Long.parseLong(strId);
            CourseJoin courseJoin = this.courseJoinService.selectById(id);
            courseJoin.setCourseGroupId(groupid);
            this.courseJoinService.updateById(courseJoin);
        }
    }
}
