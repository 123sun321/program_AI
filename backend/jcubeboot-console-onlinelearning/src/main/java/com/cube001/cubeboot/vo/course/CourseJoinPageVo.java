package com.cube001.cubeboot.vo.course;

import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.course.entity.CourseJoin;
import lombok.Data;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: CourseJoinPageVo
 * CourseJoinPageVo
 *
 * @author Juci
 * @version 1.0
 * @date 2025/7/5 17:52
 */
@Data
public class CourseJoinPageVo extends CommonSearchDto<CourseJoin> {
    /**练习编号*/
    private Long courseId;
 }
