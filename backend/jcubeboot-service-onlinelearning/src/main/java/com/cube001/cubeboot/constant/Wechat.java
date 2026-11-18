package com.cube001.cubeboot.constant;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: com.cube001.cubeboot.constant
 * 类的详细说明
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/12/25 9:54
 */
public class Wechat {

    /**
     * 微信菜单获取
     */
    public final static String URL_MENU_GET = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=";
    /**
     * 微信个性化菜单创建
     */
    public final static String URL_MENU_CUSTOM_CREATE = "https://api.weixin.qq.com/cgi-bin/menu/addconditional?access_token=";
    public final static String URL_MENU_DEFAULT_CREATE = " https://api.weixin.qq.com/cgi-bin/menu/create?access_token=";

    public final static String URL_MENU_DELETE = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=";
    /**
     * 微信标签创建
     */
    public final static String URL_TAG_CREATE = "https://api.weixin.qq.com/cgi-bin/tags/create?access_token=";
    public final static String URL_TAG_GET = "https://api.weixin.qq.com/cgi-bin/tags/get?access_token=";

    /**
     * 标签超出最大数返回码
     */
    public final static Integer ERR_TAG_OUT_NUMBER = 45056;

    /**
     * 标签重复返回码
     */
    public final static Integer ERR_TAG_REPEAT_NUMBER = 45157;
}
