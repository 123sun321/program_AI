package com.cube001.cubeboot.service.wechat.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cube001.cubeboot.constant.Wechat;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.core.weChatSdk.WeChatAuthorizationManager;
import com.cube001.cubeboot.service.wechat.TagService;
import com.cube001.cubeboot.service.wechat.MenuService;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.menu.WxMpMenu;
import me.chanjar.weixin.mp.bean.tag.WxUserTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: com.cube001.cubeboot.service.wechat.impl
 * 菜单Service
 *
 * @author 陈乐乐
 * @version 1.0
 * @date 2025/12/25 9:50
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private WeChatAuthorizationManager weChatAuthorizationManager;
    @Autowired
    private TagService tagService;

    @Resource
    private WxMpService wxMpService;

    private static HashMap<String,WxUserTag> userTagHashMap=new HashMap<>();

    @Override
    public List<Boolean> createMenu(List<JSONObject> datas) throws Exception {
        //为了创建用户个性化菜单，先获取所有的标签列表
        List<WxUserTag> res = wxMpService.getUserTagService().tagGet();
        for (WxUserTag tag:res
             ) {
            userTagHashMap.put(tag.getName(),tag);
        }

        Boolean isHaveDef = false;
        for (JSONObject data : datas) {
            if (null == data.getJSONObject("matchrule")) {
                isHaveDef = createDefMenu(data);
                break;
            }
        }
        List<Boolean> sucs = new ArrayList<>();
        if (!isHaveDef) {
            throw new FriendException("默认菜单创建失败");
        } else {
            for (JSONObject data : datas) {
                if (null != data.getJSONObject("matchrule")) {
                    sucs.add(createCustomMenu(data));
                }else {
                    sucs.add(true);
                }
            }
        }
        return sucs;
    }

    @Override
    public Object getMenu() throws Exception {
        String accessToken = weChatAuthorizationManager.getAccessToken();
        HttpRequest request = HttpUtil.createGet(Wechat.URL_MENU_GET + accessToken);
        HttpResponse httpResponse = request.execute();
        //JSONObject.p
        Object body = JSONObject.parse(httpResponse.body());
        return body;
    }

    @Override
    public Boolean delete() throws Exception {
        String accessToken = weChatAuthorizationManager.getAccessToken();
        HttpRequest request = HttpUtil.createGet(Wechat.URL_MENU_DELETE + accessToken);
        HttpResponse httpResponse = request.execute();
        HashMap<String, Object> body = JSONObject.parseObject(httpResponse.body(), HashMap.class);
        return body.get("errcode").equals(0);
    }

    private Boolean createDefMenu(JSONObject data) throws Exception {
        String accessToken = weChatAuthorizationManager.getAccessToken();
        HttpRequest httpRequest = HttpUtil.createPost(Wechat.URL_MENU_DEFAULT_CREATE + accessToken);
        httpRequest.body(data.toJSONString());
        HttpResponse httpResponse = httpRequest.execute();
        HashMap<String, Object> body = JSONObject.parseObject(httpResponse.body(), HashMap.class);
        if (!body.get("errcode").equals(0)) {
            return false;
        } else {
            return true;
        }
    }

    private Boolean createCustomMenu(JSONObject data) throws Exception {
        String accessToken = weChatAuthorizationManager.getAccessToken();
        HttpRequest httpRequest = HttpUtil.createPost(Wechat.URL_MENU_CUSTOM_CREATE + accessToken);
        Object tagNameObj = data.getJSONObject("matchrule").get("tag_id");
        if (null != tagNameObj) {
            //先判断系统中是否已有此标签
            if(userTagHashMap.containsKey(tagNameObj.toString()))
            {
                //如果有，将名称替换为id
                data.getJSONObject("matchrule").put("tag_id",userTagHashMap.get(tagNameObj.toString()).getId());
            }
            else
            {
                //如果没有，则创建
                WxUserTag res = wxMpService.getUserTagService().tagCreate(tagNameObj.toString());
                userTagHashMap.put(tagNameObj.toString(),res);
                data.getJSONObject("matchrule").put("tag_id",res.getId());
            }
        } else {
            throw new FriendException("标签名不正确");
        }
        httpRequest.body(data.toJSONString());
        HttpResponse httpResponse = httpRequest.execute();
        HashMap<String, Object> body = JSONObject.parseObject(httpResponse.body(), HashMap.class);
        if (body.containsKey("errcode")) {
            return false;
        } else {
            return true;
        }
    }


}
