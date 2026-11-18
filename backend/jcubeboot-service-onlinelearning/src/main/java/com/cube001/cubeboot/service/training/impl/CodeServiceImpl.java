package com.cube001.cubeboot.service.training.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.service.course.ExerciseJoinRecordService;
import com.cube001.cubeboot.service.course.dto.ExerciseJoinRecord.ExerciseJoinRecordDto;
import com.cube001.cubeboot.training.entity.Code;
import com.cube001.cubeboot.service.training.CodeService;
import com.cube001.cubeboot.service.training.dto.CodeConverter;
import com.cube001.cubeboot.service.training.dto.CodeDto;
import com.cube001.cubeboot.service.training.mapper.CodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: CodeServiceImpl
 * 代码Service实现类
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/3/11 16:56
 */
@Service
public class CodeServiceImpl extends ServiceImpl<CodeMapper, Code> implements CodeService {

    @Autowired
    private CodeConverter codeConverter;
    @Autowired
    private ExerciseJoinRecordService exerciseJoinRecordService;
    @Override
    public Long insert(CodeDto codeDto){
        Code code = codeConverter.dtoToEntity(codeDto);
        Integer result = this.baseMapper.insert(code);
        return result > 0 ? code.getId() : 0;
    }

    @Override
    public Long update(CodeDto codeDto){
        Code code = codeConverter.dtoToEntity(codeDto);
        Integer result = this.baseMapper.updateById(code);
        return result > 0 ? code.getId() : 0;
    }

    @Override
    public CodeDto get(Long id){
        Code code = this.baseMapper.selectById(id);
        if(null == code){
            return  new CodeDto();
        }
        return codeConverter.entityToDto(code);
    }

    /**
     * 更新实验代码
     * @author anonymous
     * @param userId
     * @param codeDto
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
     public void updateCodeDto(Long userId,CodeDto codeDto){
        Code code = codeConverter.dtoToEntity(codeDto);
        Integer result = this.baseMapper.updateById(code);
        if (codeDto.getExerciseId()!=null && codeDto.getExerciseId()!=0){
            ExerciseJoinRecordDto exerciseJoinRecordDto = new ExerciseJoinRecordDto();
            exerciseJoinRecordDto.setMinute(codeDto.getStudyTime()/60);
            exerciseJoinRecordDto.setExerciseId(codeDto.getExerciseId());
            this.exerciseJoinRecordService.insert(exerciseJoinRecordDto,userId);
        }
     }

    @Override
    public Code selectByAssignmentSubmitId(Long id) {
        return baseMapper.selectByAssignmentSubmitId(id);
    }
}
