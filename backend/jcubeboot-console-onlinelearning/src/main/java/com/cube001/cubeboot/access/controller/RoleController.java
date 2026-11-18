package com.cube001.cubeboot.access.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.access.controller.dto.*;
import com.cube001.cubeboot.core.authorization.entity.Permission;
import com.cube001.cubeboot.core.authorization.entity.PermissionRelation;
import com.cube001.cubeboot.core.authorization.entity.Role;
import com.cube001.cubeboot.core.authorization.entity.User;
import com.cube001.cubeboot.core.authorization.service.PermissionService;
import com.cube001.cubeboot.core.authorization.service.RoleService;
import com.cube001.cubeboot.core.authorization.service.UserService;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.exception.FriendException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色管理控制器
 * @Author:张向宇
 * @Description:
 * @Date:Create in 16:10 2018/07/13
 * @Modified By:
 */
@RestController
@RequestMapping("/${cubeboot.app-manager-path}/role")
public class RoleController implements IApplicationService {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private AccessUserDtoConverter userDtoConverter;

    @Autowired
    private PermissionService permissionService;

    /**
     * 获取角色信息,带所持有的权限列表
     * */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public RoleWithPermissionDto getRoleInfo(Integer id)
    {
        Role role=  roleService.selectById(id);
        List<Permission> permissions= permissionService.findPermissionsByRoleId(id);

        RoleWithPermissionDto roleWithPermissionDto=new RoleWithPermissionDto();
        roleWithPermissionDto.setRole(role);
        List<PermissionSimpleDto> permissionSimpleDtos=permissions.stream().map(p-> {
            PermissionSimpleDto permissionSimpleDto=new PermissionSimpleDto();
            permissionSimpleDto.setId(p.getId());
            permissionSimpleDto.setName(p.getName());
            permissionSimpleDto.setDataType(p.getPermissionRelation().getDataType());
            if(p.getPermissionRelation()!=null)
            {
                permissionSimpleDto.setGrant(p.getPermissionRelation().getIsGranted());
            }
            else
            {
                permissionSimpleDto.setGrant(true);
            }
            return permissionSimpleDto;
        }).collect(Collectors.toList());
        roleWithPermissionDto.setPermissions(permissionSimpleDtos);
        return roleWithPermissionDto;
    }

    /**
     * 获取角色信息列表
     * */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Page<Role> getList(@RequestBody RoleQueryParm input)
    {
        Page<Role> page =new Page<Role>(
                input.getPageIndex(),
                input.getPageSize(),
                input.getOrderFiled(),
                input.getIsAsc()
        );
        EntityWrapper<Role> entityWrapper=new EntityWrapper<>();
        if(StringUtils.isNotEmpty(input.getKeyword()))
        {
            entityWrapper.andNew().like("name",input.getKeyword()).or()
                    .like("displayName",input.getKeyword());

            //userEntityWrapper.andNew("name like '%{0}%' or surname like '%{0}%' or userName like '%{0}%' or email like '%{0}%' or phoneNumber like '%{0}%'");
        }
        roleService.selectPage(page,entityWrapper);
        return page;
    }

    /**
     * 修改角色信息
     * */
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public void update(@RequestBody RoleWithPermissionDto input)
    {
        Role role=roleService.selectById(input.getRole().getId());
//        if(role.getIsStatis()==null || !role.getIsStatis())
//        {
//
//        }
        role.setDisplayName(input.getRole().getDisplayName());
        role.setName(input.getRole().getName());
        role.setIsDefault(input.getRole().getIsDefault());
        role.setDisableTenantFilter(input.getRole().getDisableTenantFilter());
        roleService.updateById(role);
        List<Permission> newPermission=new ArrayList<>();
        for(PermissionSimpleDto permissionSimpleDto:input.getPermissions())
        {
            Permission permission=new Permission();
            permission.setId(permissionSimpleDto.getId());
            permission.setName(permissionSimpleDto.getName());
            PermissionRelation permissionRelation=new PermissionRelation();
            permissionRelation.setRoleId(role.getId());
            permissionRelation.setName(permissionSimpleDto.getName());
            permissionRelation.setIsGranted(permissionSimpleDto.getGrant());
            permissionRelation.setDataType(permissionSimpleDto.getDataType());
            permission.setPermissionRelation(permissionRelation);
            newPermission.add(permission);
        }
        permissionService.SetRolePermissions(role.getId(),newPermission);
    }

