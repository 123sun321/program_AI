package com.cube001.cubeboot.service.course;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.cube001.cubeboot.course.entity.ExerciseJoin;
import com.cube001.cubeboot.service.course.dto.ExerciseJoinDetailDto;
import com.cube001.cubeboot.service.course.dto.ExerciseJoinRecord.JoinExerciseDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Copyright (C), 2025 - 2026, zxy.
* FileName: ExerciseJoinService.java
* 类的详细说明
*
* @author 马勇超
* @date 2025/3/12 15:03
* @version 1.0
*/
public interface ExerciseJoinService extends IService<ExerciseJoin> {
    /**
     * @description 通过课程id查找相关的练习记录/参与记录
     * @author 宋泽山
     * @date 2025/4/27 16:18
     * @param courseId 课程Id
     * @return
     */
    List<ExerciseJoin> getExerciseListByCourseId(Long  courseId);
    /**
     * @description 搜索/分页查询参与课程的学生完成的实验
     * @author 张浩杰
     * @date 2025年3月15日 10:06:10
     * @param
     * @return
     */
    Page<ExerciseJoin> selectWithStudent(Page<ExerciseJoin> plusPage, Wrapper<ExerciseJoin> formToEntityWrapperWithSearch);

    /**
     * @description 删除/批量删除
     * @author 张浩杰
     * @date 2025年3月15日 10:01:23
     * @param ids
     * @return
     */
    Boolean delete(List<Long> ids);

    /**
     * 通过参与编号获取参与详情
     * @author Juci
     * @param exerciseJoinId
     * @return
     */
    ExerciseJoinDetailDto joinDetail(Long exerciseJoinId);

    /**
     * 参与实验
     * @author juci
     * @param userId 用户编号
     * @param exerciseId 实验编号
     */
    ExerciseJoin insert(Long userId,Long exerciseId);
 /**
     * @description  获取当前用户参与的实验---作品展示
     * @author 马勇超
     * @date 2025/4/9 15:45
     * @param   id,wrapper
     * @return   List
     */
    List<ExerciseJoin> selectWithExerciseStudent(Long id,Wrapper<ExerciseJoin> wrapper);

    /**
     * 结束实验
     * @author Juci
     * @param userId
     * @param joinExerciseDto
     */
    void stopExercise(Long userId,JoinExerciseDto joinExerciseDto);
}
