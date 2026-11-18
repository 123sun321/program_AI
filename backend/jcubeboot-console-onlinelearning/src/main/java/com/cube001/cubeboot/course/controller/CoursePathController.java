package com.cube001.cubeboot.course.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.domain.IApplicationService;

import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.course.entity.CoursePath;
import com.cube001.cubeboot.service.base.StudentService;
import com.cube001.cubeboot.service.course.CoursePathService;
import com.cube001.cubeboot.service.course.CourseService;
import com.cube001.cubeboot.service.course.dto.CourseConverter;
import com.cube001.cubeboot.service.course.dto.CoursePathConverter;
import com.cube001.cubeboot.service.course.dto.CoursePathDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 *  路径设置控制器
 *  CoursePathController
 * </p>
 *
 * @author anonymous
 * @since 2019-03-11
 */
@Api(value = "路径设置",produces = "路径设置")
@RestController
@RequestMapping(value = "${cubeboot.app-manager-path}/coursepath")
public class CoursePathController implements IApplicationService {

    @Autowired
    private CoursePathService service;
    @Autowired
    private CoursePathConverter coursePathConverter;
    @Autowired
    private StudentService studentService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseConverter courseConverter;

    /**
     * 删除
     * @author anonymous
     * @param id
     * @return res
    */
    @ApiOperation(value = "删除",notes = "删除")
    @GetMapping("/coursePathDel")
    public Boolean del(@RequestParam("id") Long id){
       Boolean res = service.deleteById(id);
       return res;
    }
    /**
    * @description  路径批量添加
    * @author anonymous
    * @date 2025/4/3 16:10
    * @param   courseIds,category
    */
    @ApiOperation(value = "路径批量添加",notes = "路径批量添加")
    @PostMapping("/coursePathMultipleAdd")
    public void multipleAdd(@Validated @RequestParam("courseIds[]") List<Long> courseIds,@RequestParam("category") String category){
        service.multipleAdd(courseIds,category);
    }
    /**
     * 移位
     * @author anonymous
     * @param id1,id2
    */
    @ApiOperation(value = "路径上下移位",notes = "路径上下移位")
    @GetMapping("/coursePathUpAndDown")
    public void upAndDown(@RequestParam("id1") Long id1,@RequestParam("id2") Long id2) {
        CoursePath path1 = service.selectById(id1);
        CoursePath path2 = service.selectById(id2);
        Integer sort1 = path1.getSort();
        Integer sort2 = path2.getSort();
        path1.setSort(sort2);
        path2.setSort(sort1);
        service.updateById(path1);
        service.updateById(path2);
    }
    /**
     * 展示所有
     * @author anonymous
     * @return list
    */
    @PostMapping("/coursePathAll")
    @ApiOperation(value = "展示所有",notes = "展示所有")
    public List<CoursePathDto> showAll(@RequestBody CommonSearchDto<CoursePath> searchDto){
       List<CoursePath> pathList = service.getAll(searchDto.formToEntityWrapperWithSearch(new String[]{"category","complexity"},"tc."));
       return coursePathConverter.listToDto(pathList);
    }

    /**
    * @description  获取该学生路径课程的参与情况
    * @author anonymous
    * @date 2025/5/14 14:57
    * @return
    */
    @ApiOperation(value = "获取该学生路径课程的参与情况")
    @PostMapping(value = "getJoinDetails")
    public List<CoursePathDto> getJoinDetails(){
        Long userId = ShiroKit.getUserId();
        Student student = studentService.selectOne(new EntityWrapper<Student>().eq("user_id",userId));
        if(null!=student){
            List<CoursePath> coursePathList = service.getJoinDetails(student.getId());
            return coursePathConverter.listToDto(coursePathList);
        }else {
            throw new FriendException("该用户不是学生！");
        }
    }

    /**
    * @description  路径详情分页
    * @author anonymous
    * @date 2025/4/8 9:39
    * @param   searchDto
    * @return   pageDto
    */
    @ApiOperation(value = "路径详情分页")
    @PostMapping(value = "coursePathPage")
    public Page<CoursePathDto> coursePathPage(@RequestBody CommonSearchDto<CoursePath> searchDto){
        HashMap<String,String> map = new HashMap<>();
        // 课程类别
        map.put("category","tc.");
        // 难易程度
        map.put("complexity","tc.");
        Page<CoursePath> page = service.selectWithCoursePage(searchDto.getPlusPage(),searchDto.formToEntityWrapper(null,map));
        return coursePathConverter.pageToDto(page);
    }

}
