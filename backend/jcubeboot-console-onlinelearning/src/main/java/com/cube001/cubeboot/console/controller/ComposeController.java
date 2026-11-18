package com.cube001.cubeboot.console.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.aspectj.util.FileUtil;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * 前台组合文件
 */
@RestController
@RequestMapping("/${cubeboot.app-manager-path}/compose")
public class ComposeController {

    @RequestMapping(value = "getDependency",method = RequestMethod.GET)
    public JSONArray getDependency(String module) throws Exception{
            File dependencyFile=  ResourceUtils.getFile("classpath:frontDependency/dependency.json");
            String jsonStr= FileUtil.readAsString(dependencyFile);
            JSONObject object=JSONObject.parseObject(jsonStr);
            JSONArray jsonArray= object.getJSONArray(module);
            if(jsonArray==null){
                jsonArray=new JSONArray();
            }
            return jsonArray;
    }

}
