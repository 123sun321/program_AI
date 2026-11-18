package com.cube001.cubeboot.access.controller.dto;

import lombok.Data;

/**
 * @Author:张向宇
 * @Description:
 * @Date:Create in 15:58 2018/03/20
 * @Modified By:
 */
@Data
public class UserDto {

    private String name;
    private String alipayAccount;
    private String userPic;
    private String avatar;
    private String[] roles;
}
