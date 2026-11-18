package com.cube001.cubeboot.service.course.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.course.entity.CourseCollect;
import com.cube001.cubeboot.service.course.dto.CourseCollectDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author qw
 * @since 2019-03-11
 */
public interface CourseCollectMapper extends BaseMapper<CourseCollect>{
    /**
     * 分页获取收藏列表
     * @author Juci
     * @param page
     * @param ew
     * @return
     */
    List<CourseCollectDto> searchPage(Page<CourseCollectDto> page, @Param("ew") Wrapper<CourseCollectDto> ew);
}
