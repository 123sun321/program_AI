package com.cube001.cubeboot.service.base.dto;

import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.base.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [ 文件名 ]
 * 类的详细说明
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/11/2815:15
 */
@Mapper(componentModel = "spring")
public interface StudentConverter {

    Student dtoToStudent(StudentDto studentDto);
    @Mappings({
            @Mapping(source = "student.user.userName",target = "userName"),
            @Mapping(source = "student.user.emailAddress",target = "emailAddress"),
            @Mapping(source = "student.user.emailConfirmed",target = "emailConfirmed"),
            @Mapping(source = "student.user.phoneNumber",target = "phoneNumber"),
            @Mapping(source = "student.user.phoneNumberConfirmed",target = "phoneNumberConfirmed")
    })
    StudentDto studentToDto(Student student);
    List<StudentDto> listToDto(List<Student> studentList);
    List<Student> dtoToList(List<StudentDto> studentDtoList);
    Page<StudentDto> pageToDto(Page<Student> studentPage);
}
