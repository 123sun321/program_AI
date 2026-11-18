package com.cube001.cubeboot.service.base.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.base.entity.*;
import com.cube001.cubeboot.core.authorization.entity.User;
import com.cube001.cubeboot.core.authorization.service.UserService;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.core.support.DateTimeKit;
import com.cube001.cubeboot.service.base.ProfessionsService;
import com.cube001.cubeboot.service.base.RoleExtendService;
import com.cube001.cubeboot.service.base.StudentService;
import com.cube001.cubeboot.service.base.dto.ImportStudentDto;
import com.cube001.cubeboot.service.base.dto.StudentConverter;
import com.cube001.cubeboot.service.base.dto.StudentDto;
import com.cube001.cubeboot.service.base.mapper.StudentMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/** 
* Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd. 
* FileName: StudentServiceImpl 
* 类的详细说明 
*
* @author 郭广明 
* @date   2018/11/28 13:31
* @version 1.0 
*/ 
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private StudentConverter studentConverter;
    @Autowired
    private Validator defaultValidator;
    @Autowired
    private UserService userService;

    @Autowired
    private ProfessionsService professionsService;

    @Autowired
    private RoleExtendService roleExtendService;

    /**
     * 批量删除学生
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public Boolean deleteStudentBatches(List<Long> ids){
        EntityWrapper<Student> wrapper = new EntityWrapper<>();
        wrapper.in("a.id",ids);
        List<Student> studentList = this.baseMapper.list(wrapper);
        List<Long> userIds = studentList.stream().map(m->m.getUserId()).collect(Collectors.toList());
        Boolean success = userService.deleteBatchIds(userIds);
        if(success){
            Integer count = this.baseMapper.deleteBatchIds(ids);
            if(count==0){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
            else {
                return true;
            }
        }
        return false;
    }

    /**
     * 单条删除学生
     * @param id
     * @return
     */
    @Override
    @Transactional
    public Boolean delStudent(Long id){
        Boolean b = false;
        Student student = this.baseMapper.selectById(id);
        if(null==student){
            return false;
        }
        Boolean success = userService.deleteById(student.getUserId());
        if(success){
            Integer count = this.baseMapper.deleteById(id);
           if(count==0){
               TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
           }
           else {
            b = true;
           }
        }
        return b;
    }

    /**  
     * @description 根据id获取学生详情包括专业、院系、班级
     * @author 郭广明 
     * @date 2025/11/28 15:55
     * @param id   学生id
     * @return     学生dto
    */ 
    @Override
    public StudentDto getById(Long id) {
        EntityWrapper<Student> wrapper = new EntityWrapper<>();
        wrapper.eq("a.id",id)
                .eq("a.is_deleted",0);
        Student student = studentMapper.get(wrapper);
        return studentConverter.studentToDto(student);
    }

    /**
     * @description 分页查询学生
     * @author 郭广明
     * @date 2025/11/28 16:50
     * @param page                            分页
     * @param wrapper                         条件构造器
     * @return                                分页详情
    */
    @Override
    public Page<Student> page(Page<Student> page, Wrapper<Student> wrapper) {
        List<Student> studentList = studentMapper.page(page,wrapper.eq("a.is_deleted","0").orderBy("a.creation_time",false));
        page.setRecords(studentList);
        return page;
    }

    /**
     * @description 校验Excel导入对象是否合法
     * @author 郭广明
     * @date 2025/11/28 17:02
     * @param list 封装了Excel导入实体的Object 集合
     * @return     校验过的班级Dto集合
    */
    @Override
    public List<ImportStudentDto> checkSheet(List list) {
        List<ImportStudentDto> dtoList = new ArrayList<>();
        List<Profession> gradeList = professionsService.listWithProfessionsAndGrades();
        for(int i=0;i<list.size();i++) {
            ImportStudentDto importStudentDto = new ImportStudentDto((List) list.get(i));
            importStudentDto.setSuccess(true);
            importStudentDto = checkStudent(dtoList,gradeList,importStudentDto);
            dtoList.add(importStudentDto);
        }
        return dtoList;
    }

    /**
     * @description 校验实体是否合法
     * @author 郭广明
     * @date 2025/12/3 10:36
     * @param dtoList               校验过的学生dto集合
     * @param importStudentDto      待校验的学生dto
     * @return                      校验过的学生dto
    */
    private ImportStudentDto checkStudent(List<ImportStudentDto> dtoList,List<Profession> gradeList, ImportStudentDto importStudentDto) {
        StringBuilder tips = new StringBuilder();
        for (ImportStudentDto studentDto : dtoList) {
            if (studentDto.getCode().equals(importStudentDto.getCode())) {
                importStudentDto.setSuccess(false);
                tips.append("学号已在列表里出现！;");
                break;
            }
        }
        Boolean pro = false;
        Boolean gra = false;
        for (Profession profession : gradeList) {
            if (profession.getName().equals(importStudentDto.getProfessionName())) {
                pro = true;
                importStudentDto.setProfession(profession);
                importStudentDto.setProfessionId(profession.getId());
            } else {
                continue;
            }
            for (Grade grade : profession.getGradeList()) {
                if (grade.getName().equals(importStudentDto.getGradeName()) && grade.getYear().equals(importStudentDto.getYear())) {
                    gra = true;
                    importStudentDto.setGrade(grade);
                    importStudentDto.setGradeId(grade.getId());
                    importStudentDto.setYear(grade.getYear());
                    break;
                } else {
                    continue;
                }
            }
        }

        int count = this.selectCount(new EntityWrapper<Student>().eq("code", importStudentDto.getCode()));
        if (count > 0) {
            tips.append("该学号" + importStudentDto.getCode() + "已存在！;");
            importStudentDto.setSuccess(false);
        }
        if (pro == false) {
            tips.append("该专业：" + importStudentDto.getProfessionName() + "不存在！;");
            importStudentDto.setSuccess(false);
            if (null == importStudentDto.getProfession()) {
                importStudentDto.setProfession(new Profession());
            }
        }
        if (gra == false) {
            tips.append("该年级" + importStudentDto.getYear() + importStudentDto.getGradeName() + "不存在！;");
            importStudentDto.setSuccess(false);
            if (null == importStudentDto.getGrade()) {
                importStudentDto.setGrade(new Grade());
            }
        }

        Set<ConstraintViolation<ImportStudentDto>> violationSet = defaultValidator.validate(importStudentDto); //验证
        if (violationSet.size() > 0) {
            importStudentDto.setSuccess(false);
            String err = String.join(";", violationSet.stream().map(b -> {
                return b.getMessage();
            }).collect(Collectors.joining()));
            tips.append("字段验证失败:" + err + ";");
        }
        importStudentDto.setTips(tips.toString());
        return importStudentDto;
    }

    /**
     * @description 批量上传
     * @author 郭广明
     * @date 2025/11/28 17:07
     * @param data 学生Dto集合
     * @return     批量上传成功失败状态值
    */
    @Override
    public void addList(List<StudentDto> data) {
        List<Student> studentList = studentConverter.dtoToList(data);
        Boolean res = false;
        for(Student student:studentList) {
            this.insertWithUser(student);
        }
    }

    /**
     * @description 导出所选的数据
     * @author 郭广明
     * @date 2025/11/28 17:22
     * @param ids 学生id集合
     * @return    Excel表格
    */
    @Override
    public HSSFWorkbook list(List<Long> ids) {
        EntityWrapper<Student> wrapper = new EntityWrapper<>();
        wrapper.in("a.id",ids)
                .eq("a.is_deleted",0);
        List<Student> studentList = studentMapper.list(wrapper);
        HSSFWorkbook sheets = new HSSFWorkbook();
        int width=15000;
        HSSFSheet sheet = sheets.createSheet();
        sheet.setDefaultColumnWidth(width);
        int i=0,j=0;
        HSSFRow row = null;
        HSSFCell cell = null;
        row = sheet.createRow(i);
        cell = row.createCell(j++);
        cell.setCellValue("学号");
        cell = row.createCell(j++);
        cell.setCellValue("姓名");
        cell = row.createCell(j++);
        cell.setCellValue("班级");
        cell = row.createCell(j++);
        cell.setCellValue("年级");
        cell = row.createCell(j++);
        cell.setCellValue("专业");
        cell = row.createCell(j++);
        cell.setCellValue("性别");
        cell = row.createCell(j++);
        cell.setCellValue("联系方式");
        cell = row.createCell(j++);
        cell.setCellValue("出生日期");
        cell = row.createCell(j++);
        cell.setCellValue("电子邮箱");
        cell = row.createCell(j++);
        cell.setCellValue("家庭住址");
        cell = row.createCell(j++);
        cell.setCellValue("民族");
        cell = row.createCell(j++);
        cell.setCellValue("籍贯");
        cell = row.createCell(j++);
        cell.setCellValue("政治面貌");
        cell = row.createCell(j++);
        cell.setCellValue("学历");
        cell = row.createCell(j++);
        cell.setCellValue("证件号码");
        cell = row.createCell(j++);
        cell.setCellValue("学制");
        cell = row.createCell(j++);
        cell.setCellValue("紧急联系人");
        cell = row.createCell(j++);
        cell.setCellValue("紧急联系人电话");
        cell = row.createCell(j++);
        cell.setCellValue("与紧急联系人关系");
        if(null == studentList || studentList.size()<0) {
            return sheets;
        }
        for(Student student:studentList) {
            row = sheet.createRow(++i);
            j=0;
            cell = row.createCell(j++);
            cell.setCellValue(student.getCode());
            cell = row.createCell(j++);
            cell.setCellValue(student.getName());
            cell = row.createCell(j++);
            cell.setCellValue(student.getGrade().getName());
            cell = row.createCell(j++);
            cell.setCellValue(student.getGrade().getYear());
            cell = row.createCell(j++);
            cell.setCellValue(student.getProfession().getName());
            cell = row.createCell(j++);
            cell.setCellValue(student.getSex());
            cell = row.createCell(j++);
            cell.setCellValue(student.getContact());
            cell = row.createCell(j++);
            if(null != student.getBirthday()) {
                cell.setCellValue(DateTimeKit.format(student.getBirthday(),"yyyy-MM-dd"));
            }
            cell = row.createCell(j++);
            cell.setCellValue(student.getEmail());
            cell = row.createCell(j++);
            cell.setCellValue(student.getAddress());
            cell = row.createCell(j++);
            cell.setCellValue(student.getNation());
            cell = row.createCell(j++);
            cell.setCellValue(student.getNationality());
            cell = row.createCell(j++);
            cell.setCellValue(student.getPolitics());
            cell = row.createCell(j++);
            cell.setCellValue(student.getEducation());
            cell = row.createCell(j++);
            cell.setCellValue(student.getIdnumber());
            cell = row.createCell(j++);
            cell.setCellValue(student.getSchoolingLength());
            cell = row.createCell(j++);
            cell.setCellValue(student.getLinkman());
            cell = row.createCell(j++);
            cell.setCellValue(student.getLinkmanContact());
            cell = row.createCell(j++);
            cell.setCellValue(student.getLinkmanRelation());
        }
        return sheets;
    }

    /**
     * @description 添加学生同时添加用户
     * @author 郭广明
     * @date 2025/12/3 17:31
     * @param student   学生
     * @return          添加结果
    */
    @Override
    public void insertWithUser(Student student) {
        Boolean res = false;
        res = this.insertUser(student);
        if(res == false || null == student.getUserId()) {
            throw new FriendException("创建学生用户失败！");
        }
        res = this.insert(student);
        // 添加学生角色
        if(res){
            roleExtendService.setStudentRole(student.getUserId());
        } else {
            throw new FriendException("添加学生失败！");
        }
    }

    @Override
    public List<Student> list(Wrapper<Student> wrapper) {
        return baseMapper.list(wrapper);
    }

    /**
     * @description 添加用户
     * @author 郭广明
     * @date 2025/12/3 17:32
     * @param student   学生
     * @return          添加结果
    */
    private Boolean insertUser(Student student) {
        /*学生用户类型*/
        int usertype = 1;
        User user = new User();
        user.setName(student.getName());
        user.setUserName(student.getCode());
        // 判断身份证号是否为空 若不为空默认密码设置身份证后六位 否则默认密码为000000
//        String idnumber = student.getIdnumber();
//        if(null != idnumber && idnumber.length() > 5){
//            user.setPassword(StrUtil.sub(idnumber,idnumber.length()-6,idnumber.length()));
//        }
//        else{
        user.setPassword("000000");
//        }
        user.setUserType(usertype);
        user.setSurname(student.getName());
        user.setPhoneNumber(student.getContact());
        user.setEmailAddress(student.getEmail());
        user.setIdCard(student.getIdnumber());
        user.setSex("男"==student.getSex()?1:0);
        user.setTenantid(-1);
        user.setCreationTime(new Date());
        userService.createUser(user);
        student.setUserId(user.getId());
        return true;
    }

    /**
     * 功能：根据学生姓名、学号、证件号码查询学生
     * @author HuangPengju
     * @date 2025/12/3
     * @param name 学生姓名
     * @param code 学生学号
     * @param idnumber 学生证件号码
     * @return         学生集合
     */
    @Override
    public List<Student> getStudent(String name, String code, String idnumber) {
        return this.baseMapper.getStudent(name,code,idnumber);
    }

    /**
     * 根据用户id获取学生信息(带专业名称和班级名称)
     * @description
     * @author 张向宇
     * @date 2025/01/10 14:38
     * @param userId
     * @return
     */
    @Override
    public Student getStudentByUserId(Long userId)
    {
        return  baseMapper.getStudentByUserId(userId);
    }

}
