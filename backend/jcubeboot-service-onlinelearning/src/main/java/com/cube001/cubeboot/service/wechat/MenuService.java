package com.cube001.cubeboot.service.wechat;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: com.cube001.cubeboot.service.wechat
 * 微信菜单service
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/12/25 9:49
 */
public interface MenuService {

    /**
     * 创建菜单
     *
     * @param datas
     * @return
     */
    List<Boolean> createMenu(List<JSONObject> datas) throws Exception;

    /**
     * 获取菜单
     *
     * @return java.lang.Object
     * @description
     * @author anonymous
     * @date 2025/12/25 9:56
     */
    Object getMenu() throws Exception;

    Boolean delete() throws Exception;



}
