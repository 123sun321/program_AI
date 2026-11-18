package com.cube001.cubeboot.service.assignment.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.assignment.entity.Assignment;
import com.cube001.cubeboot.base.entity.Studenthonor;
import com.cube001.cubeboot.service.assignment.dto.AssignmentDto;
import com.cube001.cubeboot.service.assignment.dto.ListAssignmentDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author renjie
 * @since 2019-03-18
 */
public interface AssignmentMapper extends BaseMapper<Assignment> {

    List<Assignment>  getAssignmentList(Page<Assignment> page, @Param("ew") Wrapper<Assignment> wrapper);
    void editeNoScore(Long id);
    /**
     * @description  提交作业人数和未评分加一
     * @author 马勇超
     * @date 2025/5/14 13:40
     * @param
     * @return
     */
    void addSubmitNumber(Long assignmentId);

    /**
     * 获取当前用户该作业的提交详情
     * @author Juci
     * @param studentId
     * @param id
     * @return
     */
    AssignmentDto getStudentAssignment(@Param("studentId") Long studentId,@Param("id") Long id);

    /**
     * 获取当前学生的班级作业
     * @param page
     * @param gradeId
     * @param studentId
     * @param wrapper
     * @return
     */
    List<AssignmentDto>  searchGradPage(Page<AssignmentDto> page,@Param("gradeId") Long gradeId,@Param("studentId") Long studentId, @Param("ew") Wrapper<AssignmentDto> wrapper);

    /**
     * 获取当前学生的小组作业
     * @param page
     * @param studentId
     * @param wrapper
     * @return
     */
    List<AssignmentDto> searchGroupPage(Page<AssignmentDto> page,@Param("studentId") Long studentId, @Param("ew") Wrapper<AssignmentDto> wrapper);
}
