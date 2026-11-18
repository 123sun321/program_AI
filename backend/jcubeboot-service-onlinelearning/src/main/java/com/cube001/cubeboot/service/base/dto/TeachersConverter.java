package com.cube001.cubeboot.service.base.dto;

import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.base.entity.Teachers;
import org.mapstruct.MapMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * teachers双向转换类
 * @author 黄鹏举
 * @since 2018-11-23
 */
@Mapper(componentModel = "spring")
public interface TeachersConverter {

    Teachers dtoToEntity(TeachersDto dto);
    @Mappings({
            @Mapping(source = "teachers.user.userName",target = "userName"),
            @Mapping(source = "teachers.user.emailAddress",target = "emailAddress"),
            @Mapping(source = "teachers.user.emailConfirmed",target = "emailConfirmed"),
            @Mapping(source = "teachers.user.phoneNumber",target = "phoneNumber"),
            @Mapping(source = "teachers.user.phoneNumberConfirmed",target = "phoneNumberConfirmed")
    })
    TeachersDto entityToDto(Teachers teachers);
    List<TeachersDto> listEntityToDto(List<Teachers> teachers);
    List<Teachers> listDtoToEntity(List<TeachersDto> teachersDtos);
    Page<TeachersDto> pageEntityToDto(Page<Teachers> page);
}
