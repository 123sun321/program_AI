package com.cube001.cubeboot.service.community.dto;

import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.community.entity.CommunityPlate;
import com.cube001.cubeboot.community.entity.IntegralScore;
import com.cube001.cubeboot.course.entity.Course;
import com.cube001.cubeboot.service.course.dto.CourseDto;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Auther: 无聊的人
 * @Date: 2019/3/23
 * @Description: com.cube001.cubeboot.service.community.dto
 */
@Mapper(componentModel = "spring")
public interface CommunityScoreConverter {

    IntegralScore dtoToEntity(IntegralScoreDto integralScoreDto);
    IntegralScoreDto entityToDto(IntegralScore integralScore);
    List<IntegralScoreDto> dtoToListCommunityScore(List<IntegralScore> communityScoreList);
    Page<IntegralScoreDto> pageCommunityScoreDto(Page<IntegralScore> page);
    List<IntegralScore> communityScoreDtoList(List<IntegralScoreDto> communityScoreDtoList);

}
