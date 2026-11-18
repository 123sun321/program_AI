package com.cube001.cubeboot.service.wechat.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cube001.cubeboot.base.entity.Teachers;
import com.cube001.cubeboot.core.authorization.entity.User;
import com.cube001.cubeboot.core.authorization.mapper.UserMapper;
import com.cube001.cubeboot.core.domain.tenant.TenantFliter;
import com.cube001.cubeboot.service.base.mapper.TeacherMapper;
import com.cube001.cubeboot.service.wechat.WechatService;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;
import me.chanjar.weixin.mp.util.xml.XStreamTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: WechatServiceImpl
 * 微信serviceImpl
 *
 * @author anonymous
 * @version 1.0
 * @date 2025 01/08 14:40
 */
@Service
public class WechatServiceImpl implements WechatService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private SetOperations<String, Object> setOperations;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    @Value("${cubeboot.app-host-name}")
    private String hostName;
    @Autowired
    private WxMpService wxMpService;

    @Override
    public String ReceptionMessage(WxMpXmlMessage wxMpXmlMessage) {
        try (TenantFliter tenantFliter = new TenantFliter(false)) {
            if ("subscribe".equals(wxMpXmlMessage.getEvent()) || "SCAN".equals(wxMpXmlMessage.getEvent())) {
                String openid = wxMpXmlMessage.getFromUser();
                //获取二维码回调参数
                String loginkey = wxMpXmlMessage.getEventKey();
                if(loginkey.indexOf("qrcodelogin_")>-1){
                    String[] arrayKey = loginkey.split("_");
                    loginkey = arrayKey[arrayKey.length-1];
                    EntityWrapper<User> wrapper = new EntityWrapper<>();
                    wrapper.eq("openId", openid);
                    List<User> userList = userMapper.selectList(wrapper);
                    if (userList.size() > 0) {
                        User user = userList.get(0);
                        //判断是否是教师
                        if(IsTeacherOrSchoolManagerHelper(user)){
                            //设置redis有效期
                            String key = "wechatLogin:" + loginkey;
                            setOperations.add(key, user);
                            redisTemplate.expire(key, 60, TimeUnit.SECONDS);
                            return ReText(wxMpXmlMessage, "登录成功！");
                        }
                        else {
                            return ReText(wxMpXmlMessage, "您没有登录权限！");
                        }
                    }
                    else{
                        return ReBindUser(wxMpXmlMessage,"qrcodelogin");
                    }
                } else if(loginkey.indexOf("binduser_")>-1){
                    return ReBindUser(wxMpXmlMessage,"binduser");
                }
            }
            return null;
        }
    }

    /**
     * 绑定回复
     * @param wxMpXmlMessage
     * @return
     */
    private String ReBindUser(WxMpXmlMessage wxMpXmlMessage,String strType) {
        StringBuilder sb = new StringBuilder();
        sb.append("您的账号尚未绑定微信，请<a href='");
        sb.append(hostName);
        sb.append("/account/wechatLogin?type=");
        sb.append(strType);
        sb.append("'>点击进入</a>进行绑定。");
        return ReText(wxMpXmlMessage, sb.toString());
    }
    /**
     * 回複信息
     *
     * @param wxMpXmlMessage
     * @return
     */
    private String ReText(WxMpXmlMessage wxMpXmlMessage, String content) {
        WxMpXmlOutTextMessage wxMpXmlOutTextMessage = new WxMpXmlOutTextMessage();
        wxMpXmlOutTextMessage.setToUserName(wxMpXmlMessage.getFromUser());
        wxMpXmlOutTextMessage.setContent(content);
        wxMpXmlOutTextMessage.setFromUserName(wxMpXmlMessage.getToUser());
        wxMpXmlOutTextMessage.setCreateTime(System.currentTimeMillis());
        String msg = XStreamTransformer.toXml(WxMpXmlOutTextMessage.class, wxMpXmlOutTextMessage);
        return msg;
    }

    /**
     * 发送客服消息
     * @param openid
     * @param text
     * @return
     * @throws WxErrorException
     */
    @Override
    public Boolean sentKefuTextMessage(String openid,String text) throws WxErrorException{
        WxMpKefuMessage wxMpKefuMessage = new WxMpKefuMessage();
        wxMpKefuMessage.setToUser(openid);
        wxMpKefuMessage.setMsgType("text");
        wxMpKefuMessage.setContent(text);
        return wxMpService.getKefuService().sendKefuMessage(wxMpKefuMessage);
    }
    /**
     * 判断用户是否是教师或学校管理员
     * @param user
     * @return
     */
    @Override
    public Boolean IsTeacherOrSchoolManager(User user){
       return IsTeacherOrSchoolManagerHelper(user);
    }
    private Boolean IsTeacherOrSchoolManagerHelper(User user){
        Teachers teachers= teacherMapper.selectWithUser(user.getId());
        return true;
    }
}
