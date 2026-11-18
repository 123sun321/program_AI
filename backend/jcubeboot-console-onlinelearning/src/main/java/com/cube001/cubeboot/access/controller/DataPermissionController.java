package com.cube001.cubeboot.access.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.permission.TableConditionContainer;
import com.cube001.cubeboot.core.permission.entity.DataPermission;
import com.cube001.cubeboot.core.permission.entity.TableCondition;
import com.cube001.cubeboot.core.permission.service.DataPermissionService;
import com.cube001.cubeboot.core.permission.service.dto.RoleDataPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: com.cube001.cubeboot.access.controller
 * 数据权限控制器
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/1/9 13:37
 */
@RestController
@RequestMapping(value = "${cubeboot.app-manager-path}/dataPermission")
public class DataPermissionController implements IApplicationService {

    @Autowired
    private DataPermissionService dataPermissionService;


    @PostMapping("add")
    @Transactional
    public Boolean add(@RequestBody @Validated RoleDataPermission roleDataPermission) {
        Boolean suc = dataPermissionService.add(roleDataPermission);
        return suc;
    }

    @GetMapping("get")
    public RoleDataPermission get(Integer roleId) {
        RoleDataPermission roleDataPermission = dataPermissionService.get(roleId);
        return roleDataPermission;
    }

    @GetMapping("getCondition")
    public List<TableCondition> getTableCondition() {
        return TableConditionContainer.getTableConditions();
    }
}
