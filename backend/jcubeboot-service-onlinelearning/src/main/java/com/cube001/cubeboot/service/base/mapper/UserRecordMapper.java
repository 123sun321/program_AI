package com.cube001.cubeboot.service.base.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.base.entity.UserRecord;
import com.cube001.cubeboot.service.base.dto.UserRecordDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: UserRecordMapper
 * 用户浏览记录Mapper
 *
 * @author Juci
 * @version 1.0
 * @date 2025/4/22 13:39
 */
public interface UserRecordMapper extends BaseMapper<UserRecord> {
    /**
     * 分页查询浏览记录
     * @author Juci
     * @param page
     * @param wrapper
     * @return
     */
    List<UserRecordDto> searchPage(Page page, @Param("ew") Wrapper<UserRecordDto> wrapper);
}
