package com.cube001.cubeboot.console.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.base.entity.Teachers;
import com.cube001.cubeboot.console.controller.dto.*;
import com.cube001.cubeboot.core.authorization.entity.Role;
import com.cube001.cubeboot.core.authorization.event.LoginSuccessEvent;
import com.cube001.cubeboot.core.authorization.service.RoleService;
import com.cube001.cubeboot.core.authorization.shiro.UserTypeEnum;
import com.cube001.cubeboot.core.authorization.shiro.token.TokenManager;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.authorization.shiro.ShiroUser;
import com.cube001.cubeboot.core.authorization.shiro.factory.IShiro;
import com.cube001.cubeboot.core.authorization.shiro.factory.ShiroFactroy;
import com.cube001.cubeboot.core.authorization.shiro.token.UsernamePasswordWithTypeToken;
import com.cube001.cubeboot.core.base.controller.BaseController;
import com.cube001.cubeboot.core.config.properties.CubeBootProperties;
import com.cube001.cubeboot.core.domain.tenant.TenantFliter;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.core.exception.InvalidKaptchaException;
import com.cube001.cubeboot.core.notification.entity.ValidationCaptchaResult;
import com.cube001.cubeboot.core.notification.service.CaptchaService;
import com.cube001.cubeboot.core.runtime.web.AppResponse;
import com.cube001.cubeboot.core.authorization.entity.User;
import com.cube001.cubeboot.core.authorization.service.UserService;
import com.cube001.cubeboot.core.util.*;
import com.cube001.cubeboot.core.weChatSdk.OpenIdSalt;
import com.cube001.cubeboot.service.base.*;
import com.cube001.cubeboot.service.base.dto.TeachersDto;
import com.cube001.cubeboot.service.console.dto.BindUserDto;
import com.cube001.cubeboot.service.base.UserRecordService;
import com.cube001.cubeboot.service.wechat.UserWechatService;
import com.cube001.cubeboot.service.wechat.WechatService;
import com.cube001.cubeboot.util.ImageBinary;
import com.cube001.cubeboot.util.RedisUtil;
import com.cube001.cubeboot.wechat.entity.UserWechat;
import com.google.code.kaptcha.Constants;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: AccountController.java
 * 用户登录控制器
 *
 * @author anonymous
 * @version 1.00
 * @Date 2018-11-27
 */
@Controller
@RequestMapping("/account")
@Slf4j
public class AccountController extends BaseController {

    @Value("${cubeboot.app-host-name}")
    private String hostName;
    @Value("${cubeboot.teacher-host-name}")
    private String teacherHostName;
    @Value("${cubeboot.student-host-name}")
    private String studentHostName;
    @Value("${cubeboot.company-host-name}")
    private String companyHostName;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserDtoConverter userDtoConverter;
    @Autowired
    private TokenManager tokenManager;
    @Autowired
    private CubeBootProperties properties;
    // 验证码发送服务
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private UserWechatService userWechatService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeachersService teachersService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private WechatService wechatService;
    @Autowired
    private UserRecordService userRecordService;
    /**
     * 跳转到登录页面
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        if (ShiroKit.isAuthenticated() || ShiroKit.getUser() != null) {
            return REDIRECT + "/";
        } else {
            return "/account/login.html";
        }
    }

    /**
     * 单点登录
     * @author anonymous
     * @date 2025/5/6
     * @return
     */
    @RequestMapping(value = "/ssologin", method = RequestMethod.GET)
    public String ssologin(@RequestParam("redirectUrl") String redirectUrl) {
        if (ShiroKit.isAuthenticated() || ShiroKit.getUser() != null) {
            String token = RandomUtil.randomString(32);
            redisUtil.set("SSOLOGIN:" + token, ShiroKit.getUserId(),60*60);
            return REDIRECT + redirectUrl+"/login?token=" + token;
        } else {
            return REDIRECT + redirectUrl+"/login?token=error";
        }
    }

