package com.cube001.cubeboot.service.base;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.base.entity.Student;
import com.baomidou.mybatisplus.service.IService;
import com.cube001.cubeboot.service.base.dto.ImportStudentDto;
import com.cube001.cubeboot.service.base.dto.StudentDto;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.List;

/** 
* Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd. 
* FileName: StudentService 
* 学生业务接口
* 
* @author anonymous 
* @date   2018/11/28 13:31
* @version 1.0 
*/ 
public interface StudentService extends IService<Student> {

    /**
     * @description 根据id获取学生详情包括专业、院系、班级
     * @author anonymous
     * @date 2025/11/28 15:54
     * @param id 学生id
     * @return   学生Dto
    */
    StudentDto getById(Long id);
    /**
     * 批量删除学生
     * @param ids
     * @return
     */
    Boolean deleteStudentBatches(List<Long> ids);

    /**
     * 单条删除学生
     * @param id
     * @return
     */
    Boolean delStudent(Long id);

    /**
     * @description 分页查询学生
     * @author anonymous
     * @date 2025/11/28 16:49
     * @param page                            分页
     * @param formToEntityWrapperWithSearch   条件构造器
     * @return                                分页详情
    */
    Page<Student> page(Page<Student> page, Wrapper<Student> formToEntityWrapperWithSearch);

    /**
     * @description 校验Excel导入对象是否合法
     * @author anonymous
     * @date 2025/11/28 17:01
     * @param list 封装了Excel导入实体的Object 集合
     * @return     校验过的班级Dto集合
    */
    List<ImportStudentDto> checkSheet(List list);

    /**
     * @description 批量上传
     * @author anonymous
     * @date 2025/11/28 17:07
     * @param data 学生Dto集合
     * @return     批量上传成功失败状态值
    */
    void addList(List<StudentDto> data);

    /**
     * @description 导出所选数据
     * @author anonymous
     * @date 2025/11/28 17:16
     * @param ids 学生id集合
     * @return    Excel表格
    */
    HSSFWorkbook list(List<Long> ids);
    /**
     * 功能：根据学生姓名、学号、证件号码查询学生
     * @author anonymous
     * @date 2025/12/3
     * @param name       学生姓名
     * @param code       学生学号
     * @param idnumber   学生证件号码
     * @return           学生集合
     */
    List<Student> getStudent(@Param("name") String name, @Param("code") String code, @Param("idnumber") String idnumber);

    /**
     * @description 添加学生的同时添加用户
     * @author anonymous
     * @date 2025/12/3 17:30
     * @param student 学生实体
     * @return        添加结果
    */
    void insertWithUser(Student student);

    /**
    * @description 取数据
    * @author anonymous
    * @date 2025/12/6 17:02
    * @param wrapper
    * @return java.util.List<com.cube001.cubeboot.base.entity.Student>
    */
    List<Student> list(Wrapper<Student> wrapper);

    /**
     * 根据用户id获取学生信息(带专业名称和班级名称)
     * @description
     * @author anonymous
     * @date 2025/01/10 14:38
     * @param userId
     * @return
     */
    Student getStudentByUserId(Long userId);

}
