package com.cube001.cubeboot.base.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.base.controller.util.ExcelUtil;
import com.cube001.cubeboot.base.controller.util.ExportExcel;
import com.cube001.cubeboot.base.entity.Teachers;
import com.cube001.cubeboot.core.authorization.entity.User;
import com.cube001.cubeboot.core.authorization.service.UserService;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.core.runtime.web.AppResponse;
import com.cube001.cubeboot.core.validateGroup.EditChecks;
import com.cube001.cubeboot.service.base.RoleExtendService;
import com.cube001.cubeboot.service.base.TeachersService;
import com.cube001.cubeboot.service.base.dto.*;
import com.cube001.cubeboot.service.base.dto.base.DashboardCount;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * Copyright (C), 2025 - 2026, zxy.
 * FileName: TeachersController.java
 * 教师的控制器接口页面
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/11/28 8:38
 */
@Api(value = "教师controller", description = "教师 controller")
@RestController
@RequestMapping("${cubeboot.app-manager-path}/teachers")
@RequiresPermissions("teacher")
public class TeachersController implements IApplicationService {
    @Autowired
    private TeachersService teachersService;

    @Autowired
    private TeachersConverter teachersConverter;

    @Autowired
    private UserService userService;
    @Autowired
    private RoleExtendService roleExtendService;

    /**
     * @param id
     * @return teachersDto 单条教师信息
     * @description 根据id查询单条教师信息
     * @author anonymous
     * @date 2025/11/28 11:10
     */
    @ApiOperation(value = "根据id获取教师信息", notes = "根据id获取教师信息")
    @GetMapping(value = "/get")
    @RequiresPermissions("teacher:get")
    public TeachersDto getById(@RequestParam("id") Long id) {
        Teachers teachers = teachersService.selectById(id);
        teachers.setUser(userService.selectById(teachers.getUserId()));
        return teachersConverter.entityToDto(teachers);
    }

    @ApiOperation(value = "获取当前教师信息")
    @GetMapping(value = "/getNow")
    @RequiresPermissions("teacher:get")
    public TeachersDto now() {
        Long userId = ShiroKit.getUserId();
        //获取当前登录用户ID
        return teachersService.selectWithDepartment(userId);
    }

