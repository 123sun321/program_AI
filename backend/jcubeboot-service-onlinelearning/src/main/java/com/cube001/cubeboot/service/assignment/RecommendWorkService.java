package com.cube001.cubeboot.service.assignment;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.cube001.cubeboot.assignment.entity.RecommendWork;
import com.cube001.cubeboot.service.assignment.dto.RecommendWorkDto;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author anonymous
 * @since 2019-04-19
 */
public interface RecommendWorkService extends IService<RecommendWork> {

    /**
     * @description  通过学生id和作业标题取消推荐
     * @author anonymous
     * @date 2025/4/22 10:49
     * @param   studentId,title
     */
    void delByStudentIdWithTitle(Long studentId, String title);

    /**
     * @description  优秀作品分页展示
     * @author anonymous
     * @date 2025/4/22 11:26
     * @param   page,wrapper
     * @return   recommendWork
     */
    Page<RecommendWork> recommendWorkPage(Long userId,Page<RecommendWork> page, Wrapper<RecommendWork> wrapper);

    /**
     * 通过编号查询优秀作品
     * @author anonymous
     * @param id
     * @return
     */
    RecommendWorkDto getRecommendWorkAndCode(Long id);
}
