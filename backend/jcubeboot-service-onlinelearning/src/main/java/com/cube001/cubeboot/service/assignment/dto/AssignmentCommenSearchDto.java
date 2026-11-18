package com.cube001.cubeboot.service.assignment.dto;

import com.cube001.cubeboot.core.dto.CommonSearchDto;
import org.apache.poi.ss.formula.functions.T;

public class AssignmentCommenSearchDto<T> extends CommonSearchDto<T> {
    /*是否提交标志*/
    private  Boolean submit;
    /* 课程ID*/
    private Long courseID;

    private Long gradeID;

    private Long groupID;

    public Long getGradeID() {
        return gradeID;
    }

    public void setGradeID(Long gradeID) {
        this.gradeID = gradeID;
    }

    public Long getGroupID() {
        return groupID;
    }

    public void setGroupID(Long groupID) {
        this.groupID = groupID;
    }

    public Long getCourseID() {
        return courseID;
    }

    public void setCourseID(Long courseID) {
        this.courseID = courseID;
    }

    public void setSubmit(Boolean submit) {
        this.submit = submit;
    }

    public Boolean isSubmit() {
        return submit;
    }
}
