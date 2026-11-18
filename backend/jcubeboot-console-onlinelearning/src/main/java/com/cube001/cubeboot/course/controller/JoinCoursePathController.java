package com.cube001.cubeboot.course.controller;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.course.entity.CourseJoin;
import com.cube001.cubeboot.course.entity.CoursePath;
import com.cube001.cubeboot.course.entity.JoinCoursePath;
import com.cube001.cubeboot.service.base.StudentService;
import com.cube001.cubeboot.service.course.CourseJoinService;
import com.cube001.cubeboot.service.course.CoursePathService;
import com.cube001.cubeboot.service.course.JoinCoursePathService;
import com.cube001.cubeboot.service.course.dto.JoinCoursePathConverter;
import com.cube001.cubeboot.service.course.dto.JoinCoursePathDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 *  Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 *  路径详情控制器
 *  JoinCoursePathController
 * </p>
 *
 * @author qw
 * @since 2019-03-14
 */
@Api(value = "加入路径详情",produces = "加入路径详情")
@RestController
@RequestMapping(value = "${cubeboot.app-manager-path}/joincoursepath")
public class JoinCoursePathController implements IApplicationService {

    @Autowired
    private JoinCoursePathService service;
    @Autowired
    private JoinCoursePathConverter converter;
    @Autowired
    private CoursePathService courseaPathService;
    @Autowired
    private CourseJoinService courseJoinService;
    @Autowired
    private StudentService studentService;

    /**
    * @description  总学习人数
    * @author 马勇超
    * @date 2025/4/18 8:48
    * @param
    * @return
    */
    @ApiOperation(value = "总学习人数",notes = "总学习人数")
    @GetMapping("/joinCount")
    public Integer joinCount(){
        Wrapper<JoinCoursePath> wrapper = new EntityWrapper<>();
        Integer count = service.selectCount(wrapper);
        return count;
    }

    /**
    * @description  加入学习路径
    * @author 马勇超
    * @date 2025/4/23 9:26
    */
    @ApiOperation(value = "加入学习路径")
    @PostMapping(value = "addJoinCoursePath")
    public void addJoinCoursePath(){
        JoinCoursePath joinCoursePath = new JoinCoursePath();
        Long userId = ShiroKit.getUserId();
        Long studentId = studentService.selectOne(new EntityWrapper<Student>().eq("user_id",userId)).getId();
        if(studentId == null){
            throw new FriendException("该用户不是学生！");
        }else {
            joinCoursePath.setStudentId(studentId);
            int count = courseJoinService.selectCount(new EntityWrapper<CourseJoin>().eq("student_id", studentId));
            joinCoursePath.setStudyNumber(count);
            JoinCoursePath coursePath = service.selectOne(new EntityWrapper<JoinCoursePath>().eq("student_id",studentId));
            if(null == coursePath){
                service.insert(joinCoursePath);
            }else {
                throw new FriendException("路径已添加，添加失败！");
            }
        }
    }

    /**
    * @description  判断该用户是否已加入路径
    * @author 马勇超
    * @date 2025/4/23 9:26
    * @return   Boolean
    */
    @ApiOperation(value = "判断该用户是否已加入路径")
    @GetMapping(value = "isJoin")
    public Boolean isJion(){
        Long userId = ShiroKit.getUserId();
        Boolean isExistence = false;
        Long studentId = studentService.selectOne(new EntityWrapper<Student>().eq("user_id",userId)).getId();
        if(studentId == null){
            throw new FriendException("该用户不是学生！");
        }else{
            JoinCoursePath joinCoursePath = service.selectOne(new EntityWrapper<JoinCoursePath>().eq("student_id",studentId));
            if(joinCoursePath != null)
            {isExistence = true;}else{
                isExistence = false;
            }
        }
        return isExistence;
    }

    /**
     * 分页展示搜索
     * @author qw
     * @param searchDto
     * @return converter.pageEntityToDto(page)
     */
    @ApiOperation(value = "搜索",notes = "搜索")
    @PostMapping("/findpage")
    public Page<JoinCoursePathDto> findPage(@RequestBody CommonSearchDto<JoinCoursePathDto> searchDto){
        HashMap<String,String> map = new HashMap<>();
        map.put("year","tg.");
        map.put("grade_id","ts.");
        Wrapper<JoinCoursePathDto> joinWrapper = searchDto.formToEntityWrapperWithSearch(new String[]{"code","name"},"ts.",map);
        Page<JoinCoursePathDto> page = service.page(searchDto.getPlusPage(),joinWrapper);
        return converter.pageEntityToDto(page);
    }
    /**
     * 学习进度
     * @author qw
     * @return count;
    */
    @ApiOperation(value = "学习进度",notes = "学习进度")
    @GetMapping("/studyschedule")
    public Integer studyschedule(){
        Wrapper<CoursePath> wrapper = new EntityWrapper<>();
        Integer count = courseaPathService.selectCount(wrapper);
        return count;
    }
}
