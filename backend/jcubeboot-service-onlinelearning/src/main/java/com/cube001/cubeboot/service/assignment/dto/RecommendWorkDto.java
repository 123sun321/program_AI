package com.cube001.cubeboot.service.assignment.dto;

import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.core.authorization.entity.User;
import com.cube001.cubeboot.service.training.dto.CodeDto;
import lombok.Data;

import java.util.Date;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [ 文件名 ] RecommendWorkDto
 * 优秀作品实体Dto
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/4/22$ 11:21$
 */
@Data
public class RecommendWorkDto {
    private Long id;
    private Long studentId;
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
    private Date creationTime;
    /** 学生实体 */
    private Student student;
    /** 该用户是否点赞 */
    private Boolean flag;
    /** 该作品点赞次数 */
    private Integer approveCount;
    /** 用户表信息 */
    private User user;
    /**优秀作品代码*/
    private CodeDto codeDto;
}
