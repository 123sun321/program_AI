package com.cube001.cubeboot.access.dataPermission;

import com.cube001.cubeboot.base.entity.Teachers;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.domain.tenant.TenantIdUtil;
import com.cube001.cubeboot.core.permission.template.TemplateDataContext;
import com.cube001.cubeboot.core.permission.template.TemplateDataService;
import com.cube001.cubeboot.service.base.TeachersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: com.cube001.cubeboot.access.dataPermission
 * 类的详细说明
 *
 * @author 陈乐乐
 * @version 1.0
 * @date 2025/1/7 15:09
 */
@Service
public class ApplicationTemplateDataServiceImpl implements TemplateDataService {
    @Autowired
    private TeachersService teachersService;

    @Override
    public TemplateDataContext get() {
        ApplicationTemplateDataContext context = new ApplicationTemplateDataContext();
        String tenantIdStr = TenantIdUtil.getTenantId();
        if (tenantIdStr == null || tenantIdStr.equals("")) {
            tenantIdStr = "-1";
        }
        context.setTenantId(Long.parseLong(tenantIdStr)); //这里有错误
        context.setUserId(ShiroKit.getUserId());

        Teachers teachers = teachersService.getByUserId(ShiroKit.getUserId());
        if (teachers != null) {
            //context.setDepartmentId(teachers.getDepartmentId());
        } else {
            context.setDepartmentId(-1L);
        }
        return context;
    }
}
