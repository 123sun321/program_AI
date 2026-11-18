package com.cube001.cubeboot.service.base.initialization;
import com.cube001.cubeboot.service.course.dto.CourseDto;
import lombok.Data;

import java.util.List;

/**
* @description 所有公开课程信息包含标签、课程实验
* @author 马勇超
* @date 2025/8/8 10:59
* @param
* @return
*/
@Data
public class InitialCourse{
    private CourseDto course;
    private List<String> courseTags;
    private List<InitialExercise> exerciseList;
}
