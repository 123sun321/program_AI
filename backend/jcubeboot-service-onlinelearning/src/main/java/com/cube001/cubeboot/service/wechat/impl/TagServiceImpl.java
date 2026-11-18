package com.cube001.cubeboot.service.wechat.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.cube001.cubeboot.constant.Wechat;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.core.weChatSdk.WeChatAuthorizationManager;
import com.cube001.cubeboot.service.wechat.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: com.cube001.cubeboot.service.wechat.impl
 * 标签Service
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/12/25 10:02
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private WeChatAuthorizationManager weChatAuthorizationManager;

    @Override
    public Boolean createTag(String tagName) throws Exception {
        String accessToken = weChatAuthorizationManager.getAccessToken();
        Map<String, Map<String, String>> tagObj = new HashMap<>(1);
        Map<String, String> tag = new HashMap<>(1);
        tag.put("name", tagName);
        tagObj.put("tag", tag);

        HttpRequest httpRequest = HttpUtil.createPost(Wechat.URL_TAG_CREATE + accessToken);
        httpRequest.body(JSONObject.toJSONString(tagObj));
        HttpResponse httpResponse = httpRequest.execute();

        HashMap<String, Object> body = JSONObject.parseObject(httpResponse.body(), HashMap.class);

        if (body.containsKey("errcode") && !body.get("errcode").equals(Wechat.ERR_TAG_REPEAT_NUMBER)) {
            if (body.get("errcode").equals(Wechat.ERR_TAG_OUT_NUMBER)) {
                throw new FriendException("标签过多,请删除未使用的标签");
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public JSONObject getTags() throws Exception {
        String accessToken = weChatAuthorizationManager.getAccessToken();
        HttpRequest httpRequest = HttpUtil.createGet(Wechat.URL_TAG_GET + accessToken);
        HttpResponse httpResponse = httpRequest.execute();
        return JSONObject.parseObject(httpResponse.body());

    }
}
