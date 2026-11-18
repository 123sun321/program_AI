package com.cube001.cubeboot.service.course;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.cube001.cubeboot.course.entity.CoursePath;
import org.apache.poi.ss.formula.functions.T;
import java.util.List;


/**
 * <p>
 *  Copyright (C), 2025 - 2026, zxy.
 *  CoursePathService
 *  路径设置服务类
 * </p>
 *
 * @author qw
 * @since 2019-03-11
 */
public interface CoursePathService extends IService<CoursePath> {

    /**
     * 展示所有添加的课程
     * @author qw
     * @date 2025年3月15日 10:06:10
    */
    List<T> findByAll();

    /**
    * @description  路径的批量添加
    * @author 马勇超
    * @date 2025/4/3 16:07
    * @param   courseIds,category
    */
    void multipleAdd(List<Long> courseIds, String category);

    /**
    * @description  路径分页
    * @author 马勇超
    * @date 2025/4/8 9:19
    * @param   page,wrapper
    * @return   page
    */
    Page<CoursePath> selectWithCoursePage(Page<CoursePath> page, Wrapper<CoursePath> wrapper);

    /**
    * @description  获取路径详情列表
    * @author 马勇超
    * @date 2025/4/8 10:14
    * @param   wrapper
    * @return   list
    */
    List<CoursePath> getAll(Wrapper<CoursePath> wrapper);
    /**
     * @description  获取该学生路径课程的参与情况
     * @author 马勇超
     * @date 2025/5/14 14:57
     * @param   studentId
     * @return
     */
    List<CoursePath> getJoinDetails(Long studentId);
}
