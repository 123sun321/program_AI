package com.cube001.cubeboot.assignment.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.Version;

import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.core.authorization.entity.User;
import com.cube001.cubeboot.core.domain.entity.FullAuditedEntityNPk;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author SZS
 * @since 2019-04-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_recommend_work")
public class RecommendWork extends FullAuditedEntityNPk<RecommendWork> {

    private static final long serialVersionUID = 1L;
    @TableId(type= IdType.ID_WORKER)
    private Long id;
    private Long studentId;
    private Long assignmentSubmitId;
    private String title;
    private Integer like;
    private Integer sort;
    private Long codeId;
    private Integer frok;
    private Integer codeViewCount;
    private Integer pageViewCount;
    private Integer fileType;
    private String codeFileFolder;
    private String recommenderName;
    /** 学生实体 */
    @TableField(exist = false)
    private Student student;
    /** 该用户是否点赞 */
    @TableField(exist = false)
    private Boolean flag;
    /** 该作品点赞次数 */
    @TableField(exist = false)
    private Integer approveCount;
    /** 用户表信息 */
    @TableField(exist = false)
    private User user;
}
