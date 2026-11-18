package com.cube001.cubeboot.access.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.cube001.cubeboot.access.PermissionContainer;
import com.cube001.cubeboot.access.PermissionContext;
import com.cube001.cubeboot.core.authorization.entity.Permission;
import com.cube001.cubeboot.core.authorization.entity.Role;
import com.cube001.cubeboot.core.authorization.service.PermissionService;
import com.cube001.cubeboot.core.authorization.service.RoleService;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.domain.IApplicationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.soap.SAAJResult;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: com.cube001.cubeboot.access.controller
 * 当前用户权限Controller
 *
 * @author 陈乐乐
 * @version 1.0
 * @date 2025/12/26 15:39
 */
@Api(value = "权限测试")
@RestController
@RequestMapping(value = "${cubeboot.app-manager-path}/userPermission")
public class UserPermissionController implements IApplicationService {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleService roleService;

    @GetMapping(value = "list")
    public List<String> getLoginUserPermission() {
        Long userid = ShiroKit.getUserId();
        List<Permission> permissions = permissionService.selectByUserReferenceRole(userid);
        List<String> permissionStrs=permissions.stream().map(b->b.getName()).collect(Collectors.toList());
        return permissionStrs;
    }

    @ApiOperation(value = "test")
    @GetMapping(value = "test")
    public List<String> get() {
        return PermissionContext.getPermissions();
    }
}
