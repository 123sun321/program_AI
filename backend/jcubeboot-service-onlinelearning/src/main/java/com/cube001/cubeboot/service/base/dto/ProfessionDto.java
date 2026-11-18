package com.cube001.cubeboot.service.base.dto;

import com.cube001.cubeboot.base.entity.Grade;
import com.cube001.cubeboot.core.validateGroup.EditChecks;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
* Copyright (C), 2025 - 2026, zxy.
* FileName: ProfessionDto.java
* 专业DTO：添加、修改、展示、删除
*
* @author 马勇超
* @date 2025/12/4 14:46
* @version 1.0
*/
@Data
public class ProfessionDto {

    /** 专业id */
    @NotNull(groups = EditChecks.class)
    private Long id;

    /** 名称 */
    @NotBlank
    @Length(max = 20,message = "专业名称超过20个字符")
    private String name;

    /** 排序 */
    private Integer sort;

    /** 专业编号*/
    private String code;

    /** 创建日期 */
    private Date creationTime;

    /** 班级列表 */
    private List<Grade> gradeList;
}
