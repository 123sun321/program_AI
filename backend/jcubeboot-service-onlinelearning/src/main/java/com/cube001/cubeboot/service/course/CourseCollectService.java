package com.cube001.cubeboot.service.course;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.cube001.cubeboot.course.entity.CourseCollect;
import com.cube001.cubeboot.service.course.dto.CourseCollectDto;
import com.cube001.cubeboot.service.course.dto.CourseJoinDto;

import java.util.List;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author anonymous
 * @since 2019-03-11
 */
public interface CourseCollectService extends IService<CourseCollect> {

    /**
     * 添加收藏
     * @author anonymous
     * @param userId 用户编号
     * @param courseId 课程编号
     */
    void add(Long userId,Long courseId);

    /**
     * 取消收藏
     * @param userId 用户编号
     * @param id
     */
    void delete(Long userId,Long id);

    /**
     * 分页获取列表
     * @author anonymous
     * @param page
     * @param wrapper
     * @return
     */
    Page<CourseCollectDto> searchPage(Page<CourseCollectDto> page, Wrapper<CourseCollectDto> wrapper);
}