    /**
     * 通过token获取用户信息
     * @author anonymous
     * @param token
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/ssouserinfo", method = RequestMethod.GET)
    public UserPersonInfoDto getSSOUserInfo(@RequestParam("token") String token) {
        try(TenantFliter tenantFliter=new TenantFliter(false)){
            Object value = redisUtil.get("SSOLOGIN:" + token);
            if(null==value){
                return null;
            }
            try {
                Long userId = Long.valueOf(String.valueOf(value));
                User user = userService.selectById(userId);
                UserPersonInfoDto personInfoDto = userDtoConverter.userToPersonDto(user);
                personInfoDto.setAvatar(user.getUserPic());
                Student student = studentService.getStudentByUserId(userId);
                // 若当前用户不是学生用户不支持单点登录
                if (student != null) {
                    personInfoDto.setUserType(2);
                    personInfoDto.setSchoolIdNumber(student.getCode());
                    personInfoDto.setProfessionName(student.getProfession().getName());
                    personInfoDto.setClassName(student.getGrade().getName());
                    return personInfoDto;
                } else {
                    return null;
                }
            }
            catch (Exception e){
                return null;
            }
        }
    }

    /**
     * 点击登录执行的动作(测试登录用)
     */
    @RequestMapping(value = "/loginTemp", method = RequestMethod.POST)
    public String loginTemp() {
        HttpServletRequest request = super.getHttpServletRequest();
        HttpServletResponse response = super.getHttpServletResponse();
        String username = super.getPara("username").trim();
        String password = super.getPara("password").trim();
        String remember = super.getPara("remember");
        //验证验证码是否正确
        if (KaptchaUtil.getKaptchaOnOff()) {
            String kaptcha = super.getPara("kaptcha").trim();
            String code = (String) super.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
            if (ToolUtil.isEmpty(kaptcha) || !kaptcha.equalsIgnoreCase(code)) {
                throw new InvalidKaptchaException();
            }
        }
        Subject currentUser = ShiroKit.getSubject();

        UsernamePasswordWithTypeToken token = new UsernamePasswordWithTypeToken(username, password);
        if ("on".equals(remember)) {
            token.setRememberMe(true);
        } else {
            token.setRememberMe(false);
        }
        currentUser.login(token);
        ShiroUser shiroUser = ShiroKit.getUser();

        super.getSession().setAttribute("shiroUser", shiroUser);
        super.getSession().setAttribute("username", shiroUser.getAccount());
        //发布登录成功事件
        publisher.publishEvent(new LoginSuccessEvent(shiroUser.getId(), shiroUser.getAccount()));
        //LogManager.me().executeLog(LogTaskFactory.loginLog(shiroUser.getId(), getIp()));
        ShiroKit.getSession().setAttribute("sessionFlag", true);
        if (Commons.isAjax(request)) {
            try {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write("success");
                response.getWriter().close();
            } catch (Exception ex) {
            }
            return "";
        } else {
            if (!shiroUser.getRoleNames().contains("shopManager")) {
                return REDIRECT + "/account/bindInviteCode";
            }
            SavedRequest savedRequest = WebUtils.getSavedRequest(super.getHttpServletRequest());
            // String url = WebUtils.getSavedRequest(super.getHttpServletRequest()).getRequestUrl();
            if (savedRequest != null) {
                String url = savedRequest.getRequestUrl();
                if (StringUtils.isNotEmpty(url) && url.indexOf("favicon.ico") < 0) {
                    return REDIRECT + url;
                } else {
                    return REDIRECT + "/";
                }
            } else {
                return REDIRECT + "/";
            }
        }
    }

    /**
     * 管理系统点击登录
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginVali(@RequestBody JSONObject jsonObject) {
        return this.loginHelper(jsonObject,-1);
    }

    /**
     * 学生端登录
     * @author anonymous
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/studentlogin", method = RequestMethod.POST)
    public String studentLogin(@RequestBody JSONObject jsonObject) {
        return this.loginHelper(jsonObject,3);
    }

    /**
     * 用户登录相关操作
     * @param jsonObject
     * @param userType -1-非学生用户 0-系统管理员 1-学校管理员 2-教师 3-学生
     * @return
     */
    private String loginHelper(JSONObject jsonObject,Integer userType) {
        // super调用父类的方法
        HttpServletRequest request = super.getHttpServletRequest();
        HttpServletResponse response = super.getHttpServletResponse();
        String username=jsonObject.get("username").toString();
        String password=jsonObject.get("password").toString();
        String remember=null;
        try {
            remember = jsonObject.get("remember").toString();
        }
        catch (Exception e){}
        if(null==username || null==password){
            throw new FriendException("账号密码不能为空！");
        }
        username = username.trim();
        password = password.trim();
        // 验证验证码是否正确
        if (KaptchaUtil.getKaptchaOnOff()) {
            String kaptcha = super.getPara("kaptcha").trim();
            String code = (String) super.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
            if (ToolUtil.isEmpty(kaptcha) || !kaptcha.equalsIgnoreCase(code)) {
                throw new InvalidKaptchaException();
            }
        }
        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        userEntityWrapper.eq("userName",username);
        List<User> userList = userService.selectList(userEntityWrapper);
        if(userList.size()==0){
            throw new FriendException("用户名不存在！");
        }
        // 若是学生用户登录判断学生用户是否存在
        if(userType==3) {
            Student student = studentService.getStudentByUserId(userList.get(0).getId());
            if (student == null) {
                throw new FriendException("非学生用户无法登录！");
            }
        }
        UsernamePasswordWithTypeToken token = new UsernamePasswordWithTypeToken(username, password);
        if ("on".equals(remember)) {
            token.setRememberMe(true);
        } else {
            token.setRememberMe(false);
        }
        try {
            ShiroKit.getSubject().login(token);
        }catch (Exception e){
            throw new FriendException("账号密码错误！");
        }
        ShiroUser shiroUser = ShiroKit.getUser();
        super.getSession().setAttribute("shiroUser", shiroUser);
        super.getSession().setAttribute("username", shiroUser.getAccount());
        //发布登录成功事件
        publisher.publishEvent(new LoginSuccessEvent(shiroUser.getId(), shiroUser.getAccount()));
        ShiroKit.getSession().setAttribute("sessionFlag", true);
        if (Commons.isAjax(request)) {
            try {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write("success");
                response.getWriter().close();
            } catch (Exception ex) {
            }
            return "";
        } else {
            SavedRequest savedRequest = WebUtils.getSavedRequest(super.getHttpServletRequest());
            if (savedRequest != null) {
                String url = savedRequest.getRequestUrl();
                if (StringUtils.isNotEmpty(url) && url.indexOf("favicon.ico") < 0) {
                    return REDIRECT + url;
                } else {
                    return REDIRECT + "/";
                }
            } else {
                return REDIRECT + "/";
            }
        }
    }


