package com.cube001.cubeboot.service.course.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.course.entity.ExerciseJoin;
import com.cube001.cubeboot.course.entity.ExerciseJoinReport;
import com.cube001.cubeboot.service.base.mapper.StudentMapper;
import com.cube001.cubeboot.service.course.ExerciseJoinReportService;
import com.cube001.cubeboot.service.course.ExerciseJoinService;
import com.cube001.cubeboot.service.course.dto.ExerciseJoinReportConverter;
import com.cube001.cubeboot.service.course.dto.ExerciseJoinReportDto;
import com.cube001.cubeboot.service.course.mapper.ExerciseJoinReportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: ExerciseJoinReportServiceImpl
 * ExerciseJoinReportServiceImpl
 *
 * @author Juci
 * @version 1.0
 * @date 2025/3/28 18:02
 */
@Service
public class ExerciseJoinReportServiceImpl extends ServiceImpl<ExerciseJoinReportMapper, ExerciseJoinReport> implements ExerciseJoinReportService {

    @Autowired
    private ExerciseJoinService exerciseJoinService;
    @Autowired
    private ExerciseJoinReportConverter exerciseJoinReportConverter;
    @Autowired
    private StudentMapper studentMapper;
    /**
     * 添加实验报告
     * @param exerciseJoinReport
     */
    @Override
    public void create(ExerciseJoinReport exerciseJoinReport){
        Wrapper<ExerciseJoinReport> wrapper = new EntityWrapper<>();
        wrapper.eq("exercise_join_id",exerciseJoinReport.getExerciseJoinId());
        if(this.baseMapper.selectCount(wrapper)>0){
            throw new FriendException("报告已存在！");
        }
        exerciseJoinReport.setWordNumber(exerciseJoinReport.getContent().length());
        this.baseMapper.insert(exerciseJoinReport);
    }

    /**
     * 编辑
     * @author Juci
     * @param exerciseJoinReportDto
     */
    @Override
    public void edit(Long userId, ExerciseJoinReportDto exerciseJoinReportDto){
        ExerciseJoinReport exerciseJoinReport = exerciseJoinReportConverter.dtoToEntity(exerciseJoinReportDto);
        exerciseJoinReport.setWordNumber(exerciseJoinReport.getContent().length());
        this.baseMapper.updateById(exerciseJoinReport);
    }

    /**
     * 分页获取报告
     * @param plusPage
     * @param wrapper
     * @return
     */
    @Override
    public Page<ExerciseJoinReportDto> searchPage(Page<ExerciseJoinReportDto> plusPage, Wrapper<ExerciseJoinReportDto> wrapper) {
        wrapper.eq("a.is_deleted",0);
        List<ExerciseJoinReportDto> exerciseJoinReportDtoList = this.baseMapper.searchPage(plusPage, wrapper);
        plusPage.setRecords(exerciseJoinReportDtoList);
        return plusPage;
    }

    /**
     * 相关报告
     * @author Juci
     * @param exerciseId 实验编号
     * @return
     */
    @Override
    public List<ExerciseJoinReportDto> correlationReport(Long exerciseId){
        Page page = new Page(0, 10);
        Wrapper<ExerciseJoinReportDto> wrapper = new EntityWrapper<>();
        wrapper.eq("a.is_deleted",0);
        wrapper.eq("b.exercise_id",exerciseId);
        return  this.baseMapper.searchPage(page, wrapper);
    }
    /**
     * 通过条件查询
     * @author Juci
     * @param wrapper
     * @return
     */
    @Override
    public ExerciseJoinReportDto selectReport(Wrapper<ExerciseJoinReportDto> wrapper){
        wrapper.eq("a.is_deleted",0);
        return this.baseMapper.searchPage(wrapper);
    }

    /**
     * 更新浏览量
     * @author Juci
     * @param id
     */
    @Override
    public void updateClick(Long id){
        this.baseMapper.updateClick(id);
    }

    /**
     * 通过实验编号获取当前用户报告
     * @author Juci
     * @param userId 用户编号
     * @param id 实验编号
     * @return
     */
    @Override
    public ExerciseJoinReport getByExerciseId(Long userId,Long id){
        Student student = studentMapper.getStudentByUserId(userId);
        if(student==null){
            throw new FriendException("非学生用户无法获取报告！");
        }
        return this.baseMapper.findByExerciseId(id,student.getId());
    }
}
