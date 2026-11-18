package com.cube001.cubeboot.service.assignment.dto;


import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.assignment.entity.Assignment;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper( componentModel ="spring")
public interface AssignmentConverter {
    Assignment dtoToEntity(AssignmentDto AssignmentDto);
    Assignment editDtoToEntity(AssignmentDto AssignmentDto);
    AssignmentDto entityToDto(Assignment Assignment);
    List<ListAssignmentDto> listEntityToDto(List<Assignment> Assignments);
    Page<ListAssignmentDto> pageEntityToDto(Page<Assignment> page);
    List<AssignmentDto> list2Ato (List<Assignment> assignments);
    Page<AssignmentDto> pageEntity2Dto(Page<Assignment> page);
}
