package com.cube001.cubeboot.base.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.base.controller.util.ExcelUtil;
import com.cube001.cubeboot.base.controller.util.ExportExcel;
import com.cube001.cubeboot.base.entity.Grade;
import com.cube001.cubeboot.base.entity.Profession;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.core.authorization.entity.User;
import com.cube001.cubeboot.core.authorization.service.UserService;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.domain.tenant.TenantFliter;
import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.core.runtime.web.AppResponse;
import com.cube001.cubeboot.core.support.DateTimeKit;
import com.cube001.cubeboot.core.validateGroup.EditChecks;
import com.cube001.cubeboot.service.base.GradeService;
import com.cube001.cubeboot.service.base.ProfessionsService;
import com.cube001.cubeboot.service.base.StudentService;
import com.cube001.cubeboot.service.base.dto.ImportStudentDto;
import com.cube001.cubeboot.service.base.dto.ListStudentDto;
import com.cube001.cubeboot.service.base.dto.StudentConverter;
import com.cube001.cubeboot.service.base.dto.StudentDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: StudentController
 * 类的详细说明
 *
 * @author 郭广明
 * @version 1.0
 * @date 2025/11/28 13:28
 */
@Api(value = "StudentController", description = "学生Controller")
@RestController
@RequestMapping(value = "${cubeboot.app-manager-path}/student")
@RequiresPermissions("student")
public class StudentController implements IApplicationService {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentConverter studentConverter;
    @Autowired
    private UserService userService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private ProfessionsService professionsService;


    private final static String templatePath = "/template/studentImportTemplate.xlsx";

    /**
    * @description  添加学生
    * @author 马勇超
    * @date 2025/3/21 14:52
    * @param   studentDto
    */
    @ApiOperation(value = "添加学生", notes = "添加学生")
    @PostMapping("/create")
    @Transactional(rollbackFor = Exception.class)
    @RequiresPermissions("student:add")
    public void create(@RequestBody @Validated StudentDto studentDto) {
        Grade grade = gradeService.selectById(studentDto.getGradeId());
        List<Student> studentList = studentService.selectList(new EntityWrapper<Student>().eq("grade_id",studentDto.getGradeId()));
        if(grade.getClassSize() == studentList.size() || studentList.size() > grade.getClassSize()){
            throw new FriendException("该班级人数已经达到最大！");
        }else {
            Student student = studentConverter.dtoToStudent(studentDto);
            student.setContact(studentDto.getContact());
            student.setEmail(studentDto.getEmail());
            studentService.insertWithUser(student);
        }
    }


    /**
     * @param studentDto 学生Dto
     * @return 修改结果
     * @description 修改学生
     * @author 郭广明
     * @date 2025/11/28 15:36
     */
    @RequiresPermissions("student:edit")
    @ApiOperation(value = "修改学生", notes = "修改学生")
    @PostMapping("/update")
    @ResponseBody
    @Transactional
    public AppResponse update(@RequestBody @Validated(EditChecks.class) StudentDto studentDto) {
        //更新user表用户头像
        User user = userService.selectById(ShiroKit.getUserId());
        user.setUserPic(studentDto.getImgUrl());
        userService.update(user);
        Student student = studentConverter.dtoToStudent(studentDto);
        student.setEmail(studentDto.getEmail());
        student.setContact(studentDto.getContact());
        Boolean res = studentService.updateById(student);
        Student newStudent = studentService.selectById(studentDto.getId());
        User studentUser = userService.selectById(newStudent.getUserId());
        studentUser.setUserName(newStudent.getCode());
        studentUser.setEmailAddress(studentDto.getEmail());
        if (!StringUtils.equals(studentDto.getEmail(), studentUser.getEmailAddress())) {
            studentUser.setEmailConfirmed(false);
        }
        studentUser.setPhoneNumber(studentDto.getContact());
        if (!StringUtils.equals(studentDto.getContact(), studentUser.getPhoneNumber())) {
            studentUser.setPhoneNumberConfirmed(false);
        }
        userService.update(studentUser);

        return new AppResponse(res, res ? "修改成功" : "修改失败");
    }

    /**
     * 验证数据重复
     * @param id
     * @param col
     * @param value
     * @return 数据验证结果
     * @description 数据重复校验
     * @author 郭广明
     * @date 2025/11/28 15:46
     */
    @ApiOperation(value = "验证数据重复")
    @RequestMapping(value = "validate", method = RequestMethod.POST)
    @RequiresPermissions("student:validate")
    public Boolean validRepeat(@RequestParam(value = "id", required = false) Long id, @RequestParam("col") String col, @RequestParam("value") String value) {
//        if (null != studentDto.getId()) {
////            Student student = studentService.selectById(studentDto.getId());
////            if (student.getCode().equals(studentDto.getCode()) && student.getIdnumber().equals(studentDto.getIdnumber())) {
////                return true;
////            }
////        }
////        EntityWrapper<Student> wrapper = new EntityWrapper<>();
////        wrapper.eq("code", studentDto.getCode())
////                .eq("idnumber", studentDto.getIdnumber());
////        int count = studentService.selectCount(wrapper);
////        return count == 0 ? true : false;
        Wrapper<Student> studentWrapper = new EntityWrapper<>();
        studentWrapper = studentWrapper.eq(col, value);
        int count = 0;
        Boolean pass = false;
        if (null != id) {
            studentWrapper = studentWrapper.eq("id", id);
            Student student = studentService.selectById(id);
            System.out.println(student);
            count = studentService.selectCount(studentWrapper);
            System.out.println(count);
            if (count == 1) {
                pass = true;
            }
            Student isCode = studentService.selectOne(new EntityWrapper<Student>().eq("code", value));
            if (isCode == null && count == 0) {
                pass = true;
            }
            if (isCode != null && count == 0 && student.getCode() != value) {
                pass = false;
            }
        } else {
            count = studentService.selectCount(studentWrapper);
            if (count == 0) {
                pass = true;
            }
        }
        return pass;
    }

