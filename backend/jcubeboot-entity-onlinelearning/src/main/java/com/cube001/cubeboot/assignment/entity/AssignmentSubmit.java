package com.cube001.cubeboot.assignment.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.base.entity.Grade;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 *      作业提交详情实体类
 * </p>
 *
 * @author renjie
 * @since 2019-03-22
 */
@Data
@Accessors(chain = true)
@TableName("t_assignment_submit")
public class AssignmentSubmit extends FullAuditedEntityNPk<AssignmentSubmit> {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ID_WORKER )
    private Long id;
    /** 作业编号 */
    private Long assignmentId;
    /** 学生编号 */
    private Long studentId;
    /** 评分 */
    private Integer score;
    /** 推荐作品 */
    private int recommend;
    /** 对应练习 */
    private Long trainingId;
    /**代码编辑记录*/
    private String recordData;
    /** 是否评分**/
    private Boolean whetherScore;
    /*代码相似度*/
    private String codeSimilarity;
    /*判断是否提交*/
    @TableField(exist = false)
    private boolean submit;
    //学生的对象
    @TableField(exist = false)
    private Student student;
    //作业对象
    @TableField(exist = false)
    private Assignment assignment;
    //班级对象
    @TableField(exist = false)
    private Grade grade;


}
