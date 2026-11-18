package com.cube001.cubeboot.service.course.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.core.domain.entity.Entity;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.course.entity.CoursePath;
import com.cube001.cubeboot.service.course.CoursePathService;
import com.cube001.cubeboot.service.course.dto.CoursePathConverter;
import com.cube001.cubeboot.service.course.dto.CoursePathDto;
import com.cube001.cubeboot.service.course.mapper.CoursePathMapper;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * <p>
 *  Copyright (C), 2025 - 2026, zxy.
 *  CoursePathServiceImpl
 *  路径设置服务实现类
 * </p>
 *
 * @author anonymous
 * @since 2019-03-11
 */
@Service
public class CoursePathServiceImpl extends ServiceImpl<CoursePathMapper, CoursePath> implements CoursePathService {


    @Autowired
    private CoursePathMapper dao;
    @Autowired
    private CoursePathService service;
    @Autowired
    private CoursePathConverter coursePathConverter;

    /**
     * 展示所有添加的课程
     * @author anonymous
     * @date 2025年3月15日 10:06:10
     */
    @Override
    public List<T> findByAll() {
        List list = dao.findAll();
        return list;
    }

    /**
     * @description  路径的批量添加
     * @author anonymous
     * @date 2025/4/3 16:07
     * @param   courseIds,category
     */
    @Override
    public void multipleAdd(List<Long> courseIds,String category) {
        Wrapper<CoursePath> wrapper = new EntityWrapper<>();
        wrapper.eq("category",category);
        wrapper.orderBy("sort desc");
        if(baseMapper.selectList(new EntityWrapper<CoursePath>().in("course_id",courseIds)).size() > 0 ){
            throw new FriendException("某些课程已存在,请重新添加！");
        }else{
            List<CoursePath> coursePathList = baseMapper.selectList(wrapper);
            Integer startSort = 0;
            if(coursePathList.size() > 0){
                startSort = coursePathList.get(0).getSort()+1;
            }else{
                startSort = 1;
            }
            for (Long id:courseIds) {
                CoursePath coursePath = new CoursePath();
                coursePath.setCourseId(id);
                coursePath.setSort(startSort);
                coursePath.setCategory(category);
                this.insert(coursePath);
                startSort++;
            }
        }
    }

    /**
     * @description  路径分页
     * @author anonymous
     * @date 2025/4/8 9:19
     * @param   page,wrapper
     * @return   page
     */
    @Override
    public Page<CoursePath> selectWithCoursePage(Page<CoursePath> page, Wrapper<CoursePath> wrapper) {
        List<CoursePath> coursePathList = baseMapper.selectWithCoursePage(page,wrapper.eq("tp.is_deleted",0).orderBy("sort"));
        page.setRecords(coursePathList);
        return page;
    }

    /**
     * @description  获取路径详情列表
     * @author anonymous
     * @date 2025/4/8 10:14
     * @param   wrapper
     * @return   list
     */
    @Override
    public List<CoursePath> getAll(Wrapper<CoursePath> wrapper) {
        List<CoursePath> list = baseMapper.getAll(wrapper.eq("tp.is_deleted",0)
                .eq("tc.type",0)
                .orderBy("tp.sort"));
        return list;
    }

    /**
     * @description  获取该学生路径课程的参与情况
     * @author anonymous
     * @date 2025/5/14 14:57
     * @param   studentId
     * @return
     */
    @Override
    public List<CoursePath> getJoinDetails(Long studentId) {
        return this.baseMapper.getJoinDetails(studentId);
    }
}
