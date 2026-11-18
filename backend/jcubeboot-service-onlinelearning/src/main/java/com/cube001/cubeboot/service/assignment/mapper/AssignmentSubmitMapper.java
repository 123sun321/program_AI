package com.cube001.cubeboot.service.assignment.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.assignment.entity.Assignment;
import com.cube001.cubeboot.assignment.entity.AssignmentSubmit;
import com.cube001.cubeboot.service.assignment.dto.AssignmentSubmitDto;
import com.cube001.cubeboot.service.assignment.dto.GetCountAssignmentSubmitDto;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 *  作业提交详情mapper
 * </p>
 *
 * @author renjie
 * @since 2019-03-22
 */

public interface AssignmentSubmitMapper extends BaseMapper<AssignmentSubmit> {

    List<AssignmentSubmit> getAssignmentList(Page<AssignmentSubmit> page,@Param("id")Long id,@Param("courseId")Long courseId,  @Param("ew") Wrapper<AssignmentSubmit> wrapper);
    GetCountAssignmentSubmitDto getAssignmentId(@Param("assignmentid")Long assignmentid,@Param("courseid")Long courseid,@Param("ew") Wrapper<AssignmentSubmit> wrapper );

    /**
     * @description  获取所有为评分的作业列表
     * @author 马勇超
     * @date 2025/4/8 14:11
     * @return   list
     */
    List<AssignmentSubmit> getNoScoreList();

    /**
     * 通过编号获取作业提交详情及代码
     * @author Juci
     * @param id
     * @return
     */
    AssignmentSubmitDto getEntityWithCode(@Param("id")Long id);

    /**
     * @description 根据提交作业id获取作业和学生信息
     * @author 马勇超
     * @date 2025/7/4 15:36
     * @param id
     * @return
     */
    AssignmentSubmit getStudentAndAssignmentById(Long id);
}
