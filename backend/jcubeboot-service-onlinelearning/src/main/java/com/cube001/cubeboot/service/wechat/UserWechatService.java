package com.cube001.cubeboot.service.wechat;

import com.baomidou.mybatisplus.service.IService;
import com.cube001.cubeboot.wechat.entity.UserWechat;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: UserWechatService
 * 微信用户Service
 *
 * @author anonymous
 * @date 2025/01/09 09:49
 * @version 1.0
 */
public interface UserWechatService extends IService<UserWechat> {
    /**
     * 添加用戶的微信信息
     * @param wxMpUser
     * @param userId
     * @param tag
     * @return
     */
    Boolean add(WxMpUser wxMpUser, Long userId,String tag);
}
