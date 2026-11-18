package com.cube001.cubeboot.console.controller;

import com.cube001.cubeboot.console.controller.dto.UserDtoConverter;
import com.cube001.cubeboot.core.authorization.entity.Role;
import com.cube001.cubeboot.core.authorization.entity.User;
import com.cube001.cubeboot.core.authorization.service.RoleService;
import com.cube001.cubeboot.core.authorization.service.UserService;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.base.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @Author:张向宇
 * @Description:
 * @Date:Create in 15:13 2018/08/03
 * @Modified By:
 */
@Controller
public class HomeController extends BaseController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;

    @Autowired
    private UserDtoConverter userDtoConverter;
    /**
     * 跳转到主页
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        //获取菜单列表
        List<Role> roleList = roleService.getRolesByUserId(ShiroKit.getUserId());
        if (roleList == null || roleList.size() == 0) {
            ShiroKit.getSubject().logout();
            model.addAttribute("tips", "该用户没有角色，无法登陆");
            return "/account/login";
        }
        return REDIRECT+ "/account/index";
    }
    /**
     * 选择模板页
     */



    private void setUserInfoToTemplate(Model model){
        //获取用户头像
        Long id = ShiroKit.getUser().getId();
        User user = userService.selectById(id);
        String avatar = user.getUserPic();
        model.addAttribute("avatar", avatar);
        model.addAttribute("user",user);
        model.addAttribute("newUserName","张向宇111");
    }
}
