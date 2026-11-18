package com.cube001.cubeboot.service.course.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.course.entity.JoinCoursePath;
import com.cube001.cubeboot.service.course.dto.JoinCoursePathDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * <p>
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: JoinCoursePathMapper.java
 * 路径详情Mapper 接口
 * </p>
 *
 * @author anonymous
 * @since 2019-03-14
 */
public interface JoinCoursePathMapper extends BaseMapper<JoinCoursePath> {


    /**
     * 分页展示查询
     * @author anonymous
     * @date 2025/3/19 10:19
    */
    List<JoinCoursePathDto> page(Pagination page, @Param("ew") Wrapper<JoinCoursePathDto> wrapper);
}
