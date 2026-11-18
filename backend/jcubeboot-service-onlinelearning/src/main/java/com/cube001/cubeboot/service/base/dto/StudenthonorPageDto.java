package com.cube001.cubeboot.service.base.dto;

import com.cube001.cubeboot.base.entity.Studenthonor;
import com.cube001.cubeboot.core.dto.CommonSearchDto;
import lombok.Data;

import java.util.List;


/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [StudenthonorPageDto ]
 * 分页查询搜索、学生奖惩dto类
 *
 * @author HuangPengju
 * @version 1.0
 * @date 2025/11/30
 */
@Data
public class StudenthonorPageDto extends CommonSearchDto<Studenthonor>  {

    /** 学生姓名 */
    private String studentName;

    /** 奖惩类型 */
    private String type;

    /** 奖惩级别 */
    private String level;

    /** 学生 专业id */
    private Long professionId;

   /** 学生学院id */
    private Long departmentId;

   /** 学生年级 */
    private String grade;
}
