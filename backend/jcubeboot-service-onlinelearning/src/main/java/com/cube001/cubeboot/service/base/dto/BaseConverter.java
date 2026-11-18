package com.cube001.cubeboot.service.base.dto;

import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.base.entity.Grade;
import com.cube001.cubeboot.base.entity.Profession;
import com.cube001.cubeboot.course.entity.CourseJoin;
import com.cube001.cubeboot.interact.entity.Comment;
import com.cube001.cubeboot.service.course.dto.CourseJoinDto;
import com.cube001.cubeboot.service.interact.Dto.CommentDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BaseConverter {

    Grade dtoToGrade(GradeDto gradeDto);
    GradeDto gradeToDto(Grade grade);
    List<GradeDto> gradeList(List<Grade> gradeList);
    List<Grade> gradeDtoList(List<GradeDto> gradeDtoList);
    Page<GradeDto> gradeDtoPage(Page<Grade> gradePage);

    Profession dtoToProfession(ProfessionDto professionDto);
    ProfessionDto professionToDto(Profession profession);
    List<ProfessionDto> dtoToListProfession(List<Profession> professionList);
    Page<ProfessionDto> pageProfessionToDto(Page<Profession> page);
    List<Profession> professionDtoList(List<ProfessionDto> professionDtoList);

    /**课程参与详情转换*/
    CourseJoin dtoToCourseParticipationDetails(CourseJoinDto courseJoinDto);
    CourseJoinDto courseParticipationDetailsToDto(CourseJoin courseJoin);
    List<CourseJoinDto> courseParticipationDetailsList(List<CourseJoin> participationDetailsList);
    List<CourseJoin> courseParticipationDetailsDtoList(List<CourseJoinDto> participationDetailsDtoList);
    Page<CourseJoinDto> courseParticipationDetailsDtoPage(Page<CourseJoin> participationDetailsPage);


    /**评论转换*/
    Comment dtoToGrade(CommentDto commentDto);
    CommentDto commentToDto(Comment comment);
    List<CommentDto> commentList(List<Comment> commentList);
    List<Comment> commentDtoList(List<CommentDto> commentDtoList);
    Page<CommentDto> commentDtoPage(Page<Comment> commentPage);


}