    @RequestMapping(value = "/jwtlogin", method = RequestMethod.POST)
    @ResponseBody
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password) {
        //Subject currentUser = ShiroKit.getSubject();

        IShiro shiroFactory = ShiroFactroy.me();
        User user = shiroFactory.user(username);
        ShiroUser shiroUser = shiroFactory.shiroUser(user);
        if (shiroFactory.Validate(username, password)) {
            if (!shiroUser.getRoleNames().contains("shopManager")) {
                return "not shopManager user";
            } else {
                return tokenManager.CreateToken(shiroUser, user.getPassword());
            }
            // return   JWTUtil.sign(shiroUser, user.getPassword());
        } else {
            return "user or password error";
        }
    }

    /**
     * 退出登录
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logOut() {
        HttpServletRequest request = super.getHttpServletRequest();
        HttpServletResponse response = super.getHttpServletResponse();
        ShiroKit.getSubject().logout();
        if (Commons.isAjax(request)) {
            try {
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write("success");
                response.getWriter().close();
            } catch (Exception ex) {
            }
            return "";
        } else {
            return REDIRECT + "/account/login";
        }
    }

    /**
     * 退出登录
     */
    @ResponseBody
    @RequestMapping(value = "/logoutbypost", method = RequestMethod.POST)
    public void logOutpost() {
        ShiroKit.getSubject().logout();
    }

    /**
     * 前台用户通过手机号注册
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse register(@RequestBody @Validated RegisterDto registerDto) {
        //验证图片验证码
//        String code = (String) super.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
//        if (ToolUtil.isEmpty(registerDto.getPicCaptcha()) || !registerDto.getPicCaptcha().equalsIgnoreCase(code)) {
//            throw new InvalidKaptchaException();
//        }

        //验证短信验证码
        ValidationCaptchaResult validationCaptchaResult = captchaService.validationCode(registerDto.getPhone(), "frontReg", registerDto.getPhoneCaptcha());
        if (validationCaptchaResult != ValidationCaptchaResult.Success) {
            throw new FriendException(validationCaptchaResult.getMessage());
        }


        //判断用户是否已经注册过系统
        try (TenantFliter tenantFliter = new TenantFliter(false)) {
            int count = userService.checkPhoneIsRegister(registerDto.getPhone());
            if (count > 0) {
                //已经注册过系统
                //判断是否有店铺管理角色
                User user = userService.getByPhoneNumber(registerDto.getPhone());
                List<Role> roles = roleService.getRolesByUserId(user.getId());
                boolean hasShopRole = roles.stream().anyMatch(r -> r.getName().equals("shopManager"));
                if (hasShopRole) {
                    //已经注册，并且是店铺管理用户
                    return new AppResponse(false, "registered", "您已经注册过本系统，并且拥有店铺创建权限，请直接登录！");
                } else {
                    //已经注册，但没有店铺管理角色，不能创建店铺，跳转到邀请码填写页面。
                    super.getSession().setAttribute("bindInviteCode_User", user);
                    return new AppResponse(false, "bindInviteCode", "您已经注册过本系统，但没有店铺创建权限，请登录后再绑定邀请码！");
                }
            } else {
                //没有注册，创建新用户
                User newUser = new User();
                newUser.setPhoneNumber(registerDto.getPhone());
                newUser.setUserTypes(EnumSet.of(UserTypeEnum.ShopManagerUser));
                newUser.setUserName(registerDto.getPhone());
                newUser.setName("用户:" + registerDto.getPhone());
                newUser.setSurname("用户:" + registerDto.getPhone());
                newUser.setActive(true);
                newUser.setPassword(registerDto.getPassword());
                newUser.setLockoutEnabled(false);
                newUser.setTenantid(-1);
                userService.createUser(newUser);
                //添加角色
                Role shopManagerRole = roleService.findRoleByName("shopManager");
                roleService.addUserToRole(shopManagerRole.getId(), newUser.getId());
                return new AppResponse(true, "用户创建成功");
            }
        }
    }

    /**
     * 前台注册时发送短信验证码
     */
    @RequestMapping(value = "/login/reg/sendPhoneCaptcha", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse sendPhoneCaptcha(@RequestParam String phone, @RequestParam String picCaptcha) {
        if (StringUtils.isEmpty(phone)) {
            throw new FriendException("手机号不能为空");
        }
        String code = (String) super.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        if (ToolUtil.isEmpty(picCaptcha) || !picCaptcha.equalsIgnoreCase(code)) {
            throw new InvalidKaptchaException();
        } else {
            super.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, null);
        }
        captchaService.sendSmsCaptchaService(phone, "frontReg");
        return new AppResponse(true, "发送成功");
    }

    /**
     * 检查手机号是否已经注册过平台
     */
    @RequestMapping(value = "/login/checkPhone", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse checkPhoneIsRegister(String phone) {
        if (StringUtils.isEmpty(phone)) {
            throw new FriendException("手机号不能为空");
        }
        //关闭租户过滤
        try (TenantFliter tenantFliter = new TenantFliter(false)) {
            int count = userService.checkPhoneIsRegister(phone);
            if (count > 0) {
                return new AppResponse(true, true);
            } else {
                return new AppResponse(true, false);
            }
        }
    }

    /**
     * 前台注册时发送短信验证码
     */
    @RequestMapping(value = "/login/rePass/sendPhoneCaptcha", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse sendPhoneCaptcha_restPass(@RequestParam String phone, @RequestParam String picCaptcha) {
        if (StringUtils.isEmpty(phone)) {
            throw new FriendException("手机号不能为空");
        }
        String code = (String) super.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        if (ToolUtil.isEmpty(picCaptcha) || !picCaptcha.equalsIgnoreCase(code)) {
            throw new InvalidKaptchaException();
        } else {
            super.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, null);
        }
        captchaService.sendSmsCaptchaService(phone, "resetPass");
        return new AppResponse(true, "发送成功");
    }

    /**
     * 忘记密码页面
     */
    @RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
    public String forgotPassword() {
        return "/account/forgotPassword.html";
    }

    /**
     * 忘记密码 第一步，校验验证码
     */
    @PostMapping("/forgotPassword/step1")
    @ResponseBody
    public void forgotPasswordStep1(String phone, String picCaptcha, String phoneCaptcha) {
        //验证图片验证码
//        String code = (String) super.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
//        if (ToolUtil.isEmpty(picCaptcha) || !picCaptcha.equalsIgnoreCase(code)) {
//            throw new InvalidKaptchaException();
//        }
        //验证短信验证码
        ValidationCaptchaResult validationCaptchaResult = captchaService.validationCode(phone, "resetPass", phoneCaptcha);
        if (validationCaptchaResult != ValidationCaptchaResult.Success) {
            throw new FriendException(validationCaptchaResult.getMessage());
        }
        //验证成功
        super.getSession().setAttribute("forgotPasswordPhone", phone);
    }

    /**
     * 前台用户重置密码
     */
    @RequestMapping(value = "/forgotPassword/step2", method = RequestMethod.POST)
    @ResponseBody
    public void forgotPassword(@RequestBody @Validated(value = forgotPasswordCheck.class) ResetPasswordDto resetPasswordDto) {

        if (super.getSession().getAttribute("forgotPasswordPhone") != null) {
            String phone = super.getSession().getAttribute("forgotPasswordPhone").toString();
            //此处只能重置前台商城用户的密码
            try (TenantFliter tenantFliter = new TenantFliter(false)) {
                //按手机号查找前台用户
                User user = userService.selectOne(new EntityWrapper<User>()
                        .eq("phoneNumber", phone)
                );
                if (user.getUserTypes().contains(UserTypeEnum.ManagerUser)) {
                    throw new FriendException("该用户类型不允许在此处重置密码。");
                }
                String salt = user.getSecurityStamp();
                ByteSource saltByte = new Md5Hash(salt);
                String tempPassword = new SimpleHash(ShiroKit.hashAlgorithmName
                        , resetPasswordDto.getPassword()
                        , saltByte
                        , ShiroKit.hashIterations
                ).toHex();
                userService.changePwd(user.getUserName(), tempPassword);
                Subject currentUser = ShiroKit.getSubject();
                currentUser.logout();
            }
        } else {
            throw new FriendException("非法访问步骤！");
        }
    }

    /**
     * @param passwordDto
     * @return
     * @description 登录用户修改密码
     * @author anonymous
     * @date 2025/12/25 10:27
     */
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    @RequiresAuthentication
    @ResponseBody
    public void changePassword(@RequestBody @Validated PasswordDto passwordDto) {
        //此处只能重置前台商城用户的密码
        try (TenantFliter tenantFliter = new TenantFliter(false)) {
            //获取当前登录用户
            User user = ShiroKit.getRealUser();
            if (!userService.validatePwd(user, passwordDto.getOldPassword())) {
                throw new FriendException("原密码输入错误！");
            }
            userService.changePassword(user, passwordDto.getNewPassword());
            Subject currentUser = ShiroKit.getSubject();
            currentUser.logout();
        }
    }

    /**
     * 用户中心首页
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @RequiresAuthentication
    @RequiresRoles("shopManager")
    public String index(Model model) {
        User user = userService.selectById(ShiroKit.getUserId());
        String avatar = user.getUserPic();
        if (avatar == null || avatar.equals("")) {
            avatar = "/static/images/customer-service-icon.png";
        }
        model.addAttribute("avatar", avatar);
        model.addAttribute("user", user);
        model.addAttribute("userName", user.getUserName());
        return "/account/index.html";
    }

    /**
     * 获取用户信息(包括：学生或教师的信息)
     *
     * @return
     * @description
     * @author anonymous
     * @date 2025/01/10 13:33
     * @edit Juci:2019/04/22 11:25
     */
    @RequestMapping(value = "/userinfo", method = RequestMethod.GET)
    @ResponseBody
    @RequiresAuthentication
    public UserPersonInfoDto getUserInfo() {
        try(TenantFliter tenantFliter=new TenantFliter(false)){
            Long userId = ShiroKit.getUserId();
            //获取用户信息
            User user = userService.selectById(userId);
            UserPersonInfoDto personInfoDto = userDtoConverter.userToPersonDto(user);
            //用户头像
            personInfoDto.setAvatar(user.getUserPic());
            List<String> roleList = ShiroKit.getRoles().roleNames;
            if (roleList != null) {
                personInfoDto.setRoles(roleList.toArray(new String[roleList.size()]));
            }
            //获取用户对应的教师信息
            TeachersDto teachers = teachersService.selectWithDepartment(userId);
            if (teachers != null) {
                //设置类型为教师
//                personInfoDto.setUserType(1);
                personInfoDto.setSchoolIdNumber(teachers.getCode());
            } else {
                //如果教师信息为null,则查找当前用户是否为学生
                Student student = studentService.getStudentByUserId(userId);
                if (student != null) {
//                    personInfoDto.setUserType(2);
                    personInfoDto.setSchoolIdNumber(student.getCode());
                    personInfoDto.setProfessionName(student.getProfession().getName());
                    personInfoDto.setClassName(student.getGrade().getName());
                }
            }
            return personInfoDto;
        }
    }

    /**
     * 修改用户信息
     *
     * @param userPersonInfoDto
     * @return
     * @description
     * @author anonymous
     * @date 2025/01/10 16:08
     */
    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
    @ResponseBody
    @RequiresAuthentication
    public void updateUserInfo(@RequestBody UserPersonInfoDto userPersonInfoDto) {
        User user = ShiroKit.getRealUser();
        User updateInfo = new User();
        updateInfo.setId(user.getId());
        updateInfo.setUserName(user.getUserName());
        updateInfo.setUserPic(user.getUserPic());
        if (!user.getPhoneNumber().equals(userPersonInfoDto.getPhoneNumber())) {
            updateInfo.setPhoneNumber(userPersonInfoDto.getPhoneNumber());
            updateInfo.setPhoneNumberConfirmed(false);
        }
        if (!user.getEmailAddress().equals(userPersonInfoDto.getEmailAddress())) {
            updateInfo.setEmailAddress(userPersonInfoDto.getEmailAddress());
            updateInfo.setEmailConfirmed(false);
        }
        // 判断为学生时，更新手机号和邮箱
        if(ShiroKit.getRealUser().getUserType() ==1){
            Student student = studentService.selectOne(new EntityWrapper<Student>().eq("user_id",ShiroKit.getUserId()));
            if(null!=student){
                userService.updateById(updateInfo);
                student.setContact(userPersonInfoDto.getPhoneNumber());
                student.setEmail(userPersonInfoDto.getEmailAddress());
                studentService.updateById(student);
            }else {
                userService.updateById(updateInfo);
            }
        }
        // 判断为教师时，更新手机号和邮箱
        if(ShiroKit.getRealUser().getUserType() == 2){
            Teachers teacher = teachersService.selectOne(new EntityWrapper<Teachers>().eq("user_id",ShiroKit.getUserId()));
            if(null!= teacher){
                teacher.setContact(userPersonInfoDto.getPhoneNumber());
                teacher.setEmail(userPersonInfoDto.getEmailAddress());
                teachersService.updateById(teacher);
                userService.updateById(updateInfo);
            } else {
                userService.updateById(updateInfo);
            }
        }
    }

    /**
     * 解绑当前用户的微信信息
     */
    @RequestMapping(value = "/unbindWeiXin", method = RequestMethod.GET)
    @ResponseBody
    @RequiresAuthentication
    @Transactional(rollbackFor = Exception.class)
    public void ubBindWeiXin() {
        ShiroUser user = ShiroKit.getUser();
        String openId=user.getOpenId();
        //删除user_wechat表中的原有内容
        userWechatService.delete(new EntityWrapper<UserWechat>().eq("user_id",user.getId()));

        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setUserName(user.getAccount());
        updateUser.setUserPic(user.getAvatar());
        updateUser.setOpenId("");
        updateUser.setUnionId("");
        userService.updateById(updateUser);
       //删除微信关联的标签
        try {
            List<Long> userTags = wxMpService.getUserTagService().userTagList(openId);
            for(Integer i=0;i<userTags.size();i++){
                wxMpService.getUserTagService().batchUntagging(userTags.get(i),new String[]{openId});
            }
        }
        catch (WxErrorException wxe){
            //报异常
            log.error("【为用户打标签失败】{0}", wxe);
            throw new FriendException("解绑微信失败，无法更新用户微信标签");
        }
    }

    /**
     * 修改用户图像
     */
    @ResponseBody
    @RequestMapping(value = "changehead", method = RequestMethod.POST)
    public Map<String, Object> changeHead(@RequestParam MultipartFile avatar) {
        try {
            Map<String, Object> resMap = new HashMap<>();
            String contentType = avatar.getContentType();
            String fileName = avatar.getOriginalFilename();
            String[] IMAGE_TYPE = ".png,.jpg,.PNG,.jpg".split(",");
            String path = null;
            boolean flag = false;
            for (String type : IMAGE_TYPE) {
                if (StringUtils.endsWithIgnoreCase(avatar.getOriginalFilename(), type)) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
                String profixTime = sdf1.format(new Date());
                String absolutePath = properties.getFileUploadPath() + File.separator + profixTime;
                String fileExtension = FileUtil.getExtensionNameByName(fileName);

                String randomName = UUID.randomUUID().toString().replace("-", "").substring(0, 8) + fileExtension;

                FileUtil.save(avatar.getBytes(), absolutePath, randomName);
                String realtivePath = "/upload/" + profixTime + "/" + randomName;

                User user = new User();
                user.setId(ShiroKit.getUserId());
                user.setUserName(ShiroKit.getUser().getAccount());
                user.setUserPic(realtivePath);
                // 判断是否为学生用户
                if(ShiroKit.getRealUser().getUserType() == 1){
                    Student student = studentService.selectOne(new EntityWrapper<Student>().eq("user_id",ShiroKit.getUserId()));
                    if(null!=student){
                        userService.update(user);
                        student.setImgUrl(realtivePath);
                        studentService.updateById(student);
                    }else {
                        userService.update(user);
                    }
                }
                // 判断是否为教师用户
                else if(ShiroKit.getRealUser().getUserType() == 2){
                    Teachers teacher = teachersService.selectOne(new EntityWrapper<Teachers>().eq("user_id",ShiroKit.getUserId()));
                    if(null!=teacher){
                        teacher.setImgUrl(realtivePath);
                        teachersService.updateById(teacher);
                        userService.update(user);
                    } else {
                        userService.update(user);
                    }
                }
                // 其他类型用户，只更新 user 表
                else {
                    userService.update(user);
                }
                resMap.put("oldFileName", fileName);
                resMap.put("newFileName", realtivePath);
                resMap.put("fileSize", avatar.getSize());
            } else {
                resMap.put("result", "图片格式不正确,支持png|jpg|jpeg");
            }
            return resMap;

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new FriendException("图片上传过程中出错！");
        }
    }

    /**
     * 用户中心绑定手机号-发送短信验证码
     *
     * @param phone      手机号
     * @param picCaptcha 图片验证码
     * @return
     * @description
     * @author anonymous
     * @date 2025/01/11 8:53
     */
    @RequestMapping(value = "/bindPhone/sendPhoneCaptcha", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse sendPhoneCaptcha_bindPhone(@RequestParam String phone, @RequestParam String picCaptcha) {
        if (StringUtils.isEmpty(phone)) {
            throw new FriendException("手机号不能为空");
        }
        String code = (String) super.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        if (ToolUtil.isEmpty(picCaptcha) || !picCaptcha.equalsIgnoreCase(code)) {
            throw new InvalidKaptchaException();
        } else {
            super.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, null);
        }
        captchaService.sendSmsCaptchaService(phone, "userBindPhone");
        return new AppResponse(true, "发送成功");
    }

    /**
     * 用户绑定手机号码
     *
     * @param phone 手机号
     * @param code  短信验证码
     * @return
     * @description
     * @author anonymous
     * @date 2025/01/11 10:56
     */
    @RequestMapping(value = "/bindPhone/save", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse bindPhone(@RequestParam String phone, @RequestParam String code) {
        //验证短信验证码
        ValidationCaptchaResult validationCaptchaResult = captchaService.validationCode(phone, "userBindPhone", code);
        if (validationCaptchaResult != ValidationCaptchaResult.Success) {
            throw new FriendException(validationCaptchaResult.getMessage());
        }
        //验证成功，存储数据
        String userName = ShiroKit.getUser().getAccount();
        userService.bindUserPhone(phone, userName);
        return new AppResponse(true, "绑定手机号成功");
    }

    /**
     * 解绑用户手机号
     *
     * @return
     * @description
     * @author anonymous
     * @date 2025/01/14 14:36
     */
    @RequestMapping(value = "/bindPhone/unbind", method = RequestMethod.GET)
    @ResponseBody
    public void unBindPhone() {
        ShiroUser user = ShiroKit.getUser();
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setUserName(user.getAccount());
        updateUser.setUserPic(user.getAvatar());
        updateUser.setPhoneNumberConfirmed(false);
        updateUser.setPhoneNumber("");
        userService.updateById(updateUser);
    }

    /**
     * 用户中心绑定电子邮箱-发送验证码
     *
     * @param email      邮箱地址
     * @param picCaptcha 图片验证码
     * @return
     * @description
     * @author anonymous
     * @date 2025/01/11 8:53
     */
    @RequestMapping(value = "/bindEmail/sendCaptcha", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse sendEmailCaptcha_bind(@RequestParam String email, @RequestParam String picCaptcha) {
        if (StringUtils.isEmpty(email)) {
            throw new FriendException("电子邮箱不能为空");
        }
        String code = (String) super.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        if (ToolUtil.isEmpty(picCaptcha) || !picCaptcha.equalsIgnoreCase(code)) {
            throw new InvalidKaptchaException();
        } else {
            super.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, null);
        }
        captchaService.sendEmaiCaptchService(email, "userBindEmail");
        return new AppResponse(true, "发送成功");
    }

    /**
     * 用户绑定电子邮箱
     *
     * @param email 用户电子邮箱
     * @param code  短信验证码
     * @return
     * @description
     * @author anonymous
     * @date 2025/01/11 10:56
     */
    @RequestMapping(value = "/bindEmail/save", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse bindEmail(@RequestParam String email, @RequestParam String code) {
        //验证短信验证码
        ValidationCaptchaResult validationCaptchaResult = captchaService.validationCode(email, "userBindEmail", code);
        if (validationCaptchaResult != ValidationCaptchaResult.Success) {
            throw new FriendException(validationCaptchaResult.getMessage());
        }
        //验证成功，存储数据
        String userName = ShiroKit.getUser().getAccount();
        userService.bindUserEmail(email, userName);
        return new AppResponse(true, "绑定电子邮箱成功");
    }


    /**
     * 解绑用户电子邮箱
     *
     * @return
     * @description
     * @author anonymous
     * @date 2025/01/14 14:36
     */
    @RequestMapping(value = "/bindEmail/unbind", method = RequestMethod.GET)
    @ResponseBody
    public void unBindEmail() {
        ShiroUser user = ShiroKit.getUser();
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setUserName(user.getAccount());
        updateUser.setUserPic(user.getAvatar());
        updateUser.setEmailConfirmed(false);
        updateUser.setEmailAddress("");
        userService.updateById(updateUser);
    }

    /**
     * 微信登錄跳轉
     *
     * @return
     */
    @GetMapping("/wechatLogin")
    public String wechatLogin(String type) {
        //设置回调地址
        String url = hostName + "/account/wechatUserInfo";
        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAUTH2_SCOPE_BASE, type);
        return REDIRECT + redirectUrl;
    }

    @GetMapping("/wechatLoginPhone")
    public String wechatLogin_phone(String hostName) {
        String url = this.hostName + "/account/userInfoPhone";
        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAUTH2_SCOPE_BASE, hostName);
        return REDIRECT + redirectUrl;
    }

    /**
     * 微信登錄api
     * @param bindUserDto
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/wechatLogin")
    public AppResponse wechatLogin(@RequestBody BindUserDto bindUserDto) {
        try (TenantFliter tenantFliter = new TenantFliter(false)) {
            EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
            userEntityWrapper.eq("userName",bindUserDto.getUserName());
            List<User> userList = userService.selectList(userEntityWrapper);
            if(userList.size()==0){
                return new AppResponse(false, "用户名不存在！");
            }
            IShiro shiroFactory = ShiroFactroy.me();
            User user = shiroFactory.user(bindUserDto.getUserName());
            ShiroUser shiroUser = shiroFactory.shiroUser(user);
            String tag = "";
            String hostUrl ="";
            if (shiroFactory.Validate(bindUserDto.getUserName(), bindUserDto.getPassword())) {
                // 判断用户是否已绑定
                if(user.getOpenId()!= null && user.getOpenId().length()>0){
                    return new AppResponse(false, "该用户已绑定微信账号，若重新绑定请进入个人中心解绑！");
                }
                // 判断类型
                if (bindUserDto.getType() == 1) {
                    EntityWrapper<Teachers> wrapper = new EntityWrapper<>();
                    wrapper.eq("user_id", shiroUser.getId());
                    tag = "教师";
                    hostUrl = teacherHostName;
                    if (!wechatService.IsTeacherOrSchoolManager(user)) {
                        return new AppResponse(false, "该教师账号不存在！");
                    }
                } else if (bindUserDto.getType() == 2) {
                    EntityWrapper<Student> wrapper = new EntityWrapper<>();
                    wrapper.eq("user_id", shiroUser.getId());
                    tag = "学生";
                    hostUrl = studentHostName;
                    if (studentService.selectCount(wrapper) == 0) {
                        return new AppResponse(false, "该学生账号不存在！");
                    }
                }
                //保存微信信息
                WxMpUser wxMpUser = new WxMpUser();
                if (super.getSession().getAttribute("wxmpuser") != null) {
                    wxMpUser = (WxMpUser) super.getSession().getAttribute("wxmpuser");
                    String randomName = wxMpUser.getOpenId() + ".jpg";
                    try {
                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
                        String profixTime = sdf1.format(new Date());
                        String absolutePath = properties.getFileUploadPath() + File.separator + profixTime;
                        ImageBinary.toBDFile(wxMpUser.getHeadImgUrl(), absolutePath, randomName);
                        wxMpUser.setHeadImgUrl("/upload/" + profixTime + "/" + randomName);
                    } catch (Exception e) {
                    }
                    userWechatService.add(wxMpUser, user.getId(), tag);
                }
                else{
                    return new AppResponse(false, "微信授权信息已过期！");
                }
                // 保存用户信息
                user.setUnionId(wxMpUser.getUnionId());
                user.setOpenId(wxMpUser.getOpenId());
                userService.update(user);
                // 判断是否通过扫码登录，发送消息
                if("binduser".equals(bindUserDto.getStrType())){
                    StringBuilder strMsg = new StringBuilder();
                    strMsg.append("您已成功绑定微信，");
                    strMsg.append("<a href='");
                    strMsg.append(hostUrl);
                    strMsg.append("'>点击登录</a>系统");
                    try {
                        wechatService.sentKefuTextMessage(wxMpUser.getOpenId(),strMsg.toString());
                    }
                    catch (WxErrorException e){
                        log.error("【发送客服消息失败】{0}", e);
                    }
                }
                return new AppResponse(true, "绑定成功！");
            } else {
                return new AppResponse(false, "账号或密码错误！");
            }
        }
    }

    /**
     * 判断是否登录
     *
     * @return
     */
    @ResponseBody
    @GetMapping("/jugeLogin")
    public Boolean jugeLogin() {
        if (super.getSession().getAttribute("loginkey") == null) {
            return false;
        }
//        String loginkey = super.getSession().getAttribute("loginkey").toString();
//        Set<Object> islogin = setOperations.members("wechatLogin:" + loginkey);
//        if (islogin.size() == 0) {
//            return false;
//        }
//        User user = (User) islogin.toArray()[0];
//        Subject currentUser = ShiroKit.getSubject();
//        UsernamePasswordWithTypeToken token = new UsernamePasswordWithTypeToken(user.getUserName());
//        currentUser.login(token);
//        ShiroUser shiroUser = ShiroKit.getUser();
//        super.getSession().setAttribute("shiroUser", shiroUser);
//        super.getSession().setAttribute("username", shiroUser.getAccount());
//        //发布登录成功事件
//        publisher.publishEvent(new LoginSuccessEvent(shiroUser.getId(), shiroUser.getAccount()));
        return true;
    }

    /**
     * 微信回调时访问的地址  这里获得code和之前所设置的returnUrl
     *
     * @param code
     * @param returnUrl
     * @return
     */
    @GetMapping("/wechatUserInfo")
    public String wechatUserInfo(HttpServletRequest request, @RequestParam("code") String code, @RequestParam("state") String returnUrl) {
        log.info("微信回调方法开始");
        try (TenantFliter tenantFliter = new TenantFliter(false)) {
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
            try {
                wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
            } catch (WxErrorException e) {
                log.error("【微信网页授权】{}", e);
            }
            String openId = wxMpOAuth2AccessToken.getOpenId();
            EntityWrapper<User> wrapper = new EntityWrapper<>();
            wrapper.eq("openId", openId);
            List<User> users = userService.selectList(wrapper);

            if (users.size() > 0) {
                //用户存在则直接跳转对应系统
                User user = users.get(0);
                String rHostUrl=getHostName(user.getId());
                log.info("微信回调找到用户："+ user.toString()+"回调地址为："+rHostUrl);
                if(StringUtils.isEmpty(rHostUrl))
                {
                    String hostUrl= HttpDomainUtil.getHostAndProctal();
                    return REDIRECT +hostUrl+ "/bindUserSuccess";
                }
                else {
                    return REDIRECT + rHostUrl;
                }
            } else {
                log.info("微信回调没有找到用户");
                try {
                    WxMpUser wxMpUser = wxMpService.getUserService().userInfo(openId);
                    super.getSession().setAttribute("wxmpuser", wxMpUser);
                } catch (WxErrorException e) {
                    log.error("【获取微信授权信息失败】{}", e);
                }
                String hostUrl= HttpDomainUtil.getHostAndProctal();
                log.info("微信回调方法结束，跳转地址："+hostUrl);
                return REDIRECT +hostUrl+ "/account/bindUser?type=" + returnUrl;
            }
        }
    }

    @ResponseBody
    @PostMapping("/loginPhone")
    public Boolean phoneLogin(@RequestParam("key") String key) throws Exception {
        OpenIdSalt openIdSalt = OpenIdSalt.get(key);
        EntityWrapper<User> wrapper = new EntityWrapper<>();
        wrapper.eq("openId", openIdSalt.getOpenId());
        List<User> users = userService.selectList(wrapper);
        if (users.size() > 0) {
            User user = users.get(0);
            Subject currentUser = ShiroKit.getSubject();
            UsernamePasswordWithTypeToken token = new UsernamePasswordWithTypeToken(user.getUserName());
            currentUser.login(token);
            ShiroUser shiroUser = ShiroKit.getUser();
            super.getSession().setAttribute("shiroUser", shiroUser);
            super.getSession().setAttribute("username", shiroUser.getAccount());
            return true;
        } else {
            throw new FriendException("找不到该用户");
        }
    }

    @GetMapping("/userInfoPhone")
    public String userInfoPhone(HttpServletRequest request, @RequestParam("code") String code, @RequestParam("state") String hostName) {
        try (TenantFliter tenantFliter = new TenantFliter(false)) {
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
            try {
                wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
            } catch (WxErrorException e) {
                log.error("【微信网页授权】{}", e);
            }
            String openId = wxMpOAuth2AccessToken.getOpenId();
            EntityWrapper<User> wrapper = new EntityWrapper<>();
            wrapper.eq("openId", openId);
            List<User> users = userService.selectList(wrapper);
            if (users.size() > 0) {
                //用户存在则直接登录
                OpenIdSalt openIdSalt = OpenIdSalt.set(openId);
                return REDIRECT + hostName + "/#/authSuccess?openid=" + openIdSalt.getKey();
            } else {
                try {
                    WxMpUser wxMpUser = wxMpService.getUserService().userInfo(openId);
                    super.getSession().setAttribute("wxmpuser", wxMpUser);
                } catch (WxErrorException e) {
                }
                return REDIRECT + hostName + "/#/focus";
            }
        }
    }

    private String getHostName(Long userId) {
        List<Role> roles = roleService.getRolesByUserId(userId);
        List<String> roleNames = roles.stream().map(b -> b.getName()).collect(Collectors.toList());

        if (roleNames.indexOf("teacher") > -1) {
            return teacherHostName;
        } else if (roleNames.indexOf("student") > -1) {
            return studentHostName;
        } else if (roleNames.indexOf("company") > -1) {
            return companyHostName;
        }
        else if(roleNames.indexOf("schoolManager")>-1)
        {
            return teacherHostName;
        }
        else if(roleNames.indexOf("admin")>-1)
        {
            return teacherHostName;
        }
        else {
            return "";
        }
    }


    /**
     * 绑定用户
     *
     * @return
     */
    @GetMapping("/bindUser")
    public String bindUser(String type,Model model) {
        model.addAttribute("type", type);
        return "/account/wechat/bindUser.html";
    }

    /**
     * 忘记密码
     *
     * @return
     */
    @GetMapping("/wechatforgotpassword")
    public String wechatforgotpassword() {
        return "/account/wechat/forgotPassword.html";
    }

    /**
     * 绑定成功
     * @return
     */
    @GetMapping("/bindUserSuccess")
    public String bindUserSuccess() {
        return "/account/wechat/bindUserSuccess.html";
    }

    /**
     * 通过用户编号获取学生或教师信息
     * @param userId
     * @return
     */
    @RequestMapping(value = "/userbyid", method = RequestMethod.GET)
    @ResponseBody
    @RequiresAuthentication
    public UserPersonInfoDto getUserById(@RequestParam("userid") Long userId) {
            //获取用户信息
            User user = userService.selectById(userId);
            if(user==null){
                throw new FriendException("用户不存在！");
            }
            UserPersonInfoDto personInfoDto = userDtoConverter.userToPersonDto(user);
            //用户头像
            personInfoDto.setAvatar(user.getUserPic());
            //获取用户对应的教师信息
            TeachersDto teachers = teachersService.selectWithDepartment(userId);
            if (teachers != null) {
                //设置类型为教师
//                personInfoDto.setUserType(1);
                personInfoDto.setSchoolIdNumber(teachers.getCode());
            } else {
                // 如果教师信息为null,则查找当前用户是否为学生
                Student student = studentService.getStudentByUserId(userId);
                if (student != null) {
//                    personInfoDto.setUserType(2);
                    personInfoDto.setSchoolIdNumber(student.getCode());
                    personInfoDto.setProfessionName(student.getProfession().getName());
                    personInfoDto.setClassName(student.getGrade().getName());
                }
//                else{
//                    throw new FriendException("用户不存在！");
//                }
            }
            // 创建用户浏览记录
            //学校管理员查看用户信息不做记录
            if(!userId.equals(ShiroKit.getUserId()) && !ShiroKit.getRoles().roleNames.contains("school")) {
                userRecordService.add(ShiroKit.getUserId(), userId);
            }
            return personInfoDto;
    }
}