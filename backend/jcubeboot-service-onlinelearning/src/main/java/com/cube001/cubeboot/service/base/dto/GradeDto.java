package com.cube001.cubeboot.service.base.dto;

import com.cube001.cubeboot.core.validateGroup.EditChecks;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;


/** 
* Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd. 
* FileName: GradeDto 
* 班级Dto：添加、修改、展示
* 
* @author anonymous 
* @date   2018/11/28 10:41
* @version 1.0 
*/ 
@Data
public class GradeDto {
    @NotNull(groups = EditChecks.class)
    private Long id;
    @NotBlank(message = "班级名称不能为空")
    @Length(min=1,max = 20,message = "班级名称为1-20个字符")
    private String name;
    @NotBlank(message = "年纪不能为空")
    private String year;
    @NotBlank(message = "学制不能为空")
    private String schoolLength;
    private String counsellor;
    @NotNull(message = "专业不能为空")
    private Long professionId;
    private Boolean inSchool;
    @NotNull(message = "入学时间不能为空")
    private Date startDate;
    @NotNull(message = "毕业时间不能为空")
    private Date graduateDate;
    private Integer classSize;

    private ProfessionDto profession;
}
