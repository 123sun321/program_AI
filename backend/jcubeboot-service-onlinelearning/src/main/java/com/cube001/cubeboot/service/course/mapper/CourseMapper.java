package com.cube001.cubeboot.service.course.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.course.entity.Course;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
* FileName: CourseMapper
* 班课mapper
*
* @author anonymous
* @date 2025/11/28 9:14
* @version 1.0
*/
public interface CourseMapper  extends BaseMapper<Course> {
    /**
     * @description 根据课程id获取该课程总收藏数量
     * @author anonymous
     * @date 2025/7/5 11:01
     * @param courseId 课程id
     * @return
     */
    Integer getCollectedNumberByCourseId(Long courseId);
    /**
     * @description 更新参与人数
     * @author anonymous
     * @date 2025/7/5 9:51
     * @param id 课程id
     * @return
     */
    Boolean updateJoinNumber(@Param("id") Long id);
    /**
     * @description 判断当前用户是否收藏该课程
     * @author anonymous
     * @date 2025/4/27 12:20
     * @param userId 用户id
     * @param courseId 课程id
     * @return
     */
    Long judgeCourseIsCollected(@Param("userId")Long userId,@Param("courseId")Long courseId);
    /**
     * @description 根据标签id,查询相关的课程的link_id,再查询出对应的course实体
     * @author anonymous
     * @date 2025/4/22 16:08
     * @param labelId 标签id
     * @return List<Course>
     */
    List<Course> getCourseListByLabelId(@Param("labelId") Long labelId);
    /**
     * @description 查询到全部的符合要求的课程列表,不带标签
     * @author anonymous
     * @date 2025/4/19 15:12
     * @param plusPage 分页
     * @param userId 用户id
     * @param wrapper 条件构造器
     * @param studentId 学生id
     * @param joinCategory 参与类型
     * @return List<Course>
     */
    List<Course> getCourseListWithoutTagsSearch(Page<Course> plusPage, @Param("ew") Wrapper<Course> wrapper, @Param("userId")Long userId,
                               @Param("studentId")Long studentId,@Param("joinCategory")Integer joinCategory);
    /**
     * @description 查询到全部的符合要求的课程列表,带标签
     * @author anonymous
     * @date 2025/4/19 15:12
     * @param  plusPage 分页信息
     * @param wrapper 查询体
     * @param userId 用户Id
     * @param joinCategory 参与类型
     * @param studentId 学生id
     * @param tagName 标签名字
     * @return List<Course>
     */
    List<Course> getCourseListWithTagsSearch(Page<Course> plusPage, @Param("ew") Wrapper<Course> wrapper, @Param("userId")Long userId,
                                                @Param("tagName")String tagName,@Param("studentId")Long studentId,@Param("joinCategory")Integer joinCategory);
    /**
     * @description 获取所有课程类型列表
     * @author anonymous
     * @date  2019年3月15日 11:28:11
     * @param
     * @return
    */
    List<Course> getCategoryList();

    /**
     * @description 获取课程难易程度列表
     * @author anonymous
     * @date 2025年3月15日 13:43:01
     * @param
     * @return
     */
    List<Course> getComplexityList();


    List<Course> getCourseType();

    /**
     * 更新实验数
     * @author anonymous
     * @param id
     */
    void updateExerciseNumber(Long id);
}
