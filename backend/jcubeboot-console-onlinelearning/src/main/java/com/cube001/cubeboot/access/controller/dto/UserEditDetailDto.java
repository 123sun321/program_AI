package com.cube001.cubeboot.access.controller.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.cube001.cubeboot.core.authorization.shiro.UserTypeEnum;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.EnumSet;

/**
 * 用户编辑传输实体
 * @Author:anonymous
 * @Description:
 * @Date:Create in 11:53 2018/07/11
 * @Modified By:
 */
@Data
public class UserEditDetailDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    /**
     * 用户类型 (前台用户-1|店铺管理用户-2|后台运营管理用户-4)
     * 一个用户可以属于多种类型，使用位运算方式存储
     * */
    @NotBlank
    private Integer userType;
    @NotBlank
    private String name;
    private String surname;
    @NotBlank
    private String userName;
    private String password;
    private String confimPassword;
    @NotBlank
    @Email
    private String emailAddress;
    private Boolean emailConfirmed;
    private Date lastLoginTime;
    private Boolean active;
    private Date lockoutEndDateUtc;
    private Integer accessFailedCount;
    private Boolean lockoutEnabled;
    @Pattern(regexp="(?:0|86|\\+86)?1[3456789]\\d{9}",message = "{reg.phone.noPhoneNumber}")
    @NotBlank
    @Size(min=11,max=11,message="{reg.phone.size}")
    private String phoneNumber;
    private Boolean phoneNumberConfirmed;
    private String qq;
    private String userPic;
    private String weiXin;
    private String idCard;
    @NotBlank
    private Integer sex;
    private Boolean shouldChangePasswordOnNextLogin;
    private String areaCode;
    private String openId;
    private String unionId;
    private Date creationTime;
    private String creatorUserId;

    @JSONField(name="userTypes")
    public EnumSet<UserTypeEnum> getUserTypes(){
        EnumSet<UserTypeEnum> userTypeEnums= EnumSet.noneOf(UserTypeEnum.class);
        if((this.userType & UserTypeEnum.FrontUser.value())== UserTypeEnum.FrontUser.value())
        {
            userTypeEnums.add(UserTypeEnum.FrontUser);
        }
        if((this.userType & UserTypeEnum.ShopManagerUser.value())== UserTypeEnum.ShopManagerUser.value())
        {
            userTypeEnums.add(UserTypeEnum.ShopManagerUser);
        }
        if((this.userType & UserTypeEnum.ManagerUser.value())== UserTypeEnum.ManagerUser.value())
        {
            userTypeEnums.add(UserTypeEnum.ManagerUser);
        }
        return userTypeEnums;
    }

    public void setUserTypes(EnumSet<UserTypeEnum> userTypes)
    {
        Integer value=0;
        for (UserTypeEnum ut :userTypes) {
            value=value | ut.value();
        }
        this.userType=value;
    }

}
