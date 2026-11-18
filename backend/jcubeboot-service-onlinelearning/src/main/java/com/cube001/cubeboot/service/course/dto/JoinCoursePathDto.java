package com.cube001.cubeboot.service.course.dto;

import lombok.Data;

/**
 * Copyright (C),2018-2019, ZhengZhouChuangZhi. Co.,Ltd.
 * FileName: [文件名]
 * 类的详细说明
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/3/14 17:27
 */
@Data
public class JoinCoursePathDto {
    private Long id;
    private Long studentId;
    private Integer studyNumber;
    private String code;
    private String name;
    private String gradename;
    private String creationTime;
    private Integer finishNumber;
}
