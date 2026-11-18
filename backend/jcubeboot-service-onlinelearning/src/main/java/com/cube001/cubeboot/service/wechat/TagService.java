package com.cube001.cubeboot.service.wechat;

import com.alibaba.fastjson.JSONObject;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: com.cube001.cubeboot.service.wechat
 * 标签service
 *
 * @author 陈乐乐
 * @version 1.0
 * @date 2025/12/25 10:01
 */
public interface TagService {
    /**
     * 创建标签
     *
     * @param tagName 标签名称
     * @return java.lang.Boolean
     * @description
     * @author 陈乐乐
     * @date 2025/12/25 9:59
     */
    Boolean createTag(String tagName) throws Exception;

    JSONObject getTags() throws Exception;
}
