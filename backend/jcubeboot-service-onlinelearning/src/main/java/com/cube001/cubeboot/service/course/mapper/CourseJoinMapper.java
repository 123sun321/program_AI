package com.cube001.cubeboot.service.course.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.course.entity.CourseJoin;
import com.cube001.cubeboot.service.course.dto.CourseDto;
import com.cube001.cubeboot.service.course.dto.CourseJoinDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: CourseJoinMapper
 * 类的详细说明
 *
 * @author 张浩杰
 * @version 1.0
 * @date 2025/3/12 17:52
 */
public interface CourseJoinMapper extends BaseMapper<CourseJoin> {
    /**
     * 搜索分页与查询
     * @param plusPage
     * @param eq
     * @return
     */
    List<CourseJoin> searchPage(Page<CourseJoin> plusPage, @Param("courseId") Long courseId, @Param("ew") Wrapper<CourseJoin> eq);

    /**
     * 获取学生参与的课程分页
     * @author Juci
     * @param page
     * @param ew
     * @return
     */
    List<CourseJoinDto> studentJoinCourse(Page<CourseJoinDto> page,  @Param("ew") Wrapper<CourseJoinDto> ew);

    /**
     * @description  学生参与私有课程的作业分页
     * @author 马勇超
     * @date 2025/4/23 9:51
     * @param   page,wrapper
     * @return
     */
    List<CourseJoinDto> studentJoinPrivateCourseTask(Page page,@Param("ew") Wrapper<CourseJoinDto> eq);

    /**
     * @description  学生参与私有课程无分页
     * @author 马勇超
     * @date 2025/4/23 9:51
     * @param   eq
     * @return
     */
    List<CourseJoinDto> studentJoinPrivateCourses(@Param("ew") Wrapper<CourseJoinDto> eq);
}
