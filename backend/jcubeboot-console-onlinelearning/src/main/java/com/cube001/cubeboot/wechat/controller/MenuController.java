package com.cube001.cubeboot.wechat.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.core.weChatSdk.WeChatAuthorizationManager;
import com.cube001.cubeboot.service.wechat.MenuService;
import com.cube001.cubeboot.service.wechat.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: com.cube001.cubeboot.weChat
 * 微信菜单
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/12/24 14:10
 */
@Api(value = "微信交互接口")
@RestController
@RequestMapping(value = "${cubeboot.app-manager-path}/wechat")
public class MenuController implements IApplicationService {

    @Autowired
    private MenuService menuService;

    @Autowired
    private TagService tagService;


    @ApiOperation(value = "获取微信菜单")
    @GetMapping(value = "menus/get")
    public Object getMenu() throws Exception {
        return menuService.getMenu();
    }

    @ApiOperation(value = "上传至服务器")
    @PostMapping(value = "menus/change")
    public List<Boolean> changeMenu(@RequestBody List<JSONObject> datas) throws Exception {
        Boolean suc = menuService.delete();
        if (!suc) {
            throw new FriendException("创建失败");
        }
        List<Boolean> sucs = menuService.createMenu(datas);
        return sucs;
    }
    @ApiOperation(value = "获取用户标签")
    @GetMapping(value = "tags/list")
    public JSONObject getTags() throws Exception {
        return tagService.getTags();
    }


}
