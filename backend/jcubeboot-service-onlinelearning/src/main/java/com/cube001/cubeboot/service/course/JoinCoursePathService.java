package com.cube001.cubeboot.service.course;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.cube001.cubeboot.course.entity.JoinCoursePath;
import com.cube001.cubeboot.service.course.dto.JoinCoursePathDto;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * <p>
 *  Copyright (C), 2025 - 2026, zxy.
 *  JoinCoursePathService
 *  路径详情服务类
 * </p>
 *
 * @author qw
 * @since 2019-03-14
 */
public interface JoinCoursePathService extends IService<JoinCoursePath> {

    /**
     * 分页展示查询
     * @author qw
     * @date 2025/3/19 10:19
     */
    Page<JoinCoursePathDto> page(Page<JoinCoursePathDto> page, Wrapper<JoinCoursePathDto> formToEntityWrapperWithSearch);

}
