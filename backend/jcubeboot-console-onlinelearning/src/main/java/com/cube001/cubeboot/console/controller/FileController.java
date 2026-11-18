package com.cube001.cubeboot.console.controller;

import com.cube001.cubeboot.core.config.properties.CubeBootProperties;
import com.cube001.cubeboot.core.runtime.web.AppResponse;
import com.cube001.cubeboot.core.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

@Controller
@RequestMapping("/${cubeboot.app-file-path}/file")
public class FileController {

    @Autowired
    private CubeBootProperties properties;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {

        String realtivePath = upload(file);
        return new AppResponse(true, realtivePath);
    }

    private String upload(MultipartFile file) throws Exception {
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();


        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        String profixTime = sdf1.format(new Date());
        String absolutePath = properties.getFileUploadPath() + File.separator + profixTime;
        String fileExtension = FileUtil.getExtensionNameByName(fileName);
        String randomName = UUID.randomUUID().toString().replace("-", "").substring(0, 8) + fileExtension;

        FileUtil.save(file.getBytes(), absolutePath, randomName);
        String realtivePath = "/upload/" + profixTime + "/" + randomName;
        return realtivePath;
    }

    @RequestMapping(value = "/upload2", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse upload2(HttpServletRequest request) throws Exception {
        MultipartHttpServletRequest request1 = (MultipartHttpServletRequest) request;
        Iterator<String> itr = request1.getFileNames();
        MultipartFile proFile = null;
        while (itr.hasNext()) {
            String str = itr.next();
            proFile = request1.getFile(str);
            break;
        }
        if (proFile != null) {
            String realtivePath = upload(proFile);
            return new AppResponse(true, realtivePath);
        } else {
            throw new Exception();
        }
    }

    @RequestMapping(value = "/down", method = RequestMethod.POST)
    public void down(@RequestParam String path, HttpServletResponse response) throws Exception {
        String fullPath = properties.getFileUploadPath() + File.separator + path;
        Path source = Paths.get(fullPath);
        InputStream inputStream = new FileInputStream(fullPath);
        response.reset();
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType(Files.probeContentType(source));
        OutputStream writer = response.getOutputStream();
        byte[] buffers = new byte[inputStream.available()];
        inputStream.read(buffers);
        writer.write(buffers);
        writer.close();
    }

    @RequestMapping(value = "/uploadBase64", method = RequestMethod.POST)
    public String uploadBase64(@RequestParam("path") String imgStr) throws Exception{
        //对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) {//图像数据为空
            throw new Exception();
        }
        BASE64Decoder decoder = new BASE64Decoder();
        //Base64解码
        byte[] b = decoder.decodeBuffer(imgStr);
        for(int i=0;i<b.length;++i)
        {
            if(b[i]<0)
            {//调整异常数据
                b[i]+=256;
            }
        }
        //生成jpeg图片
        String imgFilePath = "D:\\tupian\\new.jpg";//新生成的图片
        OutputStream out = new FileOutputStream(imgFilePath);
        out.write(b);
        out.flush();
        out.close();
        return imgFilePath;
    }
}
