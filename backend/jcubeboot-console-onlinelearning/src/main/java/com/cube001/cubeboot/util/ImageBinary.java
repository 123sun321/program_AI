package com.cube001.cubeboot.util;

import com.cube001.cubeboot.core.util.FileUtil;
import org.apache.commons.codec.digest.DigestUtils;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;



public class ImageBinary {


    public static byte[] toByteArray(InputStream in) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    /**
     * 网络文件转换为byte二进制
     *
     * @param @param  url
     * @param @return
     * @param @throws IOException    设定文件
     * @return byte[]    返回类型
     * @throws
     * @Title: toByteArray
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public static byte[] toByteArray(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        DataInputStream in = new DataInputStream(conn.getInputStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    /**
     * 网络文件转换为本地文件
     * @param urlStr
     * @param absolutePath
     * @param randomName
     * @throws IOException
     * @throws Exception
     */
    public static void toBDFile(String urlStr,String absolutePath,String randomName) throws IOException,Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        DataInputStream in = new DataInputStream(conn.getInputStream());
        byte[] data = toByteArray(in);
        in.close();
        FileUtil.save(data, absolutePath, randomName);
    }

    /**
     * 直接获取网络文件的md5值
     *
     * @param urlStr
     * @return
     */
    public static String getMd5ByUrl(String urlStr) {
        String md5 = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            DataInputStream in = new DataInputStream(conn.getInputStream());
            md5 = DigestUtils.md5Hex(in);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return md5;
    }

    /**
     * 获取网络文件的输入流
     *
     * @param urlStr
     * @return
     */
    public static InputStream getInputStreamByUrl(String urlStr) {
        DataInputStream in = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            in = new DataInputStream(conn.getInputStream());
        } catch (IOException e) {
        }
        return in;
    }
}
