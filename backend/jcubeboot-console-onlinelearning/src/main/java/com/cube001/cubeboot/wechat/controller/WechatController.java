package com.cube001.cubeboot.wechat.controller;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cube001.cubeboot.core.base.controller.BaseController;
import com.cube001.cubeboot.core.config.properties.CubeBootProperties;
import com.cube001.cubeboot.core.domain.tenant.TenantFliter;
import com.cube001.cubeboot.service.wechat.WechatService;
import com.cube001.cubeboot.util.WechatImageUtils;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.util.xml.XStreamTransformer;
import org.apache.ibatis.annotations.Param;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: WechatController
 * 微信授权
 *
 * @author Juci
 * @version 1.0
 * @date 2025/12/29 14:58
 */

@Controller
@RequestMapping("/wechat")
@Slf4j
public class WechatController extends BaseController {

    @Autowired
    private SetOperations<String, Object> setOperations;
    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private WechatService wechatService;
    @Autowired
    private CubeBootProperties properties;

    @Autowired
    @Value("${cubeboot.app-host-name}")
    private String hostName;

    @Autowired
    @Value("${cubeboot.wx.token}")
    private String TOKEN;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 与公众号服务器配置进行比对
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public void checkName(@RequestParam("signature") String signature,
                          @RequestParam("timestamp") String timestamp,
                          @RequestParam("nonce") String nonce,
                          @RequestParam("echostr") String echostr, HttpServletResponse res) {
        logger.info("微信-开始校验签名");
        logger.info("收到来自微信的 echostr 字符串:{}", echostr);
        PrintWriter out = null;
        try {
            out = res.getWriter();

//        加密/校验流程如下:
//        1. 将token、timestamp、nonce三个参数进行字典序排序
//        2. 将三个参数字符串拼接成一个字符串进行sha1加密
//        3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信

            // 1.排序
            String sortString = sort(TOKEN, timestamp, nonce);
            // 2.sha1加密
            String myString = sha1(sortString);
            // 3.字符串校验
            if (myString != null && myString != "" && myString.equals(signature)) {
                logger.info("微信-签名校验通过");
                //如果检验成功原样返回echostr，微信服务器接收到此输出，才会确认检验完成。
                logger.info("回复给微信的 echostr 字符串:{}", echostr);
                out.print(echostr);

            } else {
                logger.error("微信-签名校验失败");
                out.print("");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
            out = null;
        }
    }


    /**
     * 访问这个时便会发起微信的网页授权
     *
     * @param response
     * @param keyword
     * @param sessionid
     * @return
     */
    @GetMapping("/authorize")
    public String authorize(HttpServletResponse response, @RequestParam("keyword") String keyword, @RequestParam("sessionid") String sessionid) {
        System.out.print("进入回调方法");
        try (TenantFliter tenantFliter = new TenantFliter(false)) {
//            //设置回调地址
//            String url = hostName + "/wechat/userInfo";
//            //获取报告实体
//            EntityWrapper<ReportShare> wrapper = new EntityWrapper<>();
//            wrapper.like("url", keyword, SqlLike.LEFT);
//            List<ReportShare> reportShareList = reportShareService.selectList(wrapper);
//            if(reportShareList==null || reportShareList.size()==0)
//            {
//                return "/report/nofind.html";
//            }
//            System.out.print("找到报告"+reportShareList.get(0).toString());
//            String returnUrl = reportShareList.get(0).getUrl() + "@" + sessionid;
//            try {
//                returnUrl = URLEncoder.encode(returnUrl, "UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                log.error("UnsupportedEncodingException");
//            }
//            String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAUTH2_SCOPE_USER_INFO, returnUrl);
//            return REDIRECT + redirectUrl;
            return "/report/nofind.html";
        }
    }

    /**
     * 微信回调时访问的地址  这里获得code和之前所设置的returnUrl
     *
     * @param code
     * @param returnUrl
     * @return
     */
    @GetMapping("/userInfo")
    public String userInfo(HttpServletRequest request, @RequestParam("code") String code, @RequestParam("state") String returnUrl) {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        try {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
            log.error("【微信网页授权】{}", e);
        }
        WxMpUser wxMpUser = null;
        try {
            wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, "zh_CN");
        } catch (WxErrorException e) {
            log.error("【微信获取用户信息报错】{}", e);
        }
        String[] url = returnUrl.split("@");
        String key = "wechatUserInfo:" + url[1];
        setOperations.add(key, wxMpUser);
        redisTemplate.expire(key, 20*60, TimeUnit.SECONDS);
        return REDIRECT + url[0];
    }

