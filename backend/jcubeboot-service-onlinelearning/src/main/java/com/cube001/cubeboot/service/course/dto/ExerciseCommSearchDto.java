package com.cube001.cubeboot.service.course.dto;

import com.cube001.cubeboot.core.dto.CommonSearchDto;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [ 文件名 ]
 * 类的详细说明
 *
 * @author anonymous
 * @version 1.0
 * @ D a t e   $ D A T E $ { T I M E }
 */
@Data
public class ExerciseCommSearchDto<T> extends CommonSearchDto<T> {
    private String pragramName;
}
