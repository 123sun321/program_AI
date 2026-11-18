package com.cube001.cubeboot.service.assignment;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.cube001.cubeboot.assignment.entity.Assignment;
import com.cube001.cubeboot.base.entity.Studenthonor;
import com.cube001.cubeboot.service.assignment.dto.AssignmentDto;
import com.cube001.cubeboot.service.assignment.dto.ListAssignmentDto;

import java.util.List;


/**
* Copyright (C), 2018 - 2019, ZhengZho uChuangZhi. Co., Ltd
* FileName:
* 类的详细说明
 * 作业的服务类
*
* @author anonymous
* @date   2019/3/21 9:00
* @version 1.0
*/


public interface AssignmentService extends IService<Assignment> {
    /**
      * @description
      * @author anonymous
      * @date 2025/3/21 9:01
      * @Param 作业 assignmentid
      * @return
      */
    void delete(Long assignmentid);
    /**
     * @description批量删除作业标题
     * @author anonymous
     * @date 2025/3/28 9:00
     * @Param List<Long> ids
     * @return
     */
    Boolean deleteBatchids(List<Long> ids);
    /**
     * @description
     * @author anonymous
     * @date 2025/3/21 10:03
     * @Param 添加作业
     * @return 操作成功或者抛出友好异常
     */
    void add(AssignmentDto assignmentDto);
    /**
     * @description作业的更新
     * @author anonymous
     * @date 2025/3/28 9:04
     * @Param AssignmentDto
     * @return
     */
    void update(AssignmentDto assignmentDto);
    /**
     * @description字段校验
     * @author anonymous
     * @date 2025/3/28 9:02
     * @Param String col字段名，String value字段值
     * @return
     */
    int getAssignmentSort(String col,String value);
    /**
     * @description分页/模糊查询
     * @author anonymous
     * @date 2025/3/28 9:03
     * @Param Page<Assignment> page 分页 Wrapper<Assignment> formToEntityWrapperWithSearch查询条件
     * @return Page<Assignment>
     */
    Page<Assignment> getStuRewardPunishList(Page<Assignment> page, Wrapper<Assignment> formToEntityWrapperWithSearch);
    /**
     * 通过用户编号获取学生的班级作业
     * @param page
     * @param wrapper
     * @param userId
     * @return
     */
    Page<AssignmentDto> searchGradPage(Page<AssignmentDto> page, Wrapper<AssignmentDto> wrapper,Long userId);

    /**
     * 通过用户编号获取学生的小组作业
     * @param page
     * @param wrapper
     * @param userId
     * @return
     */
    Page<AssignmentDto> searchGroupPage(Page<AssignmentDto> page, Wrapper<AssignmentDto> wrapper,Long userId);
    /**
    * @description  提交作业人数和未评分加一
    * @author anonymous
    * @date 2025/5/14 13:40
    * @param
    * @return
    */
    void addSubmitNumber(Long assignmentId);

    /**
     * 获取当前用户该作业的提交详情
     * @author anonymous
     * @param userId
     * @param id
     * @return
     */
    AssignmentDto getStudentAssignment(Long userId,Long id);
}
