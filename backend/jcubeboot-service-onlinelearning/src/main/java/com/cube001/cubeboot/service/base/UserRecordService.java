package com.cube001.cubeboot.service.base;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.cube001.cubeboot.base.entity.UserRecord;
import com.cube001.cubeboot.service.base.dto.UserRecordDto;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: UserRecordService
 * 用户浏览记录Service
 *
 * @author Juci
 * @version 1.0
 * @date 2025/4/22 13:39
 */
public interface UserRecordService extends IService<UserRecord> {

    /**
     * 添加访问记录
     * @author Juci
     * @param visitor
     * @param areVisitor
     */
    void add(Long visitor,Long areVisitor);
    /**
     * 分页查询浏览记录
     * @author Juci
     * @param page
     * @param wrapper
     * @return
     */
    Page<UserRecordDto> searchPage(Page page, Wrapper<UserRecordDto> wrapper);
}
