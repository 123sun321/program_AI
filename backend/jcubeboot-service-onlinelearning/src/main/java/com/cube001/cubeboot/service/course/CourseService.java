package com.cube001.cubeboot.service.course;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.cube001.cubeboot.assignment.entity.Assignment;
import com.cube001.cubeboot.base.entity.Label;
import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.core.dto.PageDto;
import com.cube001.cubeboot.course.entity.Course;
import com.cube001.cubeboot.service.base.initialization.Initialization;
import com.cube001.cubeboot.service.course.dto.CourseDto;
import com.cube001.cubeboot.service.course.dto.ImportCourseDto;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: CourseService
 * 班课接口
 *
 * @author 陈乐乐
 * @date 2025/11/28 9:13
 * @version 1.0
 */
public interface CourseService extends IService<Course> {
    /**
     * @description 根据课程id获取该课程总收藏数量
     * @author SongZeShan
     * @date 2025/7/5 11:01
     * @param
     * @return
     */
    Integer getCollectedNumberByCourseId(Long courseId);
    /**
     * @description  判断当前课程是否被收藏了
     * @author SongZeShan
     * @date 2025/7/5 9:51
     * @param
     * @return
     */
    Boolean judgeCourseIsCollected(Long courseId);

    /**
     * @description 更新参与人数
     * @author SongZeShan
     * @date 2025/7/5 9:51
     * @param
     * @return
     */
    Boolean updateJoinNumber(Long id);
    /**
     * @description 学生端课程 相关课程推荐的相关处理及逻辑,同时去重
     * @author 宋泽山
     * @date 2025/4/27 12:00
     * @param courseId 课程Id
     * @param labelList 标签列表
     * @return List<Course>
     */
    List<Course> courseRecommend(Long courseId,List<Label> labelList);
    /**
     * @description 为每个课程添加标签列表
     * @author 宋泽山
     * @date 2025/4/23 17:58
     * @param page 分页结果
     * @return Page<Course>
     */
    Page<Course> setLabelsToPage(Page<Course> page);
    /**
     * @description 根据标签id,查询相关的课程的link_id,再查询出对应的course实体
     * @author 宋泽山
     * @date 2025/4/22 16:08
     * @param labelId 标签id
     * @return List<Course>
     */
    List<Course> getCourseListByLabelId( Long labelId);
    /**
     * @description 设置course的wrapper
     * @author 宋泽山
     * @date 2025/4/22 9:18
     * @param searchDto 查询表单dto
     * @return EntityWrapper<Course>
     */
    EntityWrapper<Course> setWrapper(CommonSearchDto<Course> searchDto);
    /**
     *
     * @description 学生端设置课程查询的参数
     * @author 宋泽山
     * @date 2025/4/19 9:56
     * @param  page 分页信息
     * @param wrapper 查询体
     * @param userId 用户Id
     * @param joinCategory 参与类型
     * @return Page<PageDto>
     */
    Page<Course> stuWebCoursePage(Page<Course> page,Wrapper<Course> wrapper,Long userId,String tagName,Integer joinCategory);
    /**
     * @description 根据id查询课程信息
     * @author 张浩杰
     * @date  2019年3月18日 16:02:15
     * @param
     * @return
    */
    CourseDto get(Long courseId);
    /**
     * 解析Excel数据
     *
     * @param importData
     * @return java.util.List<com.cube001.cubeboot.service.base.dto.ImportCourseDto>
     * @description
     * @author 陈乐乐
     * @date 2025/12/26 11:08
     */
    List<ImportCourseDto> importDataToListDo(List<Map<String, Object>> importData);

    /**
     * 批量添加
     *
     * @param courses
     * @return java.lang.Boolean
     * @description
     * @author 陈乐乐
     * @date 2025/12/26 13:52
     */
    Boolean addList(List<CourseDto> courses);

    /**
     * 添加一个
     *
     * @param courseDto
     * @return java.lang.Boolean
     * @description
     * @author 陈乐乐
     * @date 2025/12/26 13:55
     */
    Boolean add(CourseDto courseDto);

    /**
     * @description 修改课程信息
     * @author 张浩杰
     * @date 2025年3月15日 08:55:59
     * @param courseDto
     * @return
    */
    Boolean update(CourseDto courseDto);
    /**
     * @description 获取当前用户下的课程信息
     * @author 张浩杰
     * @date   2019年3月14日 11:06:50
     * @param
     * @return
     */
    List<CourseDto> getListByUserId(Long userId);
    /**
     * @description 获取所有课程类型列表
     * @author 张浩杰
     * @date  2019年3月15日 11:16:15
     * @param
     * @return
    */
    List<Course> getCategoryList();
    /**
     * @description 获取课程难易程度列表
     * @author 张浩杰
     * @date 2025年3月15日 13:43:01
     * @param
     * @return
     */
    List<Course> getComplexityList();
    /**
      * @description 批量导入课程
      * @author 张人杰
      * @date 2025/4/8 16:49
      * @Param List<Long> ids Long userId
      * @return
      */
    Long batchImport(List<Long> ids,Long userId,String code);
    /**
      * @description 获取课程类别
      * @author 张人杰
      * @date 2025/4/10 14:02
      * @Param
      * @return List<Course>
      */
    List<Course> getCourseType();

    boolean deleteBatchids(List<Long> ids);

    /**
      * @description 初始化数据
      * @author 马勇超
      * @date 2025/8/8 11:18\
      * @return
     */
    Initialization getInitialization();

    /**
      * @description 初始化数据上传
      * @author 马勇超
      * @date 2025/8/9 11:21
      * @param initialization
      * @return
     */
    void uploadInitialization(Initialization initialization);
}
