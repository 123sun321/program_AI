package com.cube001.cubeboot.service.assignment.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.assignment.entity.RecommendWork;
import com.cube001.cubeboot.service.assignment.dto.RecommendWorkDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author anonymous
 * @since 2019-04-19
 */
public interface RecommendWorkMapper extends BaseMapper<RecommendWork> {

    /**
     * @description  通过学生id和作业标题取消推荐
     * @author anonymous
     * @date 2025/4/22 10:49
     * @param   studentId,title
     */
    void delByStudentIdWithTitle(@Param("studentId") Long studentId,@Param("title") String title);

    /**
     * @description  优秀作品分页展示
     * @author anonymous
     * @date 2025/4/22 11:26
     * @param   page,wrapper
     * @return   recommendWork
     */
    List<RecommendWork> recommendWorkPage(@Param("userId") Long userId,Page<RecommendWork> page,@Param("ew") Wrapper<RecommendWork> wrapper);

    /**
     * 通过编号查询优秀作品信息
     * @author anonymous
     * @param id
     * @return
     */
    RecommendWorkDto getRecommendWorkAndCode(@Param("id") Long id);
}
