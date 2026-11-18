package com.cube001.cubeboot.service.community.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cube001.cubeboot.community.entity.SignRecord;
import com.cube001.cubeboot.service.community.dto.SignRecordDto;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: SignRecordMapper
 * 签到记录Mapper接口
 * </p>
 *
 * @author anonymous
 * @since 2019-03-21
 */
public interface SignRecordMapper extends BaseMapper<SignRecord> {


    /**
     * @description 分页展示带搜索
     * @author anonymous
     * @date 2025/3/21 13:40
     * @param page,wrapper
     * @return java.util.List<com.cube001.cubeboot.service.course.dto.JoinCoursePathDto>
    */
    List<SignRecordDto> page(Pagination page, @Param("ew") Wrapper<SignRecordDto> wrapper);

    /**
     * @description 根据时间条件搜索 签到班级TOP10
     * @author anonymous
     * @date 2025/3/22 9:45
     * @param startTime,endTime
     * @return java.util.List<com.cube001.cubeboot.community.entity.SignRecord>
    */
    List<SignRecord> showAllGrade(String startTime,String endTime);
    /**
     * @description 根据时间条件搜索签到学生TOP10
     * @author anonymous
     * @date 2025/3/22 14:27
     * @param startTime,endTime
     * @return java.util.List<com.cube001.cubeboot.community.entity.SignRecord>
    */
    List<SignRecord> showAllStudent(String startTime,String endTime);

    /**
     * @description 根据时间条件搜索 总签到次数
     * @author anonymous
     * @date 2025/3/22 14:28
     * @param startTime,endTime
     * @return java.util.List<com.cube001.cubeboot.community.entity.SignRecord>
    */
    List<SignRecord> showAll(String startTime,String endTime);
}
