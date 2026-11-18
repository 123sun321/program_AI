package com.cube001.cubeboot.controller;

import cn.hutool.json.JSONObject;
import com.cube001.cubeboot.core.base.controller.BaseController;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.websocket.WebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: PhoneController
 * PhoneController
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/2/21 13:31
 */
@Controller
@RequestMapping("/phone")
public class PhoneController extends BaseController {
    private static String identification="zxy15011501";

    /**
     * 控制大屏登录页
     * @return
     */
    @GetMapping("/controllogin")
    public String controllogin(){
        return "/phone/controllogin.html";
    }
    /**
     * 遥控页面
     * @return
     */
    @GetMapping("/telecontrol/{keyword}")
    public String telecontrol(@PathVariable String keyword, HttpSession session, Model model) {
        if(null == session.getAttribute("telecontrol") || null == keyword){
            return REDIRECT+"/phone/controllogin";
        }
        model.addAttribute("keyword", keyword);
        return "/phone/telecontrol.html";
    }

    /**
     * 绑定大屏
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/controllogin", method = RequestMethod.POST)
    @ResponseBody
    public String controllogin(@RequestBody JSONObject jsonObject, HttpSession session) {
        String username = jsonObject.get("userName").toString();
        if(!identification.equals(username)){
            throw new FriendException("控制密钥不合法！");
        }
        String keyword = jsonObject.get("keyword").toString();
        if(!WebSocket.getWebSocket(keyword)){
            throw new FriendException("大屏不存在！");
        }
        session.setAttribute("telecontrol",true);
        return keyword;
    }
    /**
     * 发送操作
     * @param jsonObject
     */
    @RequestMapping(value = "/pushToWeb", method = RequestMethod.POST)
    @ResponseBody
    public void pushToWeb(@RequestBody JSONObject jsonObject){
        // up-"上移" left-"左移" right-"右移" down-"下移" ok-"确认" back-"返回"
        String type=jsonObject.get("type").toString();
        String keyword=jsonObject.get("keyword").toString();
        try {
            WebSocket.sendOperation(type, keyword);
        }
        catch (Exception e){
            throw new FriendException("WebSocket连接错误！");
        }
    }
}
