package com.cube001.cubeboot.service.base.impl;

import com.cube001.cubeboot.core.authorization.entity.Role;
import com.cube001.cubeboot.core.authorization.mapper.UserroleMapper;
import com.cube001.cubeboot.core.authorization.service.RoleService;
import com.cube001.cubeboot.service.base.RoleExtendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Copyright (C),2018-2019, ZhengZhouChuangZhi. Co.,Ltd.
 * FileName: [RoleExtendServiceImpl]
 * RoleExtendServiceImpl实现类
 *
 * @author Juci
 * @date 2025-01-17
 * @version 1.0
 */
@Service
public class RoleExtendServiceImpl implements RoleExtendService {

    @Autowired
    private RoleService roleService;

    @Override
    public void setSchoolManagerRole(Long userid){
        Role role = roleService.findRoleByName("schoolManager");
        roleService.addUserToRole(role.getId(),userid);
    }
    @Override
    public void setStudentRole(Long userid){
        Role role = roleService.findRoleByName("student");
        roleService.addUserToRole(role.getId(),userid);
    }
    @Override
    public void setTeacherRole(Long userid){
        Role role = roleService.findRoleByName("teacher");
        roleService.addUserToRole(role.getId(),userid);
    }
    @Override
    public void setCompanyRole(Long userid){
        Role role = roleService.findRoleByName("company");
        roleService.addUserToRole(role.getId(),userid);
    }
    @Override
    public void deleteSchoolManagerRole(Long userid){
        Role role = roleService.findRoleByName("schoolManager");
        roleService.removeUserFromRole(role.getId(),userid);
    }
    @Override
    public void deleteStudentRole(Long userid){
        Role role = roleService.findRoleByName("student");
        roleService.removeUserFromRole(role.getId(),userid);
    }
    @Override
    public void deleteTeacherRole(Long userid){
        Role role = roleService.findRoleByName("teacher");
        roleService.removeUserFromRole(role.getId(),userid);
    }
    @Override
    public void deleteCompanyRole(Long userid){
        Role role = roleService.findRoleByName("company");
        roleService.removeUserFromRole(role.getId(),userid);
    }
}
