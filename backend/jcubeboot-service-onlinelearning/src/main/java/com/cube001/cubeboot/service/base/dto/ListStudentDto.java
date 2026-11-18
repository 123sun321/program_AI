package com.cube001.cubeboot.service.base.dto;

import lombok.Data;

import javax.validation.Valid;
import java.util.List;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [ListStudentDto ]
 * 学生Dto ：批量添加
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/11/2817:03
 */
@Data
public class ListStudentDto {

    @Valid
    private List<StudentDto> data;
}
