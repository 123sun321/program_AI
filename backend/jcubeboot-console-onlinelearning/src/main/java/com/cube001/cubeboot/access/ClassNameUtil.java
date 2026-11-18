package com.cube001.cubeboot.access;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: com.cube001.cubeboot.access
 * 类的详细说明
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/12/27 14:36
 */
public class ClassNameUtil {
    /**
     * 获取包下面所有类的名称
     *
     * @param packagePath
     * @return java.util.List<java.lang.String>
     * @description
     * @author anonymous
     * @date 2025/12/27 14:37
     */
    public static List<String> getClassName(String packagePath) throws IOException {
        List<String> fileNames = new ArrayList<>();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        packagePath = packagePath.replace(".", "/");
        Enumeration<URL> urls = loader.getResources(packagePath);
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            if (url == null) {
                continue;
            }
            String type = url.getProtocol();
            if (type.equals("file")) {
                fileNames = getClassNameByFile(url.getPath());
            }

        }
        return fileNames;
    }

    /**
     * 获取文件名称通过文件
     *
     * @param filePath 文件路径
     * @return java.util.List<java.lang.String>
     * @description
     * @author anonymous
     * @date 2025/12/27 14:29
     */
    private static List<String> getClassNameByFile(String filePath) throws UnsupportedEncodingException {
        List<String> myClassName = new ArrayList<>();
        filePath = URLDecoder.decode(filePath, "UTF-8");
        File file = new File(filePath);
        File[] childFiles = file.listFiles();
        if (childFiles == null) {
            return myClassName;
        }
        for (File childFile : childFiles) {
            if (!childFile.isDirectory()) {
                String childFilePath = childFile.getPath();
                if (childFilePath.endsWith(".class")) {
                    File controllerFile = new File(childFilePath);
                    String controllerName = controllerFile.getName();
                    myClassName.add(controllerName.replace(".class", ""));
                }
            }
        }
        return myClassName;
    }



}
