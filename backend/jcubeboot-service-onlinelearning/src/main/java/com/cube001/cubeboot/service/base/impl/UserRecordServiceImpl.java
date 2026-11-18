package com.cube001.cubeboot.service.base.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.base.entity.UserRecord;
import com.cube001.cubeboot.service.base.UserRecordService;
import com.cube001.cubeboot.service.base.dto.UserRecordDto;
import com.cube001.cubeboot.service.base.mapper.UserRecordMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: UserRecordServiceImpl
 * 用户浏览记录Service
 *
 * @author Juci
 * @version 1.0
 * @date 2025/4/22 13:45
 */
@Service
public class UserRecordServiceImpl extends ServiceImpl<UserRecordMapper, UserRecord> implements UserRecordService {

    /**
     * 添加访问记录
     * @author Juci
     * @param visitor 访问者
     * @param areVisitor 被访问者
     */
    @Override
    public void add(Long visitor,Long areVisitor){
        Wrapper<UserRecord> wrapper = new EntityWrapper<>();
        wrapper.eq("visitor",visitor);
        wrapper.eq("are_visitor",areVisitor);
        String formatDate = "'"+DateUtil.formatDate(new Date())+" 00:00:00'";
        wrapper.addFilter("creation_time>"+formatDate);
        UserRecord userRecord = new UserRecord();
        List<UserRecord> userRecordList = this.baseMapper.selectList(wrapper);
        if(userRecordList.size()>0){
            userRecord = userRecordList.get(0);
            userRecord.setCreationTime(new Date());
            this.baseMapper.updateById(userRecord);
        }
        else{
            userRecord.setVisitor(visitor);
            userRecord.setAreVisitor(areVisitor);
            this.baseMapper.insert(userRecord);
        }
    }

    /**
     * 分页获取浏览记录
     * @author Juci
     * @param page
     * @param wrapper
     * @return
     */
    @Override
    public Page<UserRecordDto> searchPage(Page page, Wrapper<UserRecordDto> wrapper) {
        List<UserRecordDto> userRecordDtoList = this.baseMapper.searchPage(page, wrapper.eq("a.is_deleted", 0));
        page.setRecords(userRecordDtoList);
        return page;
    }
}