    /**
     * @return page
     * @description 搜索/分页接口：用以分页数据和根据教师name和工号实现模糊查询
     * @author anonymous
     * @date 2025/11/28 8:50
     */
    @ApiOperation(value = "分页获取教师信息")
    @RequestMapping(value = "page", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("teacher:page")
    public Page<TeachersDto> page(@RequestBody CommonSearchDto<Teachers> searchDto) {
//        Map<String, String> map = new HashMap<>();
//        // 根据学校id
//        map.put("id", "s.");
//        // 根据教师姓名
//        map.put("name", "a.");
//        // 根据教师工号
//        map.put("code", "a.");
//        // 根据身份证号
//        map.put("car_no","a.");
        Page<Teachers> teachersPage = teachersService.searchPage(searchDto.getPlusPage(), searchDto.formToEntityWrapperWithSearch(new String[]{"name"}, "a."));
        return teachersConverter.pageEntityToDto(teachersPage);
    }

    @ApiOperation(value = "取列表")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    @RequiresPermissions("teacher:list")
    public List<TeachersDto> list() {
        List<Teachers> teachersDtos = teachersService.getAll();
        return teachersConverter.listEntityToDto(teachersDtos);
    }

    /**
     * @return Boolean
     * @description 创建单个的教师信息；拥有验证
     * @author anonymous
     * @date 2025/11/28 9:02
     */
    @ApiOperation(value = "创建教师信息", notes = "创建教师信息")
    @PostMapping(value = "/add")
    @RequiresPermissions("teacher:add")
    public AppResponse add(@RequestBody @Validated TeachersDto teachersDto) {
        Teachers teachers = teachersConverter.dtoToEntity(teachersDto);
        teachers.setContact(teachersDto.getContact());
        teachers.setEmail(teachersDto.getEmail());
        Boolean success = teachersService.insertWithUser(teachers);
        return new AppResponse(success, success ? "新建成功" : "新建失败");
    }

    /**
     * @return Boolean
     * @description 更新修改教师信息内容
     * @author anonymous
     * @date 2025/11/28 9:05
     */
    @ApiOperation(value = "修改教师信息", notes = "注意参数")
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions("teacher:edit")
    public AppResponse editTeacherStatus(@RequestBody @Validated(EditChecks.class) TeachersDto teachersDto) {
        //更新用户表头像
        User user = userService.selectById(ShiroKit.getUserId());
        user.setUserPic(teachersDto.getImgUrl());
        userService.update(user);

        Teachers teachers = teachersConverter.dtoToEntity(teachersDto);
        teachers.setContact(teachersDto.getContact());
        teachers.setEmail(teachersDto.getEmail());
        boolean success = teachersService.updateById(teachers);
        Teachers newTeacher = teachersService.selectById(teachersDto.getId());
        //修改教师对应的用户信息
        User teacherUser = userService.selectById(newTeacher.getUserId());
        teacherUser.setEmailAddress(teachersDto.getEmail());
        if (!StringUtils.equals(teachersDto.getEmail(), teacherUser.getEmailAddress())) {
            teacherUser.setEmailConfirmed(false);
        }
        teacherUser.setPhoneNumber(teachersDto.getContact());
        if (!StringUtils.equals(teachersDto.getContact(), teacherUser.getPhoneNumber())) {
            teacherUser.setPhoneNumberConfirmed(false);
        }
        userService.update(teacherUser);
        return new AppResponse(success, success ? "教师信息修改成功" : "教师信息修改失败");
    }

    /**
     * @param id
     * @return Boolean
     * @description 根据id删除单条教师信息
     * @author anonymous
     * @date 2025/11/28 9:14
     */
    @ApiOperation(value = "根据id删除教师信息", notes = "根据id删除教师信息")
    @PostMapping(value = "/del/{id}")
    @ResponseBody
    @RequiresPermissions("teacher:del")
    public Boolean delete(@PathVariable("id") Long id) {
        Teachers teachers = teachersService.selectById(id);
        Boolean success = teachersService.deleteById(id);
        // 删除教师角色
        roleExtendService.deleteTeacherRole(teachers.getUserId());
        //删除用户
        userService.deleteById(teachers.getUserId());
        return success;
    }

    /**
     * @param ids
     * @return Boolean
     * @description 批量删除多条教师信息
     * @author anonymous
     * @date 2025/11/28 9:15
     */
    @ApiOperation(value = "批量删除教师信息", notes = "批量删除教师信息")
    @PostMapping(value = "/deleteBatches")
    @ResponseBody
    @RequiresPermissions("teacher:del")
    public  Boolean deleteBatch(@RequestBody List<Long> ids){
        for (Long l:ids) {
            Teachers teachers = teachersService.selectById(l);
            System.out.println(teachers.getUserId());
            userService.deleteById(teachers.getUserId());
            roleExtendService.deleteTeacherRole(teachers.getUserId());
        }
        Boolean success =  teachersService.deleteBatchIds(ids);
        return success;
    }

    /**
     * @param id
     * @description 初始化教师的密码
     * @author anonymous
     * @date 2025/12/20 8:42
     */
    @ApiOperation(value = "重置密码", notes = "重置密码")
    @ResponseBody
    @RequiresPermissions("teacher:edit")
    @GetMapping("/resetPass")
    public void resetPass(@RequestParam Long id) {
        Teachers teachers = teachersService.selectById(id);
        if(null==teachers){
            throw new FriendException("该教师不存在！");
        }
        User user = userService.selectById(teachers.getUserId());
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
     * @author anonymous
     * @date 2025/4/12 16:09
     * @param
     * @return
     */
    @ApiOperation(value = "解绑手机")
    @RequestMapping(value = "/bindPhone/unbind", method = RequestMethod.GET)
    @ResponseBody
    public void unBindPhone(@RequestParam("id")Long id) {
        Teachers teachers = teachersService.selectById(id);
        User user = userService.selectById(teachers.getUserId());
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setUserName(user.getUserName());
        updateUser.setPhoneNumberConfirmed(false);
        updateUser.setPhoneNumber("");
        userService.updateById(updateUser);
    }

    /**
     * @param file
     * @return
     * @description Excel解析：返回一组包含专业名字，院系id，
     * success，tips 的数组数据
     * @author anonymous
     * @date 2025/11/27 15:52
     */
    @ApiOperation(value = "解析Excel文件", notes = "解析Excel文件")
    @PostMapping(value = "/import")
    @ResponseBody
    @RequiresPermissions("teacher:import")
    public List<ImportTeacherDto> upLoad(MultipartFile file) {
        String[] a = {"name", "code", "sex", "carNo", "contact", "email"};
        List<String> fields = Arrays.asList(a);
        List<Map<String, Object>> mapList = null;
        try {
            Workbook workbook = null;
            String filename = file.getOriginalFilename();
            if (filename.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(file.getInputStream());
            } else if (filename.endsWith(".xls")) {
                workbook = new HSSFWorkbook(file.getInputStream());
            }
            mapList = ExcelUtil.readFilds(workbook, fields);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<ImportTeacherDto> teacherDtoList = teachersService.importDataToDo(mapList);
        return teacherDtoList;
    }

    /**
     * @return Boolean
     * @description Excel解析出数据时调用接口实现批量导入数据
     * @author anonymous
     * @date 2025/11/28 11:03
     */
    @ApiOperation(value = "批量导入", notes = "批量导入")
    @PostMapping(value = "/multipleAdd")
    @ResponseBody
    @RequiresPermissions("teacher:import")
    public AppResponse insertBatch(@RequestBody @Validated ListTeacherDto listTeacherDto) {
        Boolean success = true;
        List<Teachers> teachersList = teachersConverter.listDtoToEntity(listTeacherDto.getData());
        for (Teachers t : teachersList) {
            Boolean res = teachersService.insertWithUser(t);
            if (!res) {
                success = false;
                break;
            }
        }
        return new AppResponse(success, success ? "批量导入成功" : "批量导入失败");
    }

    /**
     * @return Boolean
     * @description 实现前端的远端验证：尝试修改数据时，实时更新提示信息
     * @author anonymous
     * @date 2025/11/28 10:47
     */
    @ApiOperation(value = "验证数据重复")
    @RequestMapping(value = "validate", method = RequestMethod.GET)
    @ResponseBody
    @RequiresPermissions("teacher:validate")
    public Boolean validRepeat(@RequestParam("col") String col, @RequestParam("value") String value, @RequestParam(value = "id", required = false) Long id) {
        Wrapper<Teachers> teachersWrapper = new EntityWrapper<>();
        teachersWrapper = teachersWrapper.eq(col, value);
        int count = 0;
        Boolean pass = false;
        if (null != id) {
            teachersWrapper = teachersWrapper.eq("id", id);
            //查询本id下的教师信息
            Teachers teacher = teachersService.selectById(id);
            // 查询修改中的教师是否存在
            count = teachersService.selectCount(teachersWrapper);
            //只有一条是本身true
            if (count == 1) {
                pass = true;
            }
            // 查询修改中的工号是否存在
            Teachers isCode = teachersService.selectOne(new EntityWrapper<Teachers>().eq("code", value));
            // 如果工号不存在并教师查询为0则true
            if (isCode == null && count == 0) {
                pass = true;
            }
            //如果工号存在并且不是本身工号则false
            if (isCode != null && count == 0 && teacher.getCode() != value) {
                pass = false;
            }
        } else {
            // 添加时验证
            count = teachersService.selectCount(teachersWrapper);
            if (count == 0) {
                pass = true;
            }
        }
        return pass;
    }


    /**
     * @return
     * @description 根据选中的id数组导出数据
     * @author anonymous
     * @date 2025/11/28 8:53
     */
    @ApiOperation(value = "导出", notes = "导出")
    @PostMapping(value = "/export")
    @ResponseBody
    @RequiresPermissions("teacher:export")
    public void export(@RequestBody List<Long> ids, HttpServletResponse response) throws Exception {
        //获取数据
        List<Teachers> list = teachersService.selectList(new EntityWrapper<Teachers>().in("id", ids));
        //excel标题
        String[] title = {"姓名", "工号", "性别", "身份证号", "电话", "创建时间"};

        //创建院系实例
//        Department department = new Department();
        //标准时间格式为：年月日时分秒
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //excel文件名
        //String fileName = "专业信息表"+System.currentTimeMillis()+".xls";
        String fileName = "教师信息表" + ".xlsx";
        //sheet名
        String sheetName = "教师信息表";
        int line = 0;
        line = list.size();
        String[][] content = new String[line][];
        for (int i = 0; i < list.size(); i++) {
            content[i] = new String[title.length];
            Teachers obj = list.get(i);
            content[i][0] = obj.getName();
            content[i][1] = obj.getCode();
            content[i][2] = obj.getSex();
            content[i][3] = obj.getCarNo();
            content[i][4] = obj.getContact();
            content[i][5] = format.format(obj.getCreationTime());
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
     * @param
     * @return
     * @description 发送响应流方法
     * @author anonymous
     * @date 2025/11/28 10:59
     */
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
    * @description  教师首页统计
    * @author anonymous
    * @date 2025/5/14 8:38
    * @param
    * @return
    */
    @ApiOperation(value = "教师首页统计")
    @GetMapping(value = "getDashboardCount")
    @RequiresPermissions("teacher:statistics")
    public DashboardCount getDashboardCount(){
        Long id = ShiroKit.getUserId();
        DashboardCount dashboardCount = teachersService.getDashboardCount(id);
        return dashboardCount;
    }

    /**
    * @description  学校管理端首页统计
    * @author anonymous
    * @date 2025/5/14 8:40
    * @param
    * @return
    */
    @ApiOperation(value = "学校管理端首页统计")
    @GetMapping(value = "getSchoolDashboardCount")
    @RequiresPermissions("teacher:statistics")
    public DashboardCount getSchoolDashboardCount(){
        DashboardCount dashboardCount = teachersService.getSchoolDashboardCount(ShiroKit.getUserId());
        return dashboardCount;
    }
}
