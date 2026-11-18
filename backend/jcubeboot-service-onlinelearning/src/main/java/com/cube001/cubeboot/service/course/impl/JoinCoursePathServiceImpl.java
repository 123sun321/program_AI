package com.cube001.cubeboot.service.course.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.course.entity.*;
import com.cube001.cubeboot.service.course.*;
import com.cube001.cubeboot.service.course.dto.JoinCoursePathDto;
import com.cube001.cubeboot.service.course.mapper.JoinCoursePathMapper;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  Copyright (C), 2025 - 2026, zxy.
 *  JoinCoursePathServiceImpl
 *  路径详情服务实现类
 * </p>
 *
 * @author qw
 * @since 2019-03-14
 */
@Service
public class JoinCoursePathServiceImpl extends ServiceImpl<JoinCoursePathMapper, JoinCoursePath> implements JoinCoursePathService {

    @Autowired
    private JoinCoursePathMapper dao;
    @Autowired
    private CourseJoinService courseJoinService;
    @Autowired
    private CoursePathService coursePathService;
    @Autowired
    private ExerciseService exerciseService;
    @Autowired
    private ExerciseJoinService exerciseJoinService;
    /**
     * 分页展示查询
     * @author qw
     * @date 2025/3/19 10:19
     */
    @Override
    public Page<JoinCoursePathDto> page(Page<JoinCoursePathDto> page, Wrapper<JoinCoursePathDto> wrapper) {
        List<JoinCoursePathDto> joinCoursePathList = dao.page(page,wrapper.eq("t.is_deleted","0"));
        for (JoinCoursePathDto joinCoursePathDto:joinCoursePathList){
            int count = 0;
            // 获取学生参与的所有课程
            List<CourseJoin> joinList = courseJoinService.selectList(new EntityWrapper<CourseJoin>().eq("student_id",joinCoursePathDto.getStudentId()));
            // 获取所有的课程id
            List<Long> ids = joinList.stream().map(m->m.getCourseId()).collect(Collectors.toList());
            Wrapper<CoursePath> wrapper1 = new EntityWrapper<>();
            wrapper1.in("course_id",ids);
            // 查询出在路径之中的课程
            List<CoursePath> coursePathList = coursePathService.selectList(wrapper1);
            for (CoursePath coursePath:coursePathList) {
                List<Exercise> exerciseList = exerciseService.selectList(new EntityWrapper<Exercise>().eq("course_id",coursePath.getCourseId()));
                List<Long> exerciseIds = exerciseList.stream().map(m->m.getId()).collect(Collectors.toList());
                int exerciseJoinCount = exerciseJoinService.selectCount(new EntityWrapper<ExerciseJoin>().in("exercise_id",exerciseIds).eq("student_id",joinCoursePathDto.getStudentId()));
                if(exerciseList.size() == exerciseJoinCount){
                    count++;
                }
            }
            joinCoursePathDto.setFinishNumber(count);
        }
        page.setRecords(joinCoursePathList);
        return page;
    }

}
