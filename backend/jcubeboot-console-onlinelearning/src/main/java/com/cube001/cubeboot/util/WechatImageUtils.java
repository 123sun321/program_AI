package com.cube001.cubeboot.util;

import com.cube001.cubeboot.core.config.properties.CubeBootProperties;
import com.cube001.cubeboot.core.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [文件名]
 * 类的详细说明
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/2/22 14:06
 */
public class WechatImageUtils {

    /**
     * @param serverId(注：图片上传完微信服务器后返回的serverId)
     * @return
     */
    public String saveImage(String url,String serverId,String accessToken) {
        String filePath="";
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        String profixTime = sdf1.format(new Date());
        String image_number = UUID.randomUUID().toString().replace("-", "").substring(0, 8)+".png";
        String temp_path=  url + File.separator + profixTime;//保存路径

        File saveFile = new File(temp_path);
        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }

        try {
            filePath = saveImageToDisk(serverId, image_number, temp_path,accessToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String realtivePath = "/upload/" + profixTime + "/" + filePath;
        return realtivePath;
    }

    private String saveImageToDisk(String serverId, String picName, String picPath,String accessToken) throws Exception {

//        Weixin weixin = new Weixin(ConfigData.weixin_app_id, ConfigData.weixin_secret, ConfigData.weixin_cache_dir);//此步骤你自己创建自己的weixin对象

        InputStream inputStream = getInputStream(accessToken, serverId);

        // 循环取出流中的数据
        byte[] data = new byte[1024];
        int len = 0;
        FileOutputStream fileOutputStream = null;
        String filePath = picPath + picName;

        try {
            String savePath=  picPath+"\\"+picName;
            fileOutputStream = new FileOutputStream(savePath);
            while ((len = inputStream.read(data)) != -1) {
                fileOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                }
            }
        }
        return picName;
    }
    //获取
    private static InputStream getInputStream(String accessToken, String mediaId) {
        InputStream is = null;
        String url = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=" + accessToken + "&media_id=" + mediaId;
        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
            http.connect();
            // 获取文件转化为byte流
            is = http.getInputStream();
        } catch (Exception e) {
        }
        return is;

    }
}
