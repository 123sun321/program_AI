package com.cube001.cubeboot.service.community.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.community.entity.SignRecord;
import com.cube001.cubeboot.service.community.SignRecordService;
import com.cube001.cubeboot.service.community.dto.SignRecordConverter;
import com.cube001.cubeboot.service.community.dto.SignRecordDto;
import com.cube001.cubeboot.service.community.mapper.SignRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: SignRecordServiceImpl
 * 签到记录服务实现类
 * </p>
 *
 * @author qw
 * @since 2019-03-21
 */
@Service
public class SignRecordServiceImpl extends ServiceImpl<SignRecordMapper, SignRecord> implements SignRecordService {

    @Autowired
    private SignRecordConverter signRecordConverter;

    /**
     * @description 分页展示带查询
     * @author qw
     * @date 2025/3/21 13:44
     * @param page,wrapper
     * @return com.baomidou.mybatisplus.plugins.Page<com.cube001.cubeboot.service.community.dto.SignRecordDto>
    */
    @Override
    public Page<SignRecordDto> page(Page<SignRecordDto> page, Wrapper<SignRecordDto> wrapper) {
        List<SignRecordDto> signRecordDto = baseMapper.page(page,wrapper.eq("t.is_deleted","0").orderBy("t.sign_time",false));
        page.setRecords(signRecordDto);
        return page;
}


    /**
     * @description  根据时间范围查询签到学生
     * @author 马勇超
     * @date 2025/3/25 15:03
     * @param   startTime,endTime
     * @return   List<SignRecordDto>
     */
    @Override
    public List<SignRecordDto> selectCountByStudent(String startTime,String endTime) {
        Wrapper<SignRecord> wrapper = new EntityWrapper<>();
        wrapper.ge("sign_time",startTime).le("sign_time",endTime);
        wrapper.setSqlSelect("student_name, COUNT(*) as count").groupBy("student_name").orderBy("count",true);
        List<SignRecord> studentRecorList = baseMapper.selectList(wrapper);
        List<SignRecord> studentRecordes = new ArrayList<>();
        int size = studentRecorList.size();
        int fromIndex = size - 9;
        if(fromIndex < 0){
            fromIndex = 0;
        }
        studentRecordes = studentRecorList.subList(fromIndex,size);
        List<SignRecordDto> studentSignRecordDtoList = signRecordConverter.listEntityToDto(studentRecordes);
        return studentSignRecordDtoList;
    }

    /**
     * @description  根据时间范围查询签到班级
     * @author 马勇超
     * @date 2025/3/25 15:03
     * @param   startTime,endTime
     * @return   List<SignRecordDto>
     */
    @Override
    public List<SignRecordDto> selectCountByGrade(String startTime,String endTime) {
        Wrapper<SignRecord> wrapper = new EntityWrapper<>();
        wrapper.ge("sign_time",startTime).le("sign_time",endTime);
        wrapper.setSqlSelect("grade_name, COUNT(*) as count").groupBy("grade_name").orderBy("count",true);
        List<SignRecord> gradeRecorList = baseMapper.selectList(wrapper);
        List<SignRecord> gradeRecordes = new ArrayList<>();
        int size = gradeRecorList.size();
        int fromIndex = size - 9;
        if(fromIndex < 0){
            fromIndex = 0;
        }
        gradeRecordes = gradeRecorList.subList(fromIndex,size);
        List<SignRecordDto> gradeSignRecordDtoList = signRecordConverter.listEntityToDto(gradeRecordes);
        return gradeSignRecordDtoList;
    }

    /**
     * @description  根据时间范围查询每日签到
     * @author 马勇超
     * @date 2025/3/25 15:03
     * @param   startTime,endTime
     * @return   List<SignRecordDto>
     */
    @Override
    public List<SignRecordDto> selectCountByEveryDay(String startTime,String endTime) {
        Wrapper<SignRecord> wrapper = new EntityWrapper<>();

        int year = Integer.parseInt(startTime.substring(0,4));
        int month = Integer.parseInt(startTime.substring(5,7))-1;
        int day = Integer.parseInt(startTime.substring(8,10));

        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,day);
        Long starTime = calendar.getTimeInMillis();
        year = Integer.parseInt(endTime.substring(0,4));
        month = Integer.parseInt(endTime.substring(5,7))-1;
        day = Integer.parseInt(endTime.substring(8,10));
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(year,month,day);
        Long enTime = endCalendar.getTimeInMillis();
        Long oneDay = 1000*60*60*24L;

//        wrapper.ge("sign_time",startTime).le("sign_time",endTime);
       // wrapper.setSqlSelect("DATE_FORMAT(sign_time,'%Y-%m-%d') as sign_time, (CASE WHEN sign_time = null THEN 0 ELSE COUNT(sign_time) END) as count").groupBy("sign_time").orderBy("count",true);
        List<SignRecord> signRecordList = new ArrayList<>();
        while(starTime <= enTime){
            Date date = new Date(starTime);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            List<SignRecord> dayReocrList = baseMapper.selectList(new EntityWrapper<SignRecord>().eq("sign_time",df.format(date)));
            SignRecord signRecord = new SignRecord();
            signRecord.setSignTime(date);
            signRecord.setCount(dayReocrList.size());
            starTime += oneDay;
            signRecordList.add(signRecord);
        }

        List<SignRecordDto> daySignRecordDtoList = signRecordConverter.listEntityToDto(signRecordList);
        return daySignRecordDtoList;
    }

}
