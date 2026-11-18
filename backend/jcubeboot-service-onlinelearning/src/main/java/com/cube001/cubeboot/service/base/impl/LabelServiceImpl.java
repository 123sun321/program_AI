package com.cube001.cubeboot.service.base.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.base.entity.Label;
import com.cube001.cubeboot.core.util.ToolUtil;
import com.cube001.cubeboot.service.base.LabelService;
import com.cube001.cubeboot.service.base.mapper.LabelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Copyright (C), 2025 - 2026, zxy.
 * FileName: LabelServiceImpl.java
 * 类的详细说明
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/3/8 17:55
 */
@Service
public class LabelServiceImpl extends ServiceImpl<LabelMapper, Label> implements LabelService {
    /**
     * @description 根据选择的技术类型来获取课程对应的所有标签列表
     * @author anonymous
     * @date 2025/4/22 14:52
     * @param techType 技术类型
     * @return List<Label>
     */
    @Override
    public List<Label> selectListByTechType(String techType) {
        if(ToolUtil.isEmpty(techType)){
            return baseMapper.selectListByTechType("%");
        }
        else{
            return baseMapper.selectListByTechType("%"+techType+"%");
        }
    }

    /**
     * @description 根据课程ID来获取课程对应的所有标签列表: 仅公共的标签
     * @author anonymous
     * @date 2025/4/22 14:52
     * @param courseId 课程id
     * @return List<Label>
     */
    @Override
    public List<Label> selectListByCourseId(Long courseId) {
        if(courseId==null){
            return baseMapper.selectListWithoutCourseId();
        }else {
            return baseMapper.selectListByCourseId(courseId);
        }
    }
    /**
     * @description  学生端案例库标签缓存功能
     * @author anonymous
     * @date 2025/5/6 9:22
     * @return
     */
    @Override
    public List<Label> selectLabelList(String category) {
        return baseMapper.selectLabelList(category);
    }

    /**
     * @param plusPage
     * @return
     * @description 实现标签分页
     * @author anonymous
     * @date 2025/3/13 10:42
     */
    @Override
    public Page<Label> pageWith(Page<Label> plusPage, Wrapper<Label> wrapper) {
        List<Label> labels = baseMapper.pageWith(plusPage, wrapper.eq("is_deleted", 0).orderBy("creation_time",false));
        plusPage.setRecords(labels);
        return plusPage;
    }

    /**
     * @param str
     * @return List
     * @description 模糊查询
     * @author anonymous
     * @date 2025/3/13 10:43
     */
    @Override
    public List<Label> selectListWithKeyWord(String str) {
        List<Label> labels = baseMapper.selectListWithKeyWord(str);
        return labels;
    }

    /**
     * @param name
     * @return List
     * @description 通过名称精确查询
     * @author anonymous
     * @date 2025/3/13 13:35
     */
    @Override
    public List<Label> selectListByName(String name) {
        List<Label> labels = baseMapper.selectListByName(name);
        return labels;
    }

    @Override
    public Label getByName(String name) {
        Wrapper<Label> labelWrapper = new EntityWrapper<>();
        labelWrapper.eq("name", name);
        List<Label> labels = this.baseMapper.selectList(labelWrapper);
        if (labels.size() > 0) {
            return labels.get(0);
        } else {
            Label label = new Label();
            label.setName(name);
            if (this.baseMapper.insert(label) > 0) {
                return label;
            } else {
                return null;
            }
        }
    }
}
