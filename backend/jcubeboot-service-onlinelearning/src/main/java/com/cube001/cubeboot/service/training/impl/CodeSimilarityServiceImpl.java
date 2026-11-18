package com.cube001.cubeboot.service.training.impl;


import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.assignment.entity.Assignment;
import com.cube001.cubeboot.assignment.entity.AssignmentSubmit;
import com.cube001.cubeboot.service.training.CodeSimilarityService;
import com.cube001.cubeboot.service.training.mapper.CodeSimilarityMapper;
import com.cube001.cubeboot.training.entity.CodeSimilarity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author anonymous
 * @since 2019-06-25
 */
@Service
public class CodeSimilarityServiceImpl extends ServiceImpl<CodeSimilarityMapper, CodeSimilarity> implements CodeSimilarityService {

    @Override
    public Page<CodeSimilarity> getStuRewardPunishList(Page<CodeSimilarity> page, Wrapper<CodeSimilarity> formToEntityWrapperWithSearch) {
        List<CodeSimilarity> stuRewardPunishList = this.baseMapper.getCodeSimilarityList(page, formToEntityWrapperWithSearch.eq("a.is_deleted",0).orderBy("a.similarity",false));
        page.setRecords(stuRewardPunishList);
        return page;
    }
}
