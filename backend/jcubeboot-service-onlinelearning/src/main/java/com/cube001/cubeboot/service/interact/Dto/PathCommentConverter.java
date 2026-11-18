package com.cube001.cubeboot.service.interact.Dto;

import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.interact.entity.Comment;
import org.mapstruct.Mapper;

import java.util.List;

/**
* @description
* @author anonymous
* @date 2025/4/2 17:13
* @param
* @return
*/
@Mapper(componentModel = "spring")
public interface PathCommentConverter {
    Comment dtoToEntity(Comment pathCommentDto);
    Comment editDtoToEntity(PathCommentDto joinCoursePathDto);
    PathCommentDto entityToDto(Comment Comment);
    List<PathCommentDto> listEntityToDto(List<PathCommentDto> codes);
    Page<PathCommentDto> pageEntityToDto(Page<PathCommentDto> page);
}
