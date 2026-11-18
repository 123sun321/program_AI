package com.cube001.cubeboot.access;

import com.cube001.cubeboot.core.util.SpringContextHolder;

import java.util.List;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: com.cube001.cubeboot.access
 * 权限上下文
 *
 * @author 陈乐乐
 * @version 1.0
 * @date 2025/12/27 15:28
 */
public class PermissionContext {
    /**
     * 获取所有权限
     *
     * @param
     * @return java.util.List<java.lang.String>
     * @description
     * @author 陈乐乐
     * @date 2025/12/27 15:32
     */
    private static PermissionContainer permissionContainer;

    public static List<String> getPermissions() {
        return getPermissionContainer().getCurrent();
    }

    private static PermissionContainer getPermissionContainer(){
        if (permissionContainer == null) {
            permissionContainer = SpringContextHolder.getBean(PermissionContainer.class);
        }
        return permissionContainer;
    }
}
