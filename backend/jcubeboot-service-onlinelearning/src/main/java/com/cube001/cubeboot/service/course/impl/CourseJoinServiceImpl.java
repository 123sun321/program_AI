package com.cube001.cubeboot.service.course.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.assignment.entity.Assignment;
import com.cube001.cubeboot.assignment.entity.AssignmentSubmit;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.course.entity.Course;
import com.cube001.cubeboot.course.entity.CourseJoin;
import com.cube001.cubeboot.service.assignment.AssignmentService;
import com.cube001.cubeboot.service.assignment.AssignmentSubmitService;
import com.cube001.cubeboot.service.assignment.dto.AssignmentConverter;
import com.cube001.cubeboot.service.assignment.dto.AssignmentDto;
import com.cube001.cubeboot.service.base.StudentService;
import com.cube001.cubeboot.service.base.mapper.StudentMapper;
import com.cube001.cubeboot.service.course.CourseJoinService;
import com.cube001.cubeboot.service.course.dto.CourseDto;
import com.cube001.cubeboot.service.course.dto.CourseJoinDto;
import com.cube001.cubeboot.service.course.mapper.CourseJoinMapper;
import com.cube001.cubeboot.service.course.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: CourseJoinServiceImpl
 * 课程参与详情service实现类
 *
 * @author 张浩杰
 * @version 1.0
 * @date 2025/3/12 17:50
 */
@Service
public class CourseJoinServiceImpl extends ServiceImpl<CourseJoinMapper, CourseJoin> implements CourseJoinService {

    @Autowired
    private StudentService studentService;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private AssignmentConverter assignmentConverter;
    @Autowired
    private AssignmentSubmitService assignmentSubmitService;

    /**
     * @param
     * @return
     * @description 课程参与详情分页
     * @author 张浩杰
     * @date 2025年3月13日 13:09:05
     */
    @Override
    public Page<CourseJoin> searchPage(Page<CourseJoin> plusPage,Long courseId ,Wrapper<CourseJoin> formToEntityWrapper) {
        List<CourseJoin> courseJoinList = baseMapper.searchPage(plusPage,courseId, formToEntityWrapper.eq("tcpd.is_deleted", 0));
        plusPage.setRecords(courseJoinList);
        return plusPage;
    }

    /**
     * 获取学生参与的课程分页
     *
     * @param plusPage
     * @param wrapper
     * @return
     * @author Juci
     */
    @Override
    public Page<CourseJoinDto> studentJoinCourse(Page<CourseJoinDto> plusPage, Wrapper<CourseJoinDto> wrapper) {
        wrapper.eq("a.is_deleted",0);
        List<CourseJoinDto> courseJoinDtos = this.baseMapper.studentJoinCourse(plusPage, wrapper);
        plusPage.setRecords(courseJoinDtos);
        return plusPage;
    }

    /**
     * 添加参与记录
     *
     * @param userId   用户编号
     * @param courseId 课程编号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Long userId, Long courseId) {
        Student student = studentService.getStudentByUserId(userId);
        if (student == null) {
            throw new FriendException("当前用户非学生用户！");
        }
        Wrapper<CourseJoin> wrapper = new EntityWrapper<>();
        wrapper.eq("student_id", student.getId());
        wrapper.eq("course_id", courseId);
        if (this.baseMapper.selectCount(wrapper) > 0) {
            throw new FriendException("无需重复加入课程！");
        }
        CourseJoin courseJoin = new CourseJoin();
        courseJoin.setCourseId(courseId);
        courseJoin.setStudentId(student.getId());
        courseJoin.setJoinExercise(0);
        if (this.baseMapper.insert(courseJoin) == 0) {
            throw new FriendException("加入学习失败！");
        }
        Course course = this.courseMapper.selectById(courseId);
        if (course.getJoinNumber() != null) {
            course.setJoinNumber(course.getJoinNumber() + 1);
        } else {
            course.setJoinNumber(1);
        }
        if(this.courseMapper.updateById(course)==0){
            throw new FriendException("更新课程参与数失败！");
        }
    }

    /**
     * 通过邀请码参与课程学习
     * @author Juci
     * @param userid 用户编号
     * @param invitationCode 邀请码
     */
    @Override
    public void joinCourse(Long userid,String invitationCode){
        Wrapper<Course> wrapper = new EntityWrapper<>();
        wrapper.eq("code",invitationCode);
        wrapper.eq("type",1);
        List<Course> courses = this.courseMapper.selectList(wrapper);
        if(courses.size()==0){
            throw new FriendException("邀请码无效！");
        }
        this.add(userid,courses.get(0).getId());
    }

    /**
     * @description  学生参与私有课程的作业分页
     * @author 马勇超
     * @date 2025/4/23 9:51
     * @param   page,wrapper
     * @return
     */
    @Override
    public Page<AssignmentDto> studentJoinPrivateCourseTask(Page page, Wrapper<CourseJoinDto> wrapper) {
        List<CourseJoinDto> courseJoins = this.baseMapper.studentJoinPrivateCourseTask(page,wrapper.eq("a.is_deleted",0));
        List<Assignment> assignmentList = new ArrayList<>();
        List<AssignmentDto> assignmentDtoList = new ArrayList<>();
        for (CourseJoinDto c:courseJoins) {
            List<Assignment> assignments = assignmentService.selectList(new EntityWrapper<Assignment>().eq("course_id",c.getCourseId()));
            Course course = courseMapper.selectById(c.getCourseId());
            for (Assignment a:assignments) {
                a.setCourse(course);
                assignmentList.add(a);
            }
        }
        assignmentDtoList = assignmentConverter.list2Ato(assignmentList);
        Student student = studentService.selectOne(new EntityWrapper<Student>().eq("user_id", ShiroKit.getUserId()));
        // 判断作业是否已经提交
        for (AssignmentDto a:assignmentDtoList) {
            AssignmentSubmit assignmentSubmit = assignmentSubmitService.selectOne(new EntityWrapper<AssignmentSubmit>()
                    .eq("student_id",student.getId()).eq("assignment_id",a.getId()));
            if(null != assignmentSubmit){
                a.setIsSubmit(true);
                a.setSubmitScore(assignmentSubmit.getScore());
                a.setWhetherScore(assignmentSubmit.getWhetherScore());
            }else{
                a.setIsSubmit(false);
            }
        }
        page.setRecords(assignmentDtoList);
        return page;
    }

    /**
     * @description  学生参与私有课程无分页
     * @author 马勇超
     * @date 2025/4/23 9:51
     * @param   wrapper
     * @return
     */
    @Override
    public List<CourseJoinDto> studentJoinPrivateCourses(Wrapper<CourseJoinDto> wrapper) {
        List<CourseJoinDto> courseDtos = this.baseMapper.studentJoinPrivateCourses(wrapper.eq("a.is_deleted",0));
        return courseDtos;
    }
}
