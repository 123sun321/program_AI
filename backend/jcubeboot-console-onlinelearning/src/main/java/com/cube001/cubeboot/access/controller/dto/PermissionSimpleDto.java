package com.cube001.cubeboot.access.controller.dto;

import lombok.Data;

/**
 * 权限传输简易信息
 * @Author:anonymous
 * @Description:
 * @Date:Create in 17:57 2018/07/21
 * @Modified By:
 */
@Data
public class PermissionSimpleDto {
    /**
     * 编号
     * */
    private Integer id;
    /**
     * 权限名称
     * */
    private String name;
    /**
     * 是否授予
     * */
    private Boolean grant;

    private Integer dataType;

}
