package com.cube001.cubeboot.service.course.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.course.entity.ExerciseJoin;
import com.cube001.cubeboot.service.course.dto.ExerciseJoinDetailDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Copyright (C), 2025 - 2026, zxy.
* FileName: ExerciseJoinMapper.java
* 类的详细说明
*
* @author anonymous
* @date 2025/3/12 15:05
* @version 1.0
*/
public interface ExerciseJoinMapper extends BaseMapper<ExerciseJoin> {
    /**
     * @description 通过课程id查找相关的练习记录/参与记录
     * @author anonymous
     * @date 2025/4/27 16:18
     * @param courseId 课程ID
     * @param studentId 学生ID
     * @return
     */
    List<ExerciseJoin> getExerciseListByCourseId(@Param("courseId")Long  courseId,@Param("studentId")Long studentId);
    /**
     * @description 搜索/分页查询参与课程的学生完成的实验
     * @author anonymous
     * @date 2025年3月15日 10:06:10
     * @param
     * @return
     */
    List<ExerciseJoin> selectWithStudent(Page<ExerciseJoin> page,@Param("ew") Wrapper<ExerciseJoin> wrapper);

    /**
     * 通过Id获取参与实验的详情
     * @param exerciseJoinId
     * @return
     */
    ExerciseJoinDetailDto joinDetail(@Param("exerciseJoinId") Long exerciseJoinId);

    /**
     * 通过课程编号和学生编号获取参与详情
     * @author anonymous
     * @param exerciseId 课程编号
     * @param studentId 学生编号
     * @return
     */
    ExerciseJoinDetailDto joinDetailByExerciseIdAndStudentId(@Param("exerciseId") Long exerciseId,@Param("studentId") Long studentId);
    /**
     * @description  获取当前用户参与的实验---作品展示
     * @author anonymous
     * @date 2025/4/9 15:45
     * @param   eq
     * @return   List
     */
    List<ExerciseJoin> selectWithExerciseStudent(@Param("ew") Wrapper<ExerciseJoin> eq);
}
