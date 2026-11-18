package com.cube001.cubeboot.assignment.entity;

import com.cube001.cubeboot.training.entity.Code;
import lombok.Data;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [ 文件名 ]
 * 类的详细说明
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/5/9$ 17:27$
 */
@Data
public class AssignmentSubmitWithCode{
    /** 作业Id */
    private Long assignmentId;
    /**编辑记录*/
    private String recordData;
    /** 代码 */
    private Code code;
}