    /**
     * @param id 学生id
     * @return 删除结果
     * @description 根据id删除学生
     * @author 郭广明
     * @date 2025/11/28 15:49
     */
    @ApiOperation(value = "根据id删除学生", notes = "根据id删除学生")
    @GetMapping("/del")
    @RequiresPermissions("student:del")
    public Boolean delete(@RequestParam("id") Long id) {
        Boolean res = false;
        //todo: 解决级联
        res = studentService.delStudent(id);
        return res;
    }

    /**
     * @param ids 学生id集合
     * @return 批量删除结果
     * @description 批量删除学生
     * @author 郭广明
     * @date 2025/11/28 15:53
     */
    @PostMapping("/deleteBatch")
    @ApiOperation(value = "批量删除学生", notes = "批量删除学生")
    public Boolean deleteInBatches(@RequestBody List<Long> ids) {
        Boolean res = false;
        res = studentService.deleteStudentBatches(ids);
        return res;
    }

    /**
     * @param id 学生id
     * @return 学生Dto
     * @description 根据id查看学生详情
     * @author 郭广明
     * @date 2025/11/28 15:54
     */
    @ApiOperation(value = "根据id查看学生详情", notes = "根据id查看学生详情")
    @GetMapping("/{id}")
    @RequiresPermissions("student:get")
    public StudentDto getById(@PathVariable Long id) {
        try(TenantFliter tenantFliter=new TenantFliter(false)) {
            return studentService.getById(id);
        }
    }

    @ApiOperation(value = "根据用户id获取专业和院系信息")
    @GetMapping(value = "getByUserId")
    public StudentDto getStudentByUserId(@RequestParam("userId")Long userId){
        Student student = studentService.getStudentByUserId(userId);
        return studentConverter.studentToDto(student);
    }

