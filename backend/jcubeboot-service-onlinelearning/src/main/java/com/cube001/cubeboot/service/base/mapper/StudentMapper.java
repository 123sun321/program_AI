package com.cube001.cubeboot.service.base.mapper;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cube001.cubeboot.base.entity.Grade;
import com.cube001.cubeboot.base.entity.Student;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cube001.cubeboot.core.permission.DataPoint;
import com.cube001.cubeboot.service.base.dto.StudentDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/** 
* Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd. 
* FileName: StudentMapper
* 类的详细说明 
* 
* @author anonymous 
* @date   2018/11/28 13:34
* @version 1.0 
*/
@DataPoint
public interface StudentMapper extends BaseMapper<Student> {

    /**
     * @description 自定义sql查询
     * @author anonymous
     * @date 2025/11/28 16:01
     * @param wrapper   条件构造器
     * @return          学生
    */
    Student get(@Param("ew") Wrapper<Student> wrapper);

    /**
     * 根据用户id获取学生信息(带专业名称和班级名称)
     * @description
     * @author anonymous
     * @date 2025/01/10 14:38
     * @param userId
     * @return
     */
    Student getStudentByUserId(@Param("userId") Long userId);

    /**
     * @description 分页查询学生
     * @author anonymous
     * @date 2025/11/28 16:54
     * @param page       分页
     * @param wrapper    条件构造器
     * @return           学生集合
    */
    List<Student> page(Pagination page, @Param("ew") Wrapper<Student> wrapper);

    /**
     * @description 自定义sql查询集合
     * @author anonymous
     * @date 2025/11/28 17:21
     * @param wrapper    条件构造器
     * @return           学生集合
    */
    List<Student> list(@Param("ew") Wrapper<Student> wrapper);

    /**
     * 功能：根据学生姓名、学号、证件号码查询学生
     * @author anonymous
     * @date 2025/12/3
     * @param name 学生姓名
     * @param code 学生学号
     * @param idnumber 学生证件号码
     * @return
     */
    List<Student> getStudent(@Param("name") String name,@Param("code") String code,@Param("idnumber") String idnumber);

}
