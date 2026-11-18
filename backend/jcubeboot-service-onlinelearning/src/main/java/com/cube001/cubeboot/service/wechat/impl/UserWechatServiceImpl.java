package com.cube001.cubeboot.service.wechat.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.service.wechat.UserWechatService;
import com.cube001.cubeboot.service.wechat.mapper.UserWechatMapper;
import com.cube001.cubeboot.wechat.entity.UserWechat;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.tag.WxUserTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: UserWechatServiceImpl
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/01/09 09:52
 */
@Service
@Slf4j
public class UserWechatServiceImpl extends ServiceImpl<UserWechatMapper, UserWechat> implements UserWechatService {

    @Resource
    private WxMpService wxMpService;

    @Override
    public Boolean add(WxMpUser wxMpUser,Long userId,String tag){
        UserWechat userWechat = new UserWechat();
        userWechat.setUserId(userId);
        userWechat.setSubscribe(wxMpUser.getSubscribe());
        userWechat.setOpenId(wxMpUser.getOpenId());
        userWechat.setNickname(wxMpUser.getNickname());
        userWechat.setSex(wxMpUser.getSex());
        userWechat.setLanguage(wxMpUser.getLanguage());
        userWechat.setCity(wxMpUser.getCity());
        userWechat.setProvince(wxMpUser.getProvince());
        userWechat.setCountry(wxMpUser.getCountry());
        userWechat.setHeadImgUrl(wxMpUser.getHeadImgUrl());
        userWechat.setUnionId(wxMpUser.getUnionId());
        userWechat.setRemark(wxMpUser.getRemark());
        this.baseMapper.insert(userWechat);
        //为用户打标签
        try {
            // 先取消用户身上的所有标签
            List<Long> userTags = wxMpService.getUserTagService().userTagList(wxMpUser.getOpenId());
            for(Integer i=0;i<userTags.size();i++){
                wxMpService.getUserTagService().batchUntagging(userTags.get(i),new String[]{wxMpUser.getOpenId()});
            }
            // 为用户打对应的一个标签
            List<WxUserTag> tags = wxMpService.getUserTagService().tagGet().stream().filter(m->m.getName().equals(tag))
                    .collect(Collectors.toList());
            WxUserTag wxUserTag = null;
            if(tags.size() > 0){
                wxUserTag = tags.get(0);
            }
            else{
                wxUserTag = wxMpService.getUserTagService().tagCreate(tag);
            }
            wxMpService.getUserTagService().batchTagging(wxUserTag.getId(),new String[]{wxMpUser.getOpenId()});
        }
        catch (WxErrorException wxe){
            //报异常
            log.error("【为用户打标签失败】{0}", wxe);
        }
        return true;
    }
}
