package com.cube001.cubeboot.service.community.dto;

import com.cube001.cubeboot.community.entity.SignRecord;
import lombok.Data;

import java.util.List;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [ 文件名 ]
 * 类的详细说明
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/3/25$ 14:02$
 */
@Data
public class SignStudentGradeRecord {

    private List<SignRecordDto> student;
    private List<SignRecordDto> grade;
    private List<SignRecordDto> everyDay;

}
