package com.cube001.cubeboot.file.controller;


import com.cube001.cubeboot.core.authorization.entity.User;
import com.cube001.cubeboot.core.authorization.service.UserService;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.config.properties.CubeBootProperties;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.file.entity.FileResource;
import com.cube001.cubeboot.service.file.FileResourceService;
import com.cube001.cubeboot.service.file.dto.FileResourceConverter;
import com.cube001.cubeboot.service.file.dto.FileResourceDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
* Copyright (C), 2025 - 2026, zxy.
* FileName: FileResourceController.java
* 类的详细说明 资源列表控制类
*
* @author anonymous
* @date 2025/3/14 9:50
* @version 1.0
*/
@RestController
@RequestMapping("${cubeboot.app-manager-path}/fileResource")
@Api(value = "fileResource-Controller",description = "资源列表")
@RequiresPermissions("fileResource")
public class FileResourceController implements IApplicationService {

    @Autowired
    private CubeBootProperties properties;

    @Autowired
    private FileResourceService resourceService;
    @Autowired
    private FileResourceConverter converter;

    @Autowired
    private UserService userService;

    /**
    * @description
    * @author anonymous 资源列表添加
    * @date 2025/3/20 10:36
    * @param   resourceDto
    * @return   Boolean
    */
    @ApiOperation(value = "添加资源")
    @PostMapping(value = "add")
    @RequiresPermissions("fileResource:add")
    public Boolean add(@RequestBody FileResourceDto resourceDto){
        return resourceService.insert(converter.dtoToEntity(resourceDto));
    }

    @ApiOperation(value = "查询单条资源信息")
    @GetMapping(value = "get")
    @RequiresPermissions("fileResource:get")
    public FileResourceDto get(@RequestParam("id") Long id){
        FileResource fileResource = resourceService.selectById(id);
        return converter.entityToDto(fileResource);
    }


    /**
    * @description  文件上传
    * @author anonymous
    * @date 2025/3/20 10:37
    * @param   file
    * @return   FileResourceDto
    */
    @ApiOperation(value = "上传文件")
    @PostMapping(value = "upload")
    @ResponseBody
    @RequiresPermissions("fileResource:import")
    public FileResourceDto uploadFile(@RequestBody MultipartFile file, HttpServletRequest request) {
//        this.file = file;
//        this.request = request;
        Long userId = ShiroKit.getUserId();
        Double size = 0.0;
        FileResource fileResource = new FileResource();
        // 获得原始文件名
        String fileName = file.getOriginalFilename();
        // 获取文件的类型 并转换成小写
        String fileTyle =fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
        fileTyle = fileTyle.toLowerCase();
        // 新文件名
        String newFileName = fileName.substring(0,fileName.lastIndexOf("."));
        // 获得项目的路径
//      ServletContext sc = request.getSession().getServletContext();
        // 上传位置
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        String profixTime = sdf1.format(new Date());
        String path = properties.getFileUploadPath() + File.separator + userId + File.separator + profixTime + File.separator;
        String realtivePath = "/upload/"+ userId + "/" + profixTime + "/" + fileName;
        // 设定文件保存的目录
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
        if (!file.isEmpty()) {
            try {
                FileOutputStream out = new FileOutputStream(path + fileName);
                FileInputStream in = (FileInputStream)file.getInputStream();
                // 文件的大小，转换为 MB ；保留两位小数
                long b = file.getSize();
                if(b < 1024){
                    fileResource.setSize(String.valueOf(b)+"字节");
                }else if(b/1024 < 1024){
                    size = (double)b/1024;
                    size = (double) Math.round(size * 100) / 100;
                    fileResource.setSize(String.valueOf(size)+"KB");
                }else{
                    size = (double)file.getSize()/1024/1024;
                    size = (double) Math.round(size * 100) / 100;
                    fileResource.setSize(String.valueOf(size)+"MB");
                }
                FileChannel fcIn = in.getChannel();
                FileChannel fcOut = out.getChannel();
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                while (true) {
                    buffer.clear();
                    int r = fcIn.read(buffer);
                    if (r == -1) {
                        break;
                    }
                    buffer.flip();
                    fcOut.write(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
//                String data = "上传失败";
                return converter.entityToDto(fileResource);
            }
        }
        fileResource.setUserId(userId);
        fileResource.setName(newFileName);
        fileResource.setPath(realtivePath);
        fileResource.setType(fileTyle);
//        System.out.println("上传文件到:" + path + newFileName);
        return converter.entityToDto(fileResource);
    }

    /**
    * @description  文件的下载
    * @author anonymous
    * @date 2025/3/20 10:37
    * @param   id
    * @return   String
    */
    @ApiOperation(value = "下载文件")
    @PostMapping(value = "downLoad")
    @RequiresPermissions("fileResource:export")
    public void downlodaFile(@RequestParam Long id, HttpServletResponse response) {
        //Integer id = reTestCase.getId();
        // reTestCase = reTestCaseMapper.getReTestCaseById(id);
        // 得到要下载的文件名
        FileResource fileResource = new FileResource();

        fileResource = resourceService.selectById(id);
        String fileName =properties.getFileUploadPath().split("\\\\upload")[0] + fileResource.getPath() ;
        try {
            // 得到要下载的文件
            // File file = new File("/Users/dalaoyang/Documents/dalaoyang.jpeg");
            File file = new File(fileName);

            // 如果文件不存在
            if (!file.exists()) {
//                String data = "下载的文件不存在";
//                return data;
                throw new FriendException("下载的文件不存在");
            }
            // 处理文件名
            String realname = file.getName();//;
            // 设置响应头，控制浏览器下载该文件
            // response.setCharacterEncoding("UTF-8");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("GBK"),"iso8859-1"));
            // 读取要下载的文件，保存到文件输入流
            FileInputStream in = new FileInputStream(fileName);
            // 创建输出流
            OutputStream out = response.getOutputStream();
            // 创建缓冲区
            byte buffer[] = new byte[4096];
            int len = 0;
            // 循环将输入流中的内容读取到缓冲区当中
            while ((len = in.read(buffer)) > 0) {
                // 输出缓冲区的内容到浏览器，实现文件下载
                out.write(buffer, 0, len);
            }
            // 关闭文件输入流
            in.close();
            // 关闭输出流
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
//            String data = "下载文件报错";
//            return data;
        }
//        String data = "下载成功";
//        return data;
    }


}

