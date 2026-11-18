package com.cube001.cubeboot.service.course.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.course.entity.ExerciseJoin;
import com.cube001.cubeboot.course.entity.ExerciseJoinRecord;
import com.cube001.cubeboot.service.base.mapper.StudentMapper;
import com.cube001.cubeboot.service.course.ExerciseJoinRecordService;
import com.cube001.cubeboot.service.course.dto.ExerciseJoinRecord.ExerciseJoinRecordConverter;
import com.cube001.cubeboot.service.course.dto.ExerciseJoinRecord.ExerciseJoinRecordDto;
import com.cube001.cubeboot.service.course.dto.ExerciseJoinRecord.ExerciseRecordStatisticsDto;
import com.cube001.cubeboot.service.course.dto.ExerciseJoinRecord.ExerciseStatisticNumberDto;
import com.cube001.cubeboot.service.course.mapper.ExerciseJoinMapper;
import com.cube001.cubeboot.service.course.mapper.ExerciseJoinRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: ExerciseJoinRecordServiceImpl
 * 实验参与记录service实现类
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/4/8 13:34
 */
@Service
public class ExerciseJoinRecordServiceImpl extends ServiceImpl<ExerciseJoinRecordMapper, ExerciseJoinRecord> implements ExerciseJoinRecordService {

    @Autowired
    private ExerciseJoinRecordConverter exerciseJoinRecordConverter;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private ExerciseJoinMapper exerciseJoinMapper;
    /**
     * 创建实验记录
     * @author anonymous
     * @param exerciseJoinRecordDto
     * @param userId 用户编号
     */
    @Override
    public void insert(ExerciseJoinRecordDto exerciseJoinRecordDto,Long userId){
        Student student = studentMapper.getStudentByUserId(userId);
        if(student==null){
            throw new FriendException("非学生用户！");
        }
        Wrapper<ExerciseJoin> wrapper = new EntityWrapper<>();
        wrapper.eq("exercise_id",exerciseJoinRecordDto.getExerciseId());
        wrapper.eq("student_id",student.getId());
        List<ExerciseJoin> exerciseJoins = exerciseJoinMapper.selectList(wrapper);
        if(exerciseJoins.size()==0){
            throw new FriendException("当前用户尚未参与实验！");
        }
        ExerciseJoinRecord exerciseJoinRecord = exerciseJoinRecordConverter.dtoToEntity(exerciseJoinRecordDto);
        exerciseJoinRecord.setStudentId(student.getId());
        exerciseJoinRecord.setExerciseJoinId(exerciseJoins.get(0).getId());
        this.baseMapper.insert(exerciseJoinRecord);
    }
    /**
     * 学生参与练习次数相关统计
     * @author anonymous
     * @param userId 用户编号
     * @return
     */
    @Override
    public ExerciseStatisticNumberDto statisticNumber(Long userId){
        Student student = studentMapper.getStudentByUserId(userId);
        if(student==null){
            throw new FriendException("非学生用户！");
        }
        ExerciseStatisticNumberDto exerciseStatisticNumberDto = new ExerciseStatisticNumberDto();
        Wrapper<ExerciseJoinRecord> wrapper = new EntityWrapper<>();
        wrapper.eq("student_id",student.getId());
        //累计实验次数
        exerciseStatisticNumberDto.setExerciseCount(this.baseMapper.selectCount(wrapper));
        //有效学习时间
        exerciseStatisticNumberDto.setExerciseTime(this.baseMapper.sumMinute(student.getId()));
        //当前连续学习天数
        exerciseStatisticNumberDto.setContinuousDay(student.getContinuousDay());
        //当前最大连续学习天数
        exerciseStatisticNumberDto.setMaxDay(student.getMaxDay());
        //总学习天数
        exerciseStatisticNumberDto.setSumDay(this.baseMapper.sumDay(student.getId()));
        return exerciseStatisticNumberDto;
    }

    /**
     * 统计学生学习时间
     * @author anonymous
     * @param userId 用户编号
     * @return
     */
    @Override
    public List<ExerciseRecordStatisticsDto> exerciseRecordStatistics(Long userId){
        Student student = studentMapper.getStudentByUserId(userId);
        if(student==null){
            throw new FriendException("非学生用户！");
        }
        return this.baseMapper.exerciseRecordStatistics(student.getId());
    }
    /**
     * 更新学生学习天数XXLive
     * @author anonymous
     * @param student
     */
    @Override
    public void updateContinuousDay(Student student){
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        //一天的开始时间 yyyy:MM:dd 00:00:00
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        Date dayStart = calendar.getTime();
        //一天的结束时间 yyyy:MM:dd 23:59:59
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        calendar.set(Calendar.MILLISECOND,999);
        Date dayEnd = calendar.getTime();
        Wrapper<ExerciseJoinRecord> wrapper = new EntityWrapper<>();
        wrapper.eq("student_id",student.getId());
        wrapper.between("creation_time",dayStart,dayEnd);
        if(this.baseMapper.selectCount(wrapper)>0){
            Integer continuousDay = student.getContinuousDay();
            continuousDay = continuousDay+1;
            student.setContinuousDay(continuousDay);
            if(continuousDay>student.getMaxDay()){
                student.setMaxDay(continuousDay);
            }
        }
        else {
            student.setContinuousDay(0);
        }
        this.studentMapper.updateById(student);
    }
}
