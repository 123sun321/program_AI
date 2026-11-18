package com.cube001.cubeboot.base.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.base.controller.util.ExcelUtil;
import com.cube001.cubeboot.base.controller.util.ExportExcel;
import com.cube001.cubeboot.base.entity.Grade;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.base.entity.Profession;
import com.cube001.cubeboot.core.runtime.web.AppResponse;
import com.cube001.cubeboot.service.base.GradeService;
import com.cube001.cubeboot.service.base.ProfessionsService;
import com.cube001.cubeboot.service.base.dto.BaseConverter;
import com.cube001.cubeboot.service.base.dto.ImportProfessionDto;
import com.cube001.cubeboot.service.base.dto.ListProfessionDto;
import com.cube001.cubeboot.service.base.dto.ProfessionDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
* Copyright (C), 2025 - 2026, zxy.
* FileName: ProfessionController.java
* 专业的控制接口页面
*
* @author 马勇超
* @date 2025/11/27 15:41
* @version 1.0
*/
@RestController
@Api(value = "Profession-controller" ,description = "专业 Controller")
@RequestMapping(value = "${cubeboot.app-manager-path}/profession")
@RequiresPermissions("profession")
public class ProfessionController implements IApplicationService {

    @Autowired
    ProfessionsService professionsService;

    @Autowired
    BaseConverter baseConverter;

    @Autowired
    GradeService gradeService;


    /**
    * @description  根据id查询单条专业内容
    * @author 马勇超
    * @date 2025/11/27 15:52
    * @param id
    * @return 返回一条专业信息
    */
    @ApiOperation(value="根据id查询专业内容", notes="根据id查询专业内容")
    @GetMapping(value = "/get")
    @ResponseBody
    @RequiresPermissions("profession:get")
    public ProfessionDto get(@RequestParam("id") Long id){
        Profession profession = professionsService.selectById(id);
        return  baseConverter.professionToDto(profession);
    }


    /**
     * @description  查询所有专业内容的id与name
     * @author 马勇超
     * @date 2025/11/27 15:52
     * @return 返回一个只包含所有专业的id与name的数组
     */
    @ApiOperation(value="获取所有专业id和name", notes="获取所有专业id和name")
    @GetMapping(value = "/list")
    @ResponseBody
    @RequiresPermissions("profession:list")
    public List<ProfessionDto> getIdAndName(){
        return  baseConverter.dtoToListProfession(professionsService.getIdAndName());
    }

    /**
     * @description  查询所有专业内容信息
     * @author 马勇超
     * @date 2025/11/27 15:52
     * @return 返回一个包含所有专业信息的数组
     */
    @ApiOperation(value="获取所有专业列表包含班级", notes="获取所有专业列表包含班级")
    @GetMapping(value = "/listWithGrade")
    @ResponseBody
    @RequiresPermissions("profession:list")
     public List<ProfessionDto> getListWithGrade(){
        return  baseConverter.dtoToListProfession(professionsService.listWithProfessionsAndGrades());
     }

    /**
     * @description  创建单个专业信息
     * @author 马勇超
     * @date 2025/11/27 15:52
     * @return 返回一个Boolean值
     */
    @ApiOperation(value="创建专业信息", notes="创建专业信息")
    @PostMapping(value = "/add")
    @ResponseBody
    @RequiresPermissions("profession:add")
    public AppResponse create(@RequestBody @Validated ProfessionDto professionDto){
        Profession profession = baseConverter.dtoToProfession(professionDto);
        professionsService.insert(profession);
        return new AppResponse(true,"");
    }

    /**
     * @description  修改某条专业内容
     * @author 马勇超
     * @date 2025/11/27 15:52
     * @return 返回一个boolean值
     */
    @ApiOperation(value = "修改专业内容", notes = "修改专业内容")
    @PostMapping(value = "/edit")
    @ResponseBody
    @RequiresPermissions("profession:edit")
    public Boolean update(@RequestBody @Validated ProfessionDto professionDto){
        Boolean result = false;
        Profession profession = baseConverter.dtoToProfession(professionDto);
        Long id = profession.getId();
        String editName = profession.getName();
        List<Profession> professionsList = professionsService.selectList(new EntityWrapper<Profession>().eq("name",editName).eq("id",id));
        if(professionsList.size() == 0 || professionsList.size() == 1 || professionsList ==null)
        {
            professionsService.updateById(profession);
            result = true;
        }
        return result;
    }

    /**
     * @description  根据id删除单条专业信息(如果该专业下面已创建班级信息则删除失败)
     * @author 马勇超
     * @date 2025/11/27 15:52
     * @param id
     * @return 返回一个Boolean值
     */
    @ApiOperation(value="删除专业信息",notes = "删除专业内容")
    @PostMapping(value = "/del/{id}")
    @ResponseBody
    @RequiresPermissions("profession:del")
    public Boolean delete(@PathVariable(value = "id") Long id){
        //判断专业下面是否存在班级
        return professionsService.deleteWithGradeById(id);
    }

