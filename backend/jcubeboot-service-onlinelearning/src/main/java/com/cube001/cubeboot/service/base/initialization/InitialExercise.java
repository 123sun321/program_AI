package com.cube001.cubeboot.service.base.initialization;
import com.cube001.cubeboot.course.entity.ExerciseResource;
import com.cube001.cubeboot.service.course.dto.ExerciseDto;
import com.cube001.cubeboot.service.course.dto.ExerciseResourceDto;
import com.cube001.cubeboot.service.training.dto.CodeDto;
import com.cube001.cubeboot.training.entity.Code;
import lombok.Data;

import java.util.List;

/**
* @description 所有实验信息包含标签、实验资源、代码
* @author anonymous
* @date 2025/8/8 10:59
* @param
* @return
*/
@Data
public class InitialExercise {
    private ExerciseDto exerciseDto;
    private List<String> exerciseTags;
    private CodeDto code;
    private List<ExerciseResourceDto> resourceList;
}
