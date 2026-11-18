package com.cube001.cubeboot.console.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 前台用户注册dto
 * @Author:anonymous
 * @Description:
 * @Date:Create in 10:47 2018/06/05
 * @Modified By:
 */
@Data
public class RegisterDto {

    /**
     * 注册邀请码
     * */
    private String inviteCode;

    /**
     * 手机号
     * */
    @Pattern(regexp="(?:0|86|\\+86)?1[3456789]\\d{9}",message = "{reg.phone.noPhoneNumber}")
    @NotBlank
    @Size(min=11,max=11,message="{reg.phone.size}")
    private String phone;

    /**
     * 图形验证码
     * */
    @NotBlank
    private String picCaptcha;

    /**
     * 手机验证码
     * */
    @NotBlank
    private String phoneCaptcha;
    /**
     * 密码
     * */
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$",message = "密码必须是8-20位的数字和字母的组合")
    private String password;

    private String openid;
}