    /**
     * 获取生成二维码的Ticket
     *
     * @return
     */
    @ResponseBody
    @GetMapping("/qrCodeCreateTmpTicket")
    public String qrCodeCreateTmpTicket(@RequestParam(value = "prefix", required = false) String prefix) {
        WxMpQrCodeTicket wxMpQrCodeTicket = new WxMpQrCodeTicket();
        String loginkey = null;
        // 通过随机字符串标识
        String strRandom = RandomUtil.randomString(32);
        if (prefix == null || prefix.length() == 0) {
            loginkey = strRandom;
        }
        else{
            loginkey = prefix + "_" + strRandom;
        }
        try {
            wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(loginkey, 120);
            super.getSession().setAttribute("loginkey", strRandom);
        } catch (WxErrorException e) {
            log.error("【生成二维码报错】{0}", e);
        }
        String ticket = wxMpQrCodeTicket.getTicket();
        try {
            ticket = URLEncoder.encode(ticket, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException");
        }
        return ticket;
    }

    /**
     * 获取微信端发送的信息
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public String wechat() {
        System.out.println("接收到微信事件通知");
        HttpServletRequest request = super.getHttpServletRequest();
        StringBuilder sb = new StringBuilder();
        try {
            // 获取输入流
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            String line = null;
            while ((line = streamReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("消息内容：" + sb.toString());
        WxMpXmlMessage wxMpXmlMessage = XStreamTransformer.fromXml(WxMpXmlMessage.class, sb.toString());
        return wechatService.ReceptionMessage(wxMpXmlMessage);
    }

    /**
     * 排序方法
     *
     * @param token     Token
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @return
     */
    private String sort(String token, String timestamp, String nonce) {
        String[] strArray = {token, timestamp, nonce};
        Arrays.sort(strArray);
        StringBuilder sb = new StringBuilder();
        for (String str : strArray) {
            sb.append(str);
        }

        return sb.toString();
    }

    /**
     * 将字符串进行sha1加密
     *
     * @param str 需要加密的字符串
     * @return 加密后的内容
     */
    public String sha1(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();
            // 创建 16进制字符串
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
    * @description 获取JSSDK签名
    * @author 郜强
    * @date 2025/1/18 13:56
    * @param []
    * @return 签名相关数据
    */
    @ResponseBody
    @GetMapping("/getJsSdkSignature")
    public String getJsSdkSignature(@RequestParam(value = "url") String url){
        WxJsapiSignature signature=new WxJsapiSignature();
        try{
            String ticket= wxMpService.getJsapiTicket();
            signature=wxMpService.createJsapiSignature(url);
        }catch (WxErrorException e){
            log.error("生成签名错误：{0}", e);
        }

        return new JSONObject(signature).toString();
    }

    @ResponseBody
    @PostMapping("/getWechatPic")
    public String getWechatPic(@RequestParam(value = "serverId") String serverId){
        String filePath="";
        try{

            String token=wxMpService.getAccessToken();
            WechatImageUtils utils=new WechatImageUtils();
            filePath= utils.saveImage(properties.getFileUploadPath(),serverId,token);
        }
        catch (WxErrorException e){
            log.error("生成ToKen错误：{0}",e);
        }
        return filePath;
    }

}
