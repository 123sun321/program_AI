package com.cube001.cubeboot.access.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.access.controller.dto.AccessUserDtoConverter;

import com.cube001.cubeboot.access.controller.dto.UserEditDetailDto;
import com.cube001.cubeboot.access.controller.dto.UserListItemDto;
import com.cube001.cubeboot.access.controller.dto.UserQueryParm;
import com.cube001.cubeboot.core.authorization.entity.Role;
import com.cube001.cubeboot.core.authorization.entity.User;
import com.cube001.cubeboot.core.authorization.service.RoleService;
import com.cube001.cubeboot.core.authorization.service.UserService;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.exception.FriendException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: com.cube001.cubeboot.access.controller
 * 类的详细说明
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/12/29 11:14
 */
@RestController
@RequestMapping(value = "/${cubeboot.app-manager-path}/user")
public class UserController implements IApplicationService {
    @Autowired
    private UserService userService;

    @Autowired
    private AccessUserDtoConverter userDtoConverter;

    @Autowired
    private RoleService roleService;

    /**
     * 获取用户列表
     */
    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    public Page<UserListItemDto> getUserList(@RequestBody UserQueryParm input) {
        Page<User> page = new Page<User>(
                input.getPageIndex(),
                input.getPageSize(),
                input.getOrderFiled(),
                input.getIsAsc()
        );
        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        if (input.getUserType() != null) {
            userEntityWrapper.addFilter("userType&" + input.getUserType() + "=" + input.getUserType());
        }
        if (input.getStartDate() != null) {
            userEntityWrapper.ge("creationTime", input.getStartDate());
        }
        if (input.getEndDate() != null) {
            userEntityWrapper.le("creationTime", input.getEndDate());
        }
        if (StringUtils.isNotEmpty(input.getKeyword())) {
            userEntityWrapper.andNew().like("name", input.getKeyword()).or()
                    .like("surname", input.getKeyword())
                    .or().like("userName", input.getKeyword())
                    .or().like("emailAddress", input.getKeyword())
                    .or().like("phoneNumber", input.getKeyword());

            //userEntityWrapper.andNew("name like '%{0}%' or surname like '%{0}%' or userName like '%{0}%' or email like '%{0}%' or phoneNumber like '%{0}%'");
        }
        userService.selectPage(page, userEntityWrapper);
        return userDtoConverter.toPagedDto(page);
        //return userService.selectList(userEntityWrapper);
    }

    /**
     * 获取用户详细信息
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public UserEditDetailDto get(Integer id) {
        User user = userService.selectById(id);
        user.setPassword("");//不传输密码
        return userDtoConverter.userToEditDto(user);
    }

    @GetMapping(value = "resetPassword")
    public Boolean resetPassword(@RequestParam("account") String account) {
        User user = userService.getByAccount(account);
        Boolean suc = userService.changePassword(user, "000000");
        return suc;
    }

    /**
     * 保存用户信息
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public void save(@RequestBody UserEditDetailDto userEditDetailDto) {
        if (userEditDetailDto.getId() != null) {
            //update
            User updateUser = userService.selectById(userEditDetailDto.getId());
            if (!updateUser.getUserType().equals(userEditDetailDto.getUserType())) {
                updateUser.setUserType(userEditDetailDto.getUserType());
            }
            updateUser.setName(userEditDetailDto.getName());
            updateUser.setSurname(userEditDetailDto.getSurname());
            updateUser.setSex(userEditDetailDto.getSex());
            if (!updateUser.getPhoneNumber().equals(userEditDetailDto.getPhoneNumber())) {
                if (userService.checkPhoneIsRegister(userEditDetailDto.getPhoneNumber()) > 0) {
                    throw new FriendException("该手机号已经注册");
                }
                updateUser.setPhoneNumber(userEditDetailDto.getPhoneNumber());
            }
            updateUser.setEmailAddress(userEditDetailDto.getEmailAddress());
            updateUser.setActive(userEditDetailDto.getActive());
            updateUser.setLockoutEnabled(userEditDetailDto.getLockoutEnabled());
            updateUser.setLastModificationTime(new Date());
            updateUser.setLastModefierUserId(ShiroKit.getUserId().toString());
            userService.updateById(updateUser);
        } else {
            //create
            if (userEditDetailDto.getUserName().equals("admin")) {
                throw new FriendException("用户名不能使用");
            }
            User createUser = userDtoConverter.editDtoToUser(userEditDetailDto);
            createUser.setTenantid(-1);
            createUser.setCreationTime(new Date());
            createUser.setCreatorUserId(ShiroKit.getUserId().toString());
            userService.createUser(createUser);
        }
    }

    /**
     * 删除用户信息
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public void delete(Long id) {
        User user = userService.selectById(id);
        if (user.getUserName().equals("admin")) {
            throw new FriendException("不能删除系统用户");
        }
        userService.deleteById(id);
    }

    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    public List<Role> getRoles(@RequestParam("userId") Long userid) {
        List<Role> roles = roleService.getRolesByUserId(userid);
        return roles;
    }

    @RequestMapping(value = "/removeRole", method = RequestMethod.POST)
    public Boolean remove(@RequestParam("roleId") int roleId,@RequestParam("userId") Long userid) {
        roleService.removeUserFromRole(roleId, userid);
        return true;
    }
}
