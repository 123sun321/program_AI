package com.cube001.cubeboot.service.base.dto;

import lombok.Data;

import javax.validation.Valid;
import java.util.List;

/** 
* Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd. 
* FileName: ListGradeDto 
* 班级Dto ：批量上传
* 
* @author 郭广明 
* @date   2018/11/28 10:41
* @version 1.0 
*/ 
@Data
public class ListGradeDto {
    @Valid
    private List<GradeDto> data;
}