    /**
     * 创建角色
     * */
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @Transactional
    public void create(@RequestBody RoleWithPermissionDto input)
    {
        Role role=input.getRole();
        role.setIsStatis(false);
        roleService.insert(role);
        List<Permission> newPermission=new ArrayList<>();
        for(PermissionSimpleDto permissionSimpleDto:input.getPermissions())
        {
            Permission permission=new Permission();
            permission.setId(permissionSimpleDto.getId());
            permission.setName(permissionSimpleDto.getName());
            PermissionRelation permissionRelation=new PermissionRelation();
            permissionRelation.setRoleId(role.getId());
            permissionRelation.setName(permissionSimpleDto.getName());
            permissionRelation.setIsGranted(permissionSimpleDto.getGrant());
            permission.setPermissionRelation(permissionRelation);
            newPermission.add(permission);
        }
        permissionService.SetRolePermissions(role.getId(),newPermission);
    }


    @RequestMapping(value = "/",method = RequestMethod.DELETE)
    public void delete(Integer id)
    {
        Role role=roleService.selectById(id);
        if(role.getIsStatis())
        {
            throw new FriendException("系统角色不允许删除");
        }
        roleService.deleteById(id);
    }

    /**
     * 获取角色对应的用户列表
     * */
    @RequestMapping(value = "/userList", method = RequestMethod.POST)
    public Page<UserListItemDto> getUserList(@RequestBody RoleUserQueryParm input)
    {
        Page<User> page =new Page<User>(
                input.getPageIndex(),
                input.getPageSize(),
                input.getOrderFiled(),
                input.getIsAsc()
        );
        EntityWrapper<User> userEntityWrapper=new EntityWrapper<>();
        if(input.getUserType()!=null)
        {
            userEntityWrapper.addFilter("userType&"+input.getUserType()+"="+input.getUserType());
        }
        if(input.getStartDate()!=null)
        {
            userEntityWrapper.ge("creationTime",input.getStartDate());
        }
        if(input.getEndDate()!=null)
        {
            userEntityWrapper.le("creationTime",input.getEndDate());
        }
        if(StringUtils.isNotEmpty(input.getKeyword()))
        {
            userEntityWrapper.andNew().like("name",input.getKeyword()).or()
                    .like("surname",input.getKeyword())
                    .or().like("userName",input.getKeyword())
                    .or().like("emailAddress",input.getKeyword())
                    .or().like("phoneNumber",input.getKeyword());

            //userEntityWrapper.andNew("name like '%{0}%' or surname like '%{0}%' or userName like '%{0}%' or email like '%{0}%' or phoneNumber like '%{0}%'");
        }
        userService.selectUserListByRole(page,input.getRoleId(),userEntityWrapper);
        return userDtoConverter.toPagedDto(page);
        //return userService.selectList(userEntityWrapper);
    }

    /**
     * 从角色中移除用户
     * */
    @RequestMapping(value = "/userList",method = RequestMethod.DELETE)
    public void RemoveUser(Integer roleId,Long userId)
    {
        roleService.removeUserFromRole(roleId,userId);
    }

    /**
     * 向角色中添加新用户
     * */
    @RequestMapping(value = "/userList/add",method = RequestMethod.POST)
    public void AddUser(Integer roleId,Long userId)
    {
        roleService.addUserToRole(roleId,userId);
    }

    /**
     * 获取角色对应的用户列表
     * */
    @RequestMapping(value = "/userList/roleStatus", method = RequestMethod.POST)
    public Page<UserListItemDto> getUserListWithRoleStatus(@RequestBody RoleUserQueryParm input)
    {
        Page<User> page =new Page<User>(
                input.getPageIndex(),
                input.getPageSize(),
                input.getOrderFiled(),
                input.getIsAsc()
        );
        EntityWrapper<User> userEntityWrapper=new EntityWrapper<>();
        if(input.getUserType()!=null)
        {
            userEntityWrapper.addFilter("userType&"+input.getUserType()+"="+input.getUserType());
        }
        if(input.getStartDate()!=null)
        {
            userEntityWrapper.ge("creationTime",input.getStartDate());
        }
        if(input.getEndDate()!=null)
        {
            userEntityWrapper.le("creationTime",input.getEndDate());
        }
        if(StringUtils.isNotEmpty(input.getKeyword()))
        {
            userEntityWrapper.andNew().like("name",input.getKeyword()).or()
                    .like("surname",input.getKeyword())
                    .or().like("userName",input.getKeyword())
                    .or().like("emailAddress",input.getKeyword())
                    .or().like("phoneNumber",input.getKeyword());

            //userEntityWrapper.andNew("name like '%{0}%' or surname like '%{0}%' or userName like '%{0}%' or email like '%{0}%' or phoneNumber like '%{0}%'");
        }
        userService.selectUserListWithRoleStatus(page,input.getRoleId(),userEntityWrapper);
        return userDtoConverter.toPagedDto(page);
        //return userService.selectList(userEntityWrapper);
    }

}