    /**
     * @description  根据id数组删除多条专业信息(如果某条专业下面已创建班级信息则全部删除失败)
     * @author 马勇超
     * @date 2025/11/27 15:52
     * @param ids
     * @return  Boolean
     */
    @ApiOperation(value = "批量删除专业信息",notes = "批量ids删除专业信息")
    @PostMapping(value = "/deleteBatches")
    @ResponseBody
    @RequiresPermissions("profession:del")
    public Boolean deleteBatch(@RequestBody List<Long> ids){
        Boolean result = false;
        //判断专业下面是否存在班级
        List<Grade> gradeList = gradeService.selectList(new EntityWrapper<Grade>().in("profession_id",ids));
        if(gradeList.size() ==0||gradeList==null){
            result = professionsService.deleteBatchIds(ids);
        }
        return result;
    }
    /**
     * @description  Excel解析出数据时调用接口实现批量导入数据
     * @author 马勇超
     * @date 2025/11/27 15:52
     * @return
     */
    @ApiOperation(value = "批量上传",notes = "批量上传")
    @RequestMapping(value = "multipleAdd",method = RequestMethod.POST)
    @RequiresPermissions("profession:import")
    public Boolean insertBatches(@RequestBody @Validated ListProfessionDto professionDto) {
        List<Profession> professionList = baseConverter.professionDtoList(professionDto.getData());
        Boolean success = professionsService.insertBatch(professionList);
        return success;
    }

    /**
     * @description  搜索/分页接口：用以分页数据和根据专业name实现模糊查询
     * @author 马勇超
     * @date 2025/11/27 15:52
     * @return page //返回一个page对象
     */
    @ApiOperation(value = "搜索/分页",notes = "搜索/分页")
    @RequestMapping(value = "/page",method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("profession:page")
    public Page<ProfessionDto> page(@RequestBody CommonSearchDto<Profession> commonSearchDto){
        Map<String,String> map = new HashMap<>();
        // 根据教师姓名
        map.put("name","a.");
        Page<Profession> professionPage =  professionsService.searchPage(commonSearchDto.getPlusPage(),commonSearchDto.formToEntityWrapper(null,map));
        return  baseConverter.pageProfessionToDto(professionPage);
    }

    /**
     * @description  Excel解析：返回一组包含专业名字，院系id,success，tips 的数组数据
     * @author 马勇超
     * @date 2025/11/27 15:52
     * @param file
     * @return
     */
    @ApiOperation(value = "解析Excel文件",notes = "解析Excel文件")
    @PostMapping(value = "/import")
    @ResponseBody
    @RequiresPermissions("profession:import")
    public List<ImportProfessionDto> upLoad(MultipartFile file){
        String[] a = {"name","code"};
        List<String> fields = Arrays.asList(a);
        List<Map<String, Object>> mapList = null;
        try {
            Workbook workbook = null;
            String filename = file.getOriginalFilename();
            if (filename.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(file.getInputStream());
            }else if(filename.endsWith(".xls")) {
                workbook = new HSSFWorkbook(file.getInputStream());
            }
            mapList = ExcelUtil.readFilds(workbook, fields);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<ImportProfessionDto> professionDtoList = professionsService.importDataToDo(mapList);
        return professionDtoList;
    }

    /**
     * @description  实现前端的远端验证：尝试修改数据时，实时更新提示信息
     * @author 马勇超
     * @date 2025/11/27 15:52
     * @return  Boolean
     */
    @ApiOperation(value = "验证数据重复")
    @RequestMapping(value = "validate", method = RequestMethod.GET)
    @ResponseBody
    @RequiresPermissions("profession:validate")
    public Boolean validRepeat(@RequestParam(value = "id",required = false) Long id,@RequestParam("col") String col, @RequestParam("value") String value) {
        Boolean pass = false;
        Wrapper<Profession> wrapper = new EntityWrapper<>();
        wrapper.eq(col,value);
//        if(null != id){
//            wrapper.eq(col,value).eq("id",id);
//        }
        int count = professionsService.selectCount(wrapper);
        Profession profession = professionsService.selectOne(wrapper);
        if(count == 0 && profession == null){
            pass = true;
        }
        if(count ==1 && profession.getId().equals(id)){
            pass = true;
        }
        if(profession!=null && !profession.getId().equals(id)){
            pass = false;
        }
        return pass;
    }

    /**
    * @description  根据选中的id数组导出数据
    * @author 马勇超
    * @date 2025/11/28 8:53
    * @return
    */
    @ApiOperation(value = "导出",notes = "导出")
    @PostMapping(value = "/export")
    @ResponseBody
    @RequiresPermissions("profession:export")
    public void export(@RequestBody List<Long> ids, HttpServletResponse response) throws Exception {
        //获取数据
        List<Profession> list = professionsService.selectList(new EntityWrapper<Profession>().in("id",ids));
        //excel标题
        String[] title = {"专业名称","专业编号"};
        //标准时间格式为：年月日时分秒
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //excel文件名
        //String fileName = "专业信息表"+System.currentTimeMillis()+".xls";
        String fileName = "专业信息表"+".xlsx";
         //sheet名
          String sheetName = "专业信息表";
          int line = 0;
          line = list.size();
          String [][] content = new String[line][];
          for (int i = 0; i < list.size(); i++) {
              content[i] = new String[title.length];
              Profession obj = list.get(i);
              content[i][0] = obj.getName();
              content[i][1] = obj.getCode();
              // content[i][2] = format.format(obj.getCreationTime());
              }
//创建HSSFWorkbook
HSSFWorkbook wb = ExportExcel.getHSSFWorkbook(sheetName, title, content, null);

//响应到客户端
      try {
           this.setResponseHeader(response, fileName);
           OutputStream os = response.getOutputStream();
           wb.write(os);
           os.flush();
           os.close();
          } catch (Exception e) {
           e.printStackTrace();
        }
}

    /**
    * @description  发送响应流方法
    * @author 马勇超
    * @date 2025/11/28 8:55
    * @return
    */
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(),"ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}

