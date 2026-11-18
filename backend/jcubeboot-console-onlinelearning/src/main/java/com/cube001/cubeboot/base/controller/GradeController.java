package com.cube001.cubeboot.base.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.base.controller.util.ExcelUtil;
import com.cube001.cubeboot.base.controller.util.ExportExcel;
import com.cube001.cubeboot.base.entity.Grade;
import com.cube001.cubeboot.base.entity.Profession;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.core.runtime.web.AppResponse;
import com.cube001.cubeboot.core.support.DateTimeKit;
import com.cube001.cubeboot.core.util.ValidateUtil;
import com.cube001.cubeboot.core.validateGroup.EditChecks;
import com.cube001.cubeboot.service.base.GradeService;
import com.cube001.cubeboot.service.base.ProfessionsService;
import com.cube001.cubeboot.service.base.StudentService;
import com.cube001.cubeboot.service.base.dto.BaseConverter;
import com.cube001.cubeboot.service.base.dto.GradeDto;
import com.cube001.cubeboot.service.base.dto.ImportGradeDto;
import com.cube001.cubeboot.service.base.dto.ListGradeDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.groups.Default;
import javax.websocket.server.PathParam;
import java.io.*;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
* Copyright (C), 2025 - 2026, zxy.
* FileName: GradeController.java
* 类的详细说明
*
* @author anonymous
* @date 2025/3/8 9:21
* @version 1.0
*/

@Api(value = "GradeController",description = "班级Controller")
@RestController
@RequestMapping(value = "${cubeboot.app-manager-path}/grade")
@RequiresPermissions("grade")
public class GradeController implements IApplicationService {

    @Autowired
    private GradeService gradeService;
    @Autowired
    private BaseConverter baseConverter;
    @Autowired
    private StudentService studentService;

    @Autowired
    private ProfessionsService professionsService;

    private final static String templatePath = "/template/gradeImportTemplate.xlsx";
    

    /**
    * Copyright (C), 2025 - 2026, zxy.
    * FileName: GradeController.java
    * 类的详细说明
    *
    * @author anonymous
    * @date 2025/3/8 9:22
    * @version 1.0
    */
    @ApiOperation(value = "添加班级",notes = "添加班级")
    @PostMapping("/create")
    @RequiresPermissions("grade:add")
    public Boolean create(@RequestBody @Validated GradeDto gradeDto) {
        Boolean res = false;
        if(null != gradeDto) {
            Grade grade = baseConverter.dtoToGrade(gradeDto);
            res = gradeService.insert(grade);
        }
        return res;
    }


    /**  
     * @description 
     * @author anonymous 
     * @date 2025/11/27 17:13 
     * @param gradeDto   班级Dto
     * @return           修改状态
    */ 
    @ApiOperation(value = "修改班级",notes = "修改班级")
    @PostMapping("/update")
    @RequiresPermissions("grade:edit")
    public Boolean update(@RequestBody @Validated({Default.class, EditChecks.class}) GradeDto gradeDto) {
        Boolean res = false;
        if(null != gradeDto) {
            Grade grade = baseConverter.dtoToGrade(gradeDto);
            res = gradeService.updateById(grade);
        }
        return res;
    }

    /**  
     * @description 
     * @author anonymous 
     * @date 2025/11/27 17:13 
     * @param id     班级id
     * @return       删除结果
    */ 
    @ApiOperation(value = "根据id删除班级",notes = "根据id删除班级")
    @GetMapping("/delete/{id}")
    @RequiresPermissions("grade:del")
    public Boolean delete(@PathVariable Long id) {
        Boolean res = false;
        if(null != id) {
            if(studentService.selectList(new EntityWrapper<Student>().eq("grade_id",id)).size()>0) {
                return false;
            }
            res = gradeService.deleteById(id);
        }
        return res;
    }

    /**  
     * @description 
     * @author anonymous 
     * @date 2025/11/27 17:14 
     * @param id     班级id
     * @return       班级Dto
    */ 
    @ApiOperation(value = "根据id查看班级",notes = "根据id查看班级")
    @GetMapping("/{id}")
    @RequiresPermissions("grade:get")
    public GradeDto get(@PathVariable Long id) {
        if(null != id) {
            Grade grade = gradeService.getWithProfessions(id);
            return baseConverter.gradeToDto(grade);
        }
        return null;
    }

    @ApiOperation(value = "通过年级查询班级")
    @GetMapping(value = "getByYear")
    @RequiresPermissions("grade:get")
    public List<GradeDto> getList(@RequestParam("year") String year){
        List<Grade> gradeList = gradeService.selectListByYear(year);
        return baseConverter.gradeList(gradeList);
    }


    /**  
     * @description 
     * @author anonymous 
     * @date 2025/11/27 17:14 
     * @param ids     班级id集合
     * @return java.lang.Boolean  
    */ 
    @PostMapping("/deleteBatch")
    @ApiOperation(value = "批量删除班级",notes = "批量删除班级")
    @RequiresPermissions("grade:del")
    public Boolean deleteInBatches(@RequestBody List<Long> ids) {
        for(Long id: ids) {
            int count = studentService.selectCount(new EntityWrapper<Student>().eq("grade_id",id));
            if(count >0) {
                return false;
            }
        }
        return gradeService.deleteBatchIds(ids);
    }

