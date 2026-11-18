package com.cube001.cubeboot.service.base;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.cube001.cubeboot.base.entity.Studenthonor;
import com.cube001.cubeboot.service.base.dto.StudenthonorDto;
import com.cube001.cubeboot.service.base.dto.StudenthonorPageDto;
import org.apache.ibatis.annotations.Param;

/**
 * Copyright (C),2018-2019, ZhengZhouChuangZhi. Co.,Ltd.
 * FileName: [StudenthonorService]
 * StudenthonorService服务层接口
 *
 * @author HuangPengju
 * @Date 2018-11-30
 * @version 1.0
 */
public interface StudenthonorService extends IService<Studenthonor> {
    /**
     * 功能：分页、模糊查询获取学生奖惩列表
     * @param page
     * @param formToEntityWrapperWithSearch
     * @return
     */
    Page<Studenthonor> getStuRewardPunishList(Page page, Wrapper<Studenthonor> formToEntityWrapperWithSearch);
    /**
     * 功能：获取需要修改的单条数据
     * @param id 学生奖惩id
     * @return 学生奖惩信息
     */
    Studenthonor getStuRewardPunish(Long id);
    /**
     * @description 处理同时添加多个学生的奖惩信息
     * @author HuangPengju
     * @date 2025/12/5 9:27
     * @param studenthonorDto 提交的数据
     * @return void
    */
    Boolean getStudenthonorEntity(StudenthonorDto studenthonorDto);
    /**
     * @description 组合搜索处理
     * @author HuangPengju
     * @date 2025/12/5 9:34
     * @param studenthonorPageDto 搜索、分页数据
     * @return com.baomidou.mybatisplus.mapper.EntityWrapper<com.cube001.cubeboot.base.entity.Studenthonor>
    */
    EntityWrapper<Studenthonor> setWrapper(StudenthonorPageDto studenthonorPageDto);
}