    /**
     * @param commonSearchDto 分页查询Dto
     * @return 分页结果详情
     * @description 模糊分页查询学生
     * @author 郭广明
     * @date 2025/11/28 16:49
     */
    @ApiOperation(value = "模糊分页查询学生", notes = "模糊分页查询学生")
    @PostMapping("/page")
    public Page<StudentDto> page(@RequestBody CommonSearchDto<Student> commonSearchDto) {
        Page<Student> page = commonSearchDto.getPlusPage();
        HashMap<String,String> map = new HashMap<>();
        map.put("professionId","a.");
        map.put("year","b.");
        map.put("grade_id","a.");
        map.put("name","a.");
        page = studentService.page(page, commonSearchDto.formToEntityWrapper(null,map));
        return studentConverter.pageToDto(page);
    }
    /**
     * @param file Excel文件
     * @return 校验过的学生Dto集合
     * @description 解析导入Excel
     * @author 郭广明
     * @date 2025/11/28 17:01
     */
    @ApiOperation(value = "解析导入Excel，返回学生List", notes = "解析导入Excel，返回学生List")
    @PostMapping("/upload")
    public List<ImportStudentDto> upload(MultipartFile file) {
        int fieldSize = 19;
        try {
            XSSFWorkbook sheets = new XSSFWorkbook(file.getInputStream());
            List list = ExcelUtil.readSheet(sheets, fieldSize);
            List<ImportStudentDto> studentDtoList = studentService.checkSheet(list);
            return studentDtoList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param listStudentDto 学生dto集合
     * @return 批量上传结果
     * @description 批量上传
     * @author 郭广明
     * @date 2025/11/28 17:07
     */
    @ApiOperation(value = "批量上传", notes = "批量上传")
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/createBatches")
    public void insertBatches(@RequestBody @Validated ListStudentDto listStudentDto) {
        studentService.addList(listStudentDto.getData());
    }


    /**
     * @description  根据选中的id数组导出数据
     * @author 马勇超
     * @date 2025/11/28 8:53
     * @return
     */
    @ApiOperation(value = "导出",notes = "导出")
    @PostMapping(value = "/download/export")
    @ResponseBody
    public void export(@RequestBody List<Long> ids, HttpServletResponse response) throws Exception {
        //获取数据
        List<Student> list = studentService.selectList(new EntityWrapper<Student>().in("id",ids));
        //excel标题
        String[] title = {"学号","姓名","班级","年级","专业","性别","联系方式","出生日期","电子邮箱",
                "家庭住址","民族","籍贯","政治面貌","学历","证件号码","学制","紧急联系人","紧急联系人电话","与紧急联系人关系"};
        //标准时间格式为：年月日时分秒
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //excel文件名
        //String fileName = "专业信息表"+System.currentTimeMillis()+".xls";
        String fileName = "学生信息表"+".xlsx";
        //sheet名
        String sheetName = "学生信息表";
        int line = 0;
        line = list.size();
        String [][] content = new String[line][];
        for (int i = 0; i < list.size(); i++) {
            content[i] = new String[title.length];
            Grade grade = new Grade();
            Profession profession = new Profession();
            Student obj = list.get(i);
            content[i][0] = obj.getCode();
            content[i][1] = obj.getName();
            grade = gradeService.selectById(obj.getGradeId());
            content[i][2] = grade.getName();
            content[i][3] = grade.getYear();
            profession = professionsService.selectById(obj.getProfessionId());
            content[i][4] = profession.getName();
            content[i][5] = obj.getSex();
            content[i][6] = obj.getContact();
            if(obj.getBirthday() != null) {
                content[i][7] = DateTimeKit.format(obj.getBirthday(),"yyyy-MM-dd");
            }else{
                content[i][7] = "";
            }
            content[i][8] = obj.getEmail();
            content[i][9] = obj.getAddress();
            content[i][10] = obj.getNation();
            content[i][11] = obj.getNationality();
            content[i][12] = obj.getPolitics();
            content[i][13] = obj.getEducation();
            content[i][14] = obj.getIdnumber();
            content[i][15] = obj.getSchoolingLength();
            content[i][16] = obj.getLinkman();
            content[i][17] = obj.getLinkmanContact();
            content[i][18] = obj.getLinkmanRelation();
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

    /**
     * @return 模板地址
     * @description 学生模板下载
     * @author 郭广明
     * @date 2025/11/28 17:36
     */
    @ApiOperation(value = "学生模板下载", notes = "学生模板下载")
    @GetMapping("/download/template")
    public String download() {
        String path = templatePath;
        return path;
    }

    /**
    * @description  重置密码
    * @author 马勇超
    * @date 2025/4/12 9:05
    * @param   id
    * @return  boolean
    */
    @ApiOperation(value = "重置密码", notes = "重置密码")
    @GetMapping("/resetPass")
    public void resetPass(@RequestParam Long id) {
        Student student = studentService.selectById(id);
        if(null==student){
            throw new FriendException("该学生不存在！");
        }
        User user = userService.selectById(student.getUserId());
        if(null==user){
            throw new FriendException("该用户不存在！");
        }
        String password = "000000";
        try {
            userService.changePassword(user, password);
        }
        catch (Exception e){
            throw new FriendException("重置密码失败！");
        }
    }

    /**
    * @description  解绑手机
    * @author 马勇超
    * @date 2025/4/12 16:09
    * @param
    * @return
    */
    @ApiOperation(value = "解绑手机")
    @RequestMapping(value = "/bindPhone/unbind", method = RequestMethod.GET)
    @ResponseBody
    public void unBindPhone(@RequestParam("id")Long id) {
        Student student = studentService.selectById(id);
        User user = userService.selectById(student.getUserId());
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setUserName(user.getUserName());
        updateUser.setPhoneNumberConfirmed(false);
        updateUser.setPhoneNumber("");
        userService.updateById(updateUser);
    }


    /**
     * @param commonSearchDto
     * @return java.util.List<com.cube001.cubeboot.service.base.dto.StudentDto>
     * @description 取列表
     * @author 陈乐乐
     * @date 2025/12/6 17:11
     */
//    @ApiOperation(value = "取列表")
//    @RequestMapping(value = "list", method = RequestMethod.POST)
//    @RequiresPermissions("student:list")
//    public List<StudentDto> list(@RequestBody CommonSearchDto<Student> commonSearchDto) {
//        List<Student> students = studentService.list(commonSearchDto.formToEntityWrapperWithSearch(new String[]{"name", "code"}, "a."));
//        return studentConverter.listToDto(students);
//    }

//    @ApiOperation(value = "生成学生报告")
//    @PostMapping("createReport")
//    public Boolean createReport(@RequestBody List<Long> studentIds) {
////        Wrapper<Student> wrapper=new EntityWrapper<>();
////        wrapper.in("a.id",studentIds);
////        List<Student> students = studentService.listWithDepartment(wrapper);
////        for (Student student : students) {
////            studentReportService.buildStudentReport(student);
////        }
//        return true;
//    }
    /**
    * @description
    * @author 郜强
    * @date 2025/1/22 15:23
    * @return java.lang.Long
    */
    @ApiOperation(value = "获取当前登录用户的学生id")
    @GetMapping("getStudentId")
    public Long getStudentId(){
        Long userid=ShiroKit.getUserId();
        Student student=studentService.selectOne(new EntityWrapper<Student>().eq("user_id",userid));
        return student.getId();
    }
}

