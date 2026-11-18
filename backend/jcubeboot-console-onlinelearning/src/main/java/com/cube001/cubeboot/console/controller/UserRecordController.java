package com.cube001.cubeboot.console.controller;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.service.base.StudentService;
import com.cube001.cubeboot.service.base.UserRecordService;
import com.cube001.cubeboot.service.base.dto.UserRecordDto;
import com.cube001.cubeboot.vo.course.CommonSearchExtendUserIdVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: UserRecordController
 * 用户浏览记录控制器
 *
 * @author Juci
 * @version 1.0
 * @date 2025/4/22 14:09
 */
@Api(value = "UserRecordController", description = "用户浏览记录控制器")
@RestController
@RequestMapping(value = "${cubeboot.app-manager-path}/userrecord")
public class UserRecordController implements IApplicationService {

    @Autowired
    private UserRecordService userRecordService;
    @Autowired
    private StudentService studentService;

    /**
     * 用户浏览记录分页
     * @author Juci
     * @param searchDto
     * @return
     */
    @ApiOperation(value = "用户浏览记录分页")
    @RequestMapping(value = "page", method = RequestMethod.POST)
    public Page<UserRecordDto> page(@RequestBody CommonSearchExtendUserIdVo<UserRecordDto> searchDto) {
        if(searchDto.getUserId()==null){
            throw new FriendException("用户编号为空！");
        }
        Page<UserRecordDto> page = searchDto.getPlusPage();
        Wrapper<UserRecordDto> wrapper = searchDto.formToEntityWrapper(null);
        wrapper.eq("are_visitor",searchDto.getUserId());
        return userRecordService.searchPage(page, wrapper);
    }
    /**
    * @description  用户浏览记录分页通过学生Id
    * @author 马勇超
    * @date 2025/4/29 9:28
    * @param   searchDto
    * @return
    */
    @ApiOperation(value = "用户浏览记录分页通过学生Id")
    @RequestMapping(value = "pageByStudentId", method = RequestMethod.POST)
    public Page<UserRecordDto> pageByStudentId(@RequestParam(value = "studentId",required = false)Long studentId, @RequestBody CommonSearchExtendUserIdVo<UserRecordDto> searchDto) {
//        if(searchDto.getUserId()==null){
//            throw new FriendException("用户编号为空！");
//        }
        Student student = studentService.selectById(studentId);
        Page<UserRecordDto> page = searchDto.getPlusPage();
        Wrapper<UserRecordDto> wrapper = searchDto.formToEntityWrapper(null);
        wrapper.eq("are_visitor",student.getUserId());
        return userRecordService.searchPage(page, wrapper);
    }
}
