package com.cube001.cubeboot.access.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cube001.cubeboot.access.PermissionContext;
import com.cube001.cubeboot.access.controller.dto.PermissionDtoConverter;
import com.cube001.cubeboot.access.controller.dto.PermissionEditDto;
import com.cube001.cubeboot.access.controller.dto.PermissionListDto;
import com.cube001.cubeboot.core.authorization.entity.Permission;
import com.cube001.cubeboot.core.authorization.service.PermissionService;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.core.validateGroup.EditChecks;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: com.cube001.cubeboot.authration
 * 权限转换dto
 *
 * @author 陈乐乐
 * @version 1.0
 * @date 2025/12/26 14:12
 */
@RestController
@RequestMapping("/${cubeboot.app-manager-path}/permission")
public class PermissionController implements IApplicationService {


    @Autowired
    private PermissionService permissionService;

    @Autowired
    private PermissionDtoConverter permissionDtoConverter;

    /**
     * 获取所有权限列表
     */
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    public List<Permission> getAll() {
        List<Permission> permissions = permissionService.selectList(new EntityWrapper<>());
        List<Permission> treePermission = new ArrayList<Permission>();
        List<Permission> rootPermissions = permissions.stream().filter(p -> p.getName().indexOf(":") < 0).collect(Collectors.toList());

        for (Permission permission : rootPermissions) {
            buildTreePermission(permission, permissions);
        }
        treePermission.addAll(rootPermissions);
        return treePermission;
    }

    @GetMapping(value = "get")
    public Permission get(@RequestParam("id") Long id) {
        Permission permission = permissionService.selectById(id);
        return permission;
    }

    @PostMapping(value = "update")
    public Boolean change(@RequestBody @Validated({Default.class, EditChecks.class}) PermissionEditDto permissionEditDto) {
        Permission permission = new Permission();
        permission.setId(permissionEditDto.getId());
        permission.setDescription(permissionEditDto.getDescription());
        permission.setDisplayName(permissionEditDto.getDisplayName());
        Boolean suc = permissionService.updateById(permission);
        return suc;
    }

    @GetMapping("syncPermission")
    public List<String> syncPermission() {
        List<String> context = PermissionContext.getPermissions();
        List<Permission> allPermissions = permissionService.selectList(new EntityWrapper<>());
        List<String> allPermissionNames = allPermissions.stream().map(b -> b.getName()).collect(Collectors.toList());
        List<String> addPermissions = context.stream().filter(b -> allPermissionNames.indexOf(b) == -1).collect(Collectors.toList());
        return addPermissions;
    }

    /**
     * 保存权限信息
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public void save(@RequestBody PermissionEditDto permissionEditDto) {
        if (StringUtils.isNotEmpty(permissionEditDto.getParentName())) {
            //有父类，检查名称规范
            if (!permissionEditDto.getName().startsWith(permissionEditDto.getParentName() + ":")) {
                throw new FriendException("权限名称必段以父权限的名称开头");
            }
            String lastName = permissionEditDto.getName().substring(permissionEditDto.getParentName().length() + 1);
            if (StringUtils.isEmpty(lastName)) {
                throw new FriendException("子权限名称不能为空");
            }
            if (lastName.indexOf(":") > 0) {
                throw new FriendException("子权限名称中不能包括冒号(:)");
            }

        } else {
            if (permissionEditDto.getName().indexOf(":") > 0) {
                throw new FriendException("权限名称中不能包括冒号(:)");
            }
        }
        Permission permission = permissionDtoConverter.editDtoToEntity(permissionEditDto);
        if (permission.getId() == null) {
            //create
            permissionService.insert(permission);
        } else {
            //edit
            permissionService.updateById(permission);
        }

    }

    /**
     * 批量插入数据
     *
     * @param permissionListDto
     * @return void
     * @description
     * @author 陈乐乐
     * @date 2025/12/28 13:29
     */
    @PostMapping(value = "batchInsert")
    public Boolean batchInsert(@RequestBody @Validated PermissionListDto permissionListDto) {
        List<Permission> permissions = new ArrayList<>();
        for (PermissionEditDto editDto : permissionListDto.getList()) {
            Permission permission = new Permission();
            permission.setName(editDto.getName());
            permission.setDisplayName(editDto.getDisplayName());
            permission.setDescription(editDto.getDescription());
            permissions.add(permission);
        }
        Boolean suc = permissionService.insertBatch(permissions);
        return suc;
    }

    /**
     * 删除权限信息
     */
    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public void delete(Integer id) {
        Permission deletePermission = permissionService.selectById(id);
        if (deletePermission != null) {
            permissionService.deleteById(id);
        }
    }

    /**
     * 生成children数据
     */
    private void buildTreePermission(Permission parent, List<Permission> allPermissions) {
        List<Permission> children = allPermissions.stream().filter(p -> p.getName().startsWith(parent.getName() + ":")).collect(Collectors.toList());
        for (Permission permission : children) {


            String nameLast = permission.getName().substring(parent.getName().length() + 1);
            if (nameLast.indexOf(":") > 0) {

                continue;
            } else {
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<Permission>());
                }
                parent.getChildren().add(permission);
            }
            buildTreePermission(permission, allPermissions);
        }
    }


}


