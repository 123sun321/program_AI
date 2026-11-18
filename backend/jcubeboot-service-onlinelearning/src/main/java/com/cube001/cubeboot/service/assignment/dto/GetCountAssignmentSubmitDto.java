package com.cube001.cubeboot.service.assignment.dto;

import lombok.Data;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [ 文件名 ]
 * 用于作业提交接口的函数计算
 * 总提交人数 最高分 最低分
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/3/25 15:10
 */

@Data
public class GetCountAssignmentSubmitDto {
    /** 作业提交总人数*/
    private Integer number;
    /** 作业提交人数*/
    private Integer submtnumber;
    /**作业提交评分最高评分 */
    private  Integer maxnumber;
    /**作业提交评分最低评分 */
    private Integer minnumber;



}
