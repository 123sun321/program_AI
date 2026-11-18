package com.cube001.cubeboot.service.course;

import com.cube001.cubeboot.service.course.dto.ExerciseResourceDto;

import java.util.List;

public interface ExerciseResourceService {

    /**
     * 保存课程资源
     * @author anonymous
     * @param exerciseResourceDto
     * @return
     */
    Long insert(ExerciseResourceDto exerciseResourceDto);

    /**
     * 修改资源
     * @author anonymous
     * @param exerciseResourceDto
     * @return
     */
    Long update(ExerciseResourceDto exerciseResourceDto);

    /**
     * 根据实验编号获取实验资源
     * @author anonymous
     * @param exerciseId
     * @return
     */
    List<ExerciseResourceDto> getList(Long exerciseId);
}
