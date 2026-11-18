package com.cube001.cubeboot.service.base.dto;

import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.base.entity.BaseLibrary;
import com.cube001.cubeboot.base.entity.Grade;
import com.cube001.cubeboot.base.entity.Profession;
import com.cube001.cubeboot.course.entity.CourseJoin;
import com.cube001.cubeboot.interact.entity.Comment;
import com.cube001.cubeboot.service.course.dto.CourseJoinDto;
import com.cube001.cubeboot.service.interact.Dto.CommentDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BaseLibraryConverter {

    BaseLibrary dtoToEntity(BaseLibraryDto baseLibraryDto);
    BaseLibraryDto entityToDto(BaseLibrary baseLibrary);
    List<BaseLibraryDto> dtoList(List<BaseLibrary> baseLibraries);
    List<BaseLibrary> entityList(List<BaseLibraryDto> baseLibraryDtos);
    Page<BaseLibraryDto> dtoPage(Page<BaseLibrary> baseLibraryPage);
}
