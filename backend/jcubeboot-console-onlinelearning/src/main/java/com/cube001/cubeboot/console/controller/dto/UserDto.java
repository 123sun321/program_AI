package com.cube001.cubeboot.console.controller.dto;

import lombok.Data;

/**
 * @Author:anonymous
 * @Description:
 * @Date:Create in 15:58 2018/03/20
 * @Modified By:
 */
@Data
public class UserDto {
    private Long id;
    private String name;
    private String avatar;
    private String userPic;
    private String alipayAccount;

    private String[] roles;
}
