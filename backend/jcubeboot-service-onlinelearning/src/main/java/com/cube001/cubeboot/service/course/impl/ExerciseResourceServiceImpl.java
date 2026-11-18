package com.cube001.cubeboot.service.course.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.course.entity.ExerciseResource;
import com.cube001.cubeboot.service.course.ExerciseResourceService;
import com.cube001.cubeboot.service.course.dto.ExerciseResourceConverter;
import com.cube001.cubeboot.service.course.dto.ExerciseResourceDto;
import com.cube001.cubeboot.service.course.mapper.ExerciseResourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: ExerciseResourceServiceImpl
 * 实验资源Service实现类
 *
 * @author Juci
 * @version 1.0
 * @date 2025/3/11 16:56
 */
@Service
public class ExerciseResourceServiceImpl extends ServiceImpl<ExerciseResourceMapper, ExerciseResource> implements ExerciseResourceService {

    @Autowired
    private ExerciseResourceConverter exerciseResourceConverter;

    @Override
    public Long insert(ExerciseResourceDto exerciseResourceDto){
        ExerciseResource exerciseResource = exerciseResourceConverter.dtoToEntity(exerciseResourceDto);
        Integer result = this.baseMapper.insert(exerciseResource);
        return result > 0 ? exerciseResource.getId() : 0;
    }

    @Override
    public Long update(ExerciseResourceDto exerciseResourceDto){
        ExerciseResource exerciseResource = exerciseResourceConverter.dtoToEntity(exerciseResourceDto);
        Integer result = this.baseMapper.updateById(exerciseResource);
        return result > 0 ? exerciseResource.getId() : 0;
    }

    @Override
    public List<ExerciseResourceDto> getList(Long exerciseId){
        Wrapper<ExerciseResource> wrapper = new EntityWrapper<>();
        wrapper.eq("exercise_id",exerciseId);
        List<ExerciseResource> exerciseResources = this.baseMapper.selectList(wrapper);
        return exerciseResourceConverter.listEntityToDto(exerciseResources);
    }
}
