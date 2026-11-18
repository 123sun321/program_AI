package com.cube001.cubeboot.service.assignment.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.assignment.entity.Assignment;
import com.cube001.cubeboot.assignment.entity.AssignmentSubmit;
import com.cube001.cubeboot.course.entity.Course;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class AssignmentDto {
    /** 编号 */
    @NotNull(groups = Assignment.class)
    private  Long id;

    /** 所属课程 */
    @NotNull(message = "课程不能为空")
    private Long courseId;
    /** 学年 */

    private String year;
    /** 所属班级 */

    private Long gradeId;
    /** 标题 */
    @NotBlank(message = "标题不能空")
    private String title;
    /** 作业要求 */
    @NotBlank(message = "作业要求不能为空")
    private String summary;
    /** 排序 */
    @NotNull(message = "排序不能为空")
    private Integer sort;
    /** 提交人数 */
    private Integer submitNumber;
    /** 评分 分为已评分1和未评分0 */
    private Integer noScore;
    /**小组编号 */
    private Long groupId;
    /**团队类别 */
    private Integer teamCategory;
    /** 作业创建日期 */
    private Date creationTime;
    /** 是否提交作业 */
    private Boolean isSubmit;
    /** 提交作业分数 */
    private Integer submitScore;
    /** 提交作业是否已经批改 */
    private Boolean whetherScore;
    /** 课程 */
    private Course course;
    /**提交作业详情*/
    private AssignmentSubmitDto assignmentSubmitDto;
}
