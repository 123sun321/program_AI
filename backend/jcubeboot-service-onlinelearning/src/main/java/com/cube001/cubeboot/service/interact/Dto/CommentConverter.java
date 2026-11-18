package com.cube001.cubeboot.service.interact.Dto;

import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.interact.entity.Comment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentConverter {
    Comment dtoToEntity(CommentDto dto);
    CommentDto entityToDto(Comment course);
    List<CommentDto> listEntityToDto(List<Comment> courses);
    List<Comment> listDtoToEntity(List<CommentDto> courseDtos);
    Page<CommentDto> pageEntityToDto(Page<Comment> page);
}

