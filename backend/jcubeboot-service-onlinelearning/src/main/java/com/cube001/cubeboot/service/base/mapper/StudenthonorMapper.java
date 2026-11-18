package com.cube001.cubeboot.service.base.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.base.entity.Studenthonor;
import com.cube001.cubeboot.service.base.dto.StudenthonorDto;
import com.cube001.cubeboot.service.base.dto.StudenthonorPageDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Copyright (C),2018-2019, ZhengZhouChuangZhi. Co.,Ltd.
 * FileName: [SchoolhonorMapper]
 * SchoolhonorMapper接口
 *
 * @author anonymous
 * @Date 2018-11-30
 * @version 1.0
 */
public interface StudenthonorMapper extends BaseMapper<Studenthonor> {
    /**
     * 功能：分页、模糊查询获取学生奖惩列表
     * @param page
     * @param wrapper
     * @return
     */
    List<Studenthonor>  getStuRewardPunishList(Page page, @Param("ew") Wrapper<Studenthonor> wrapper);

    /**
     * 功能：获取需要修改的单条数据
     * @param id 学生奖惩id
     * @return
     */
    Studenthonor getStuRewardPunish(@Param("id") Long id);
    /**
     * @description 处理同时添加多个学生的奖惩信息
     * @author anonymous
     * @date 2025/12/5 9:28
     * @param studenthonorDto 提交的数据
     * @return void
    */
    void getStudenthonorEntity(StudenthonorDto studenthonorDto);
    /**
     * @description 组合搜索处理
     * @author anonymous
     * @date 2025/12/5 9:35
     * @param studenthonorPageDto 分页、搜索数据
     * @return com.baomidou.mybatisplus.mapper.EntityWrapper<com.cube001.cubeboot.base.entity.Studenthonor>
    */
    EntityWrapper<Studenthonor> setWrapper(StudenthonorPageDto studenthonorPageDto);
}
