package com.cube001.cubeboot.service.base.mapper;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cube001.cubeboot.base.entity.Profession;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author anonymous
 * @since 2018-11-16
 */
public interface ProfessionsMapper extends BaseMapper<Profession> {

    /**
     * 获取全部的专业id和name
     * @return
     */
    List<Profession> getIdAndName();

    /**
     * 搜索分页与查询
     * @param page
     * @param wrapper
     * @return
     */
    List<Profession> searchPage(Page<Profession> page,@Param("ew") Wrapper<Profession> wrapper);

    List<Profession> listWithProfessionsAndGrades();
}
