package com.cube001.cubeboot.service.assignment;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.cube001.cubeboot.assignment.entity.AssignmentSubmit;
import com.cube001.cubeboot.service.assignment.dto.AssignmentSubmitDto;
import com.cube001.cubeboot.service.assignment.dto.GetCountAssignmentSubmitDto;
import com.cube001.cubeboot.training.entity.Code;

import java.util.List;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author renjie
 * @since 2019-03-22
 */
public interface AssignmentSubmitService extends IService<AssignmentSubmit> {
    /**
      * @description
      * @author 张人杰
      * @date 2025/3/25 11:24
      * @Param 分页查询
      * @return
      */

    Page<AssignmentSubmit> getAssignmentSubmitList(Page<AssignmentSubmit> page,Long id,Long courseId, Wrapper<AssignmentSubmit> formToEntityWrapperWithSearch);
    /**
      * @description
      * @author 张人杰
      * @date 2025/3/25 13:36
      * @Param 作业提交详情id 评分score
      * @return
      */

    void editorScore(Long assignmentSubmitid, Integer Score);
    /**
     * @description作业提交中 把学生的作业设置为推荐作品
     * @author 张人杰
     * @date 2025/3/28 9:08
     * @Param Long assignmentSubmitid作业提交Id
     * @return
     */
    void editorRecommend(Long assignmentSubmitid);
    /**
     * @description作业提交中 把学生的作业设置的推荐作品取消
     * @author 张人杰
     * @date 2025/5/15 11:15
     * @Param Long assignmentSubmitid作业提交Id
     * @return
     */
    void editordelRecommend(Long assignmentSubmitid);
    /**
      * @description通过作业提交Id获取整条作业的信息
      * @author 张人杰
      * @date 2025/3/28 9:12
      * @Param Long assignmentSubmitId 作业提交Id
      * @return
      */

    GetCountAssignmentSubmitDto getByassignmentId(Long assignmentId ,Long courseid,Long gradeid,Long groupid);


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
    AssignmentSubmitDto getEntityWithCode(Long id);
    /*  作业对比查重
    * @description
    * @author 张人杰
    * @date 2025/7/29 10:02
    * @param
    * @return
    */

    String gettest1(List<Long> ids,String tenantId);

    /**
      * @description 根据提交作业id获取作业和学生信息
      * @author 马勇超
      * @date 2025/7/4 15:36
      * @param id
      * @return
     */
    AssignmentSubmit getStudentAndAssignmentById(Long id);
}
