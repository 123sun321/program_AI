package com.cube001.cubeboot.service.course;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.cube001.cubeboot.assignment.entity.Assignment;
import com.cube001.cubeboot.course.entity.CourseJoin;
import com.cube001.cubeboot.service.assignment.dto.AssignmentDto;
import com.cube001.cubeboot.service.course.dto.CourseJoinDto;

import java.util.List;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: CourseJoinService
 * 课程参与详情接口
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/3/12 17:44
 */
public interface CourseJoinService extends IService<CourseJoin> {

    /**
     * @description 课程参与详情分页列表
     * @author anonymous
     * @date 2025年3月13日 13:06:47
     * @param
     * @return
    */
    Page<CourseJoin> searchPage(Page<CourseJoin> plusPage,Long courseId, Wrapper<CourseJoin> formToEntityWrapper);

    /**
     * 获取学生参与的课程分页
     * @author anonymous
     * @param plusPage
     * @param wrapper
     * @return
     */
    Page<CourseJoinDto> studentJoinCourse(Page<CourseJoinDto> plusPage, Wrapper<CourseJoinDto> wrapper);

    /**
     * 添加参与课程记录
     * @author anonymous
     * @param userId 用户编号
     * @param courseId 课程编号
     */
    void add(Long userId,Long courseId);

    /**
     * 通过邀请码参与课程学习
     * @auther Juci
     * @param userid 用户编号
     * @param invitationCode 邀请码
     */
    void joinCourse(Long userid,String invitationCode);

    /**
     * @description  学生参与私有课程的作业分页
     * @author anonymous
     * @date 2025/4/23 9:51
     * @param   page,wrapper
     * @return
     */
    Page<AssignmentDto> studentJoinPrivateCourseTask(Page page, Wrapper<CourseJoinDto> wrapper);

    /**
     * @description  学生参与私有课程无分页
     * @author anonymous
     * @date 2025/4/23 9:51
     * @param   wrapper
     * @return
     */
    List<CourseJoinDto> studentJoinPrivateCourses(Wrapper<CourseJoinDto> wrapper);
}
