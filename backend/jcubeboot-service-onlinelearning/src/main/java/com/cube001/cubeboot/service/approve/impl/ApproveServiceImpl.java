package com.cube001.cubeboot.service.approve.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.approve.entity.Approve;
import com.cube001.cubeboot.service.approve.mapper.ApproveMapper;
import com.cube001.cubeboot.service.approve.ApproveService;
import org.springframework.stereotype.Service;

/**
* Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
* FileName: ApproveServiceImpl.java
* 通用点赞服务实现类的详细说明
*
* @author anonymous
* @date 2025/4/5 11:17
* @version 1.0
*/
@Service
public class ApproveServiceImpl extends ServiceImpl<ApproveMapper, Approve> implements ApproveService {
    @Override
    public boolean insert(Approve entity) {
        return super.insert(entity);
    }
}
