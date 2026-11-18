package com.cube001.cubeboot.service.assignment.impl;


import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.assignment.entity.RecommendWork;
import com.cube001.cubeboot.service.assignment.RecommendWorkService;
import com.cube001.cubeboot.service.assignment.dto.RecommendWorkDto;
import com.cube001.cubeboot.service.assignment.mapper.RecommendWorkMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author anonymous
 * @since 2019-04-19
 */
@Service
public class RecommendWorkServiceImpl extends ServiceImpl<RecommendWorkMapper, RecommendWork> implements RecommendWorkService {

    /**
    * @description  通过学生id和作业标题取消推荐
    * @author anonymous
    * @date 2025/4/22 10:49
    * @param   studentId,title
    */
    @Override
    public void delByStudentIdWithTitle(Long studentId, String title) {
        this.baseMapper.delByStudentIdWithTitle(studentId,title);
    }

    /**
     * @description  优秀作品分页展示
     * @author anonymous
     * @date 2025/4/22 11:26
     * @param   page,wrapper
     * @return   recommendWork
     */
    @Override
    public Page<RecommendWork> recommendWorkPage(Long userId,Page<RecommendWork> page, Wrapper<RecommendWork> wrapper) {
        List<RecommendWork> recommendWorks = baseMapper.recommendWorkPage(userId,page,wrapper.eq("a.is_deleted",0).orderBy("a.creation_time",false));
        page.setRecords(recommendWorks);
        return page;
    }
    /**
     * 通过编号查询优秀作品
     * @author anonymous
     * @param id
     * @return
     */
    @Override
    public RecommendWorkDto getRecommendWorkAndCode(Long id){
        return this.baseMapper.getRecommendWorkAndCode(id);
    }
}
