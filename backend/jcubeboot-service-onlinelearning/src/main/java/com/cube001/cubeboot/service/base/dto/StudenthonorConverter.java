package com.cube001.cubeboot.service.base.dto;

import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.base.entity.Studenthonor;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Copyright (C),2018-2019, ZhengZhouChuangZhi. Co.,Ltd.
 * FileName: [SchoolhonorConverter]
 * SchoolhonorConverter双向转换类接口
 *
 * @author HuangPengju
 * @Date 2018-11-27
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface StudenthonorConverter {
    Studenthonor dtoToEntity(StudenthonorDto dto);
    Studenthonor editDtoToEntity(StudenthonorEditDto dto);
    StudenthonorDto entityToDto(Studenthonor studenthonor);
    List<StudenthonorDto> listEntityToDto(List<Studenthonor> studenthonors);
    Page<StudenthonorDto> pageEntityToDto(Page<Studenthonor> page);
}
