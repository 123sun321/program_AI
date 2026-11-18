package com.cube001.cubeboot.service.base.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/** 
* Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd. 
* FileName: ImportGradeDto 
* 班级Dto ：批量导入数据校验
*
* @author anonymous 
* @date   2018/11/28 10:41
* @version 1.0 
*/ 
@Data
public class ImportGradeDto extends GradeDto{
    private String tips;
    private Boolean success;
    private String professionName;
    private String departmentsName;

    public ImportGradeDto() {};
    public ImportGradeDto(List list) {
        this.setName((String) list.get(0));
        this.setProfessionName((String)list.get(1));
        this.setYear((String)list.get(2));
        this.setSchoolLength((String)list.get(3));
        this.setCounsellor((String)list.get(4));
        this.setInSchool((Boolean)list.get(5));
        this.setStartDate((Date)list.get(6));
        this.setGraduateDate((Date)list.get(7));
    }
}
