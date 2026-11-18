package com.cube001.cubeboot.access;

import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: com.cube001.cubeboot.access
 * 类的详细说明
 *
 * @author 陈乐乐
 * @version 1.0
 * @date 2025/12/27 11:07
 */
@Component
public class PermissionContainer {

    @Value("#{'${cubeboot.permissionPackage}'.split(',')}")
    private List<String> permissionPackage;
    private Object loc = new Object();
    /**
     * 维护一个静态列表
     */
    private final List<String> current = new ArrayList<>();

    public List<String> getCurrent() {
        if (current.size() == 0) {
            synchronized (loc) {
                if (current.size() == 0) {
                    for (String pack : permissionPackage) {
                        try {
                            List<String> classNames = ClassNameUtil.getClassName(pack);
                            if (classNames.size() > 0) {
                                for (String className : classNames) {
                                    current.addAll(getPermissionNames(pack + "." + className));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }
        return new ArrayList<>(current);
    }

    private List<String> getPermissionNames(String fullClassName) {
        List<String> names = new ArrayList<>();
        try {
            Class controllerClass = Class.forName(fullClassName);
            //获取Controller的注解
            names.addAll(getRequiresPermissionsValues(controllerClass.getAnnotation(RequiresPermissions.class)));
            // 获取controller方法的注解
            Method[] methods = controllerClass.getMethods();
            for (Method method : methods) {
                names.addAll(getRequiresPermissionsValues(method.getAnnotation(RequiresPermissions.class)));
            }
            names = names.stream().distinct().collect(Collectors.toList());
            return names;

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return names;

    }

    /**
     * 获取注解上的值
     *
     * @param annotation
     * @return java.util.List<java.lang.String>
     * @description
     * @author 陈乐乐
     * @date 2025/12/27 15:18
     */
    private List<String> getRequiresPermissionsValues(Annotation annotation) {

        List<String> permissions = new ArrayList<>();
        if (annotation != null && annotation.annotationType().equals(RequiresPermissions.class)) {
            RequiresPermissions trans = (RequiresPermissions) annotation;
            String[] values = trans.value();
            permissions.addAll(Arrays.asList(values));
        }
        return permissions;
    }


}
