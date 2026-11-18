package com.cube001.cubeboot.access.controller.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author:anonymous
 * @Description:
 * @Date:Create in 11:55 2018/07/10
 * @Modified By:
 */
@Data
public class UserListItemDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Integer tenantid;

    /**
     * 用户类型 (前台用户-1|店铺管理用户-2|后台运营管理用户-4)
     * 一个用户可以属于多种类型，使用位运算方式存储
     * */
    private Integer userType;
    private String name;
    private String surname;
    private String userName;

    private String emailAddress;

    private Date lastLoginTime;
    private Boolean active;
    private Boolean lockoutEnabled;
    private String phoneNumber;
    private Boolean phoneNumberConfirmed;

    private String qq;
    private String userPic;
    private String weiXin;
    private Integer sex;
    private Date creationTime;

    private Integer roleCount;

    public String getUserPic() {
        if(StringUtils.isEmpty(userPic))
        {
            return "/static/web/images/noavatar_middle.gif";
        }
        else {
            return userPic;
        }
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }
}
