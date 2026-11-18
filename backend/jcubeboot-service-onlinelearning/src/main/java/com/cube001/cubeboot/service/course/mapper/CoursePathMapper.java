package com.cube001.cubeboot.service.course.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.course.entity.CoursePath;
import com.cube001.cubeboot.service.course.dto.CoursePathDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * <p>
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: CoursePathMapper.java
 * 路径设置Mapper 接口
 * </p>
 *
 * @author qw
 * @since 2019-03-11
 */
public interface CoursePathMapper extends BaseMapper<CoursePath> {

    /**
     * 查询所有
     * @author qw
    */
    List<T> findAll();

    /**
     * @description  路径分页
     * @author 马勇超
     * @date 2025/4/8 9:19
     * @param   page,wrapper
     * @return   page
     */
    List<CoursePath> selectWithCoursePage(Page<CoursePath> page,@Param("ew") Wrapper<CoursePath> wrapper);

    /**
     * @description  获取路径详情列表
     * @author 马勇超
     * @date 2025/4/8 10:14
     * @param   wrapper
     * @return   list
     */
    List<CoursePath> getAll(@Param("ew") Wrapper<CoursePath> wrapper);

    /**
     * @description  获取该学生路径课程的参与情况
     * @author 马勇超
     * @date 2025/5/14 14:57
     * @param   studentId
     * @return
     */
    List<CoursePath> getJoinDetails(@Param("studentId") Long studentId);

    /**
      * @description 获取课程路径信息和课程名称
      * @author 马勇超
      * @date 2025/8/8 14:23
      * @param
      * @return
     */
    List<CoursePath> selectWithCourseName();
}
