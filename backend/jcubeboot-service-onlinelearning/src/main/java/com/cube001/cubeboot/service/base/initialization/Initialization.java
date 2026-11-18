package com.cube001.cubeboot.service.base.initialization;
import com.cube001.cubeboot.service.course.dto.CoursePathDto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
* @description 初始化数据
* @author 马勇超
* @date 2025/8/8 11:06
* @param
* @return
*/
@Data
public class Initialization implements Serializable {
    List<InitialCourse> initialCourseList;
    List<InitialExercise> initialExerciseList;
    List<CoursePathDto> coursePath;
}
