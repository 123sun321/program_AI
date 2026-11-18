package com.cube001.cubeboot.service.wechat;

import com.cube001.cubeboot.core.authorization.entity.User;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;

import javax.servlet.http.HttpSession;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: WechatService
 * 微信service
 *
 * @author anonymous
 * @version 1.0
 * @date 2025 01/08 14:38
 */
public interface WechatService {
    /**
     * 接收处理微信信息
     * @param wxMpXmlMessage
     * @return
     */
    String ReceptionMessage(WxMpXmlMessage wxMpXmlMessage);

    /**
     * 判断用户是否教师或学校管理员
     * @param user
     * @return
     */
    Boolean IsTeacherOrSchoolManager(User user);

    /**
     * 发送客服消息
     * @param openid
     * @param text
     * @return
     * @throws WxErrorException
     */
    Boolean sentKefuTextMessage(String openid,String text) throws WxErrorException;
}