    /**  
     * @description 
     * @author anonymous 
     * @date 2025/11/27 17:14 
     * @param commonSearchDto 分页查询Dto
     * @return                班级分页详情
    */ 
    @ApiOperation(value = "模糊分页查询班级",notes = "模糊分页查询班级")
    @PostMapping("/page")
    @RequiresPermissions("grade:page")
    public Page<GradeDto> page(@RequestBody CommonSearchDto<Grade> commonSearchDto) {
        Page<Grade>  page = gradeService.pageWithProfessions(commonSearchDto.getPlusPage(),commonSearchDto.formToEntityWrapperWithSearch(new String[]{"name","profession_id","year","counsellor"}, "a."));
        return baseConverter.gradeDtoPage(page);
    }


    @ApiOperation(value = "解析导入Excel，返回班级List",notes = "解析导入Excel，返回班级List")
    @PostMapping("/upload")
    @RequiresPermissions("grade:import")
    public List<ImportGradeDto> upload(MultipartFile file) {
        int fieldSize = 10;
        try {
            XSSFWorkbook sheets = new XSSFWorkbook(file.getInputStream());
            List list = ExcelUtil.readSheet(sheets,fieldSize);
            List<ImportGradeDto> gradeList = gradeService.checkSheet(list);
            return gradeList;
        } catch (IOException e) {
            e.printStackTrace();

        }
        return null;
    }

    /**
     * @description
     * @author anonymous
     * @date 2025/11/27 17:14
     * @param listGradeDto 班级Dto集合
     * @return             批量上传结果
     */
    @ApiOperation(value = "批量上传",notes = "批量上传")
    @PostMapping("/createBatches")
    @RequiresPermissions("grade:import")
    public Boolean insertBatches(@RequestBody  @Validated ListGradeDto listGradeDto) {
        Boolean res = gradeService.addList(listGradeDto.getData());
        return res;
    }

    /**
     * @description  根据选中的id数组导出数据
     * @author anonymous
     * @date 2025/11/28 8:53
     * @return
     */
    @ApiOperation(value = "导出",notes = "导出")
    @PostMapping(value = "/download/export")
    @ResponseBody
    @RequiresPermissions("grade:export")
    public void export(@RequestBody List<Long> ids, HttpServletResponse response) throws Exception {
        //获取数据
        List<Grade> list = gradeService.selectList(new EntityWrapper<Grade>().in("id",ids));
        //excel标题
        String[] title = {"班级名称","专业","年级","学制","导员","在校状态","入学时间", "毕业时间","班级人数"};
        //excel文件名
        //String fileName = "专业信息表"+System.currentTimeMillis()+".xls";
        String fileName = "班级信息表"+".xlsx";
        //sheet名
        String sheetName = "班级信息表";
        int line = 0;
        line = list.size();
        String [][] content = new String[line][];
        for (int i = 0; i < list.size(); i++) {
            content[i] = new String[title.length];
            Profession profession = new Profession();
            Grade obj = list.get(i);
            content[i][0] = obj.getName();
            profession = professionsService.selectById(obj.getProfessionId());
            content[i][1] = profession.getName();
            content[i][2] = obj.getYear()+"级";
            content[i][3] = obj.getSchoolLength()+"年制";
            content[i][4] = obj.getCounsellor();
            if (obj.getInSchool()){
                content[i][5] = "在校";
            }else{
                content[i][5] = "离校";
            }
            content[i][6] = DateTimeKit.format(obj.getStartDate(),"yyyy-MM-dd");
            content[i][7] = DateTimeKit.format(obj.getGraduateDate(),"yyyy-MM-dd");
            if(obj.getClassSize() != null){
                content[i][8] = Integer.toString(obj.getClassSize());
            }
            else {
                content[i][8] = "";
            }
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
     * @author anonymous
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

    /**  
     * @description 
     * @author anonymous 
     * @date 2025/11/27 17:15
     * @param gradeDto   班级Dto
     * @return           数据校验结果
    */ 
    @ApiOperation(value = "验证数据重复")
    @RequestMapping(value = "validate", method = RequestMethod.POST)
    @RequiresPermissions("grade:validate")
    public Boolean validRepeat(@RequestBody GradeDto gradeDto) {
        EntityWrapper<Grade> wrapper = new EntityWrapper<>();
        if(null != gradeDto.getId()) {
            Grade grade = gradeService.selectById(gradeDto.getId());
            if(grade.getYear().equals(gradeDto.getYear()) && grade.getName().equals(gradeDto.getName())) {
                return true;
            }
        }
        wrapper.eq("name",gradeDto.getName())
                .eq("year",gradeDto.getYear());
        int count = gradeService.selectCount(wrapper);
        return count==0?true:false;
    }

    /**
    * @description 获取列表
    * @author anonymous
    * @date 2025/12/5 14:24
    * @param dto
    * @return java.util.List<com.cube001.cubeboot.service.base.dto.GradeDto>
    */
    @ApiOperation(value = "取列表")
    @RequestMapping(value = "list",method = RequestMethod.GET)
    @RequiresPermissions("grade:list")
    public List<GradeDto> list(){
       List<Grade> gradeList= gradeService.selectList(new EntityWrapper<>());
       return  baseConverter.gradeList(gradeList);
    }
    /**
    * @description
    * @author anonymous
    * @date 2025/1/10 10:10
    * @param ids ID集合
    * @return java.util.List<com.cube001.cubeboot.service.base.dto.GradeDto>
    */
    @ApiOperation(value = "根据ID批量获取")
    @RequestMapping(value = "getBatchByIds",method = RequestMethod.GET)
    @RequiresPermissions("grade:list")
    public List<GradeDto> getBatchByIds(@RequestParam("ids") String ids){
        String[] arr= ids.split(",");
        Wrapper<Grade > wrapper=new EntityWrapper<>();
        wrapper.in("id",arr);
        List<Grade> list= gradeService.selectList(wrapper);
        return baseConverter.gradeList(list);
    }
}

