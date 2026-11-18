package com.cube001.cubeboot.vo.course;

import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.course.entity.ExerciseJoin;
import lombok.Data;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: ExercisePageVo
 * 练习参与分页接收Vo
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/3/15 14:16
 */
@Data
public class ExercisePageVo extends CommonSearchDto<ExerciseJoin> {
    /**练习编号*/
    private Long exerciseId;
}
