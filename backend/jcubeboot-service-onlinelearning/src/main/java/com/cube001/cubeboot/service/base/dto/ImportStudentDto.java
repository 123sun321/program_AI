package com.cube001.cubeboot.service.base.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [ 文件名 ]
 * 学生Dto ：批量导入数据校验
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/11/2816:57
 */
@Data
public class ImportStudentDto  extends  StudentDto{

    private String tips;
    private Boolean success;

    private String gradeName;
    private String departmentName;
    private String professionName;

    public ImportStudentDto() {}

    public ImportStudentDto(List list) {
        int i=0;

        this.setCode((String)list.get(i++));
        this.setName((String)list.get(i++));
        this.setGradeName((String)list.get(i++));
        this.setYear((String)list.get(i++));
        this.setProfessionName((String)list.get(i++));
//        this.setDepartmentName((String)list.get(i++));
        this.setSex((String)list.get(i++));
        this.setContact((String)list.get(i++));
        this.setBirthday((Date) list.get(i++));
        this.setEmail((String)list.get(i++));
        this.setAddress((String)list.get(i++));
        this.setNation((String)list.get(i++));
        this.setNationality((String)list.get(i++));
        this.setPolitics((String)list.get(i++));
        this.setEducation((String)list.get(i++));
        this.setIdnumber((String)list.get(i++));
        this.setSchoolingLength((String)list.get(i++));
        this.setLinkman((String)list.get(i++));
        this.setLinkmanContact((String)list.get(i++));
        this.setLinkmanRelation((String)list.get(i++));

    }
}
