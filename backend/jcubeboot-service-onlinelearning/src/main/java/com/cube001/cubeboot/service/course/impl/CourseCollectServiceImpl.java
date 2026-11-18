package com.cube001.cubeboot.service.course.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.service.course.CourseCollectService;
import com.cube001.cubeboot.service.course.dto.CourseCollectDto;
import com.cube001.cubeboot.service.course.mapper.CourseCollectMapper;
import com.cube001.cubeboot.course.entity.CourseCollect;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author qw
 * @since 2019-03-11
 */
@Service
public class CourseCollectServiceImpl extends ServiceImpl<CourseCollectMapper, CourseCollect> implements CourseCollectService {

    /**
     * 添加收藏
     * @author Juci
     * @param userId 用户编号
     * @param courseId 课程编号
     */
    @Override
    public void add(Long userId,Long courseId){
        Wrapper<CourseCollect> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id",userId);
        wrapper.eq("course_id",courseId);
        if(this.baseMapper.selectCount(wrapper)>0){
            throw new FriendException("无需重复收藏课程！");
        }
        CourseCollect courseCollect = new CourseCollect();
        courseCollect.setUserId(userId);
        courseCollect.setCourseId(courseId);
        this.baseMapper.insert(courseCollect);
    }

    /**
     * 取消收藏
     * @param userId 用户编号
     * @param id 课程收藏行的id
     */
    @Override
    public void delete(Long userId,Long id){
        CourseCollect courseCollect = this.baseMapper.selectById(id);
        if(courseCollect==null || !userId.equals(courseCollect.getUserId())){
            throw new FriendException("当前传入数据异常或者无法取消别人的收藏！");
        }
        this.baseMapper.deleteById(id);
    }

    /**
     * 分页获取列表
     * @author Juci
     * @param page
     * @param wrapper
     * @return
     */
    @Override
    public Page<CourseCollectDto> searchPage(Page<CourseCollectDto> page, Wrapper<CourseCollectDto> wrapper){
        wrapper.eq("a.is_deleted",0);
        List<CourseCollectDto> courseCollectDtos = this.baseMapper.searchPage(page, wrapper);
        page.setRecords(courseCollectDtos);
        return page;
    }
}
