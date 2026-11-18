package com.cube001.cubeboot.service.training.dto;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.cube001.cubeboot.core.validateGroup.EditChecks;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [ TrainingPathDto ]
 * 类的详细说明 : 练习目录的Dto
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/3/17 14:19
 */
@Data
public class TrainingPathDto {

    @NotNull(groups = EditChecks.class)
    private Long id;

    @NotBlank(message = "目录名称不可以为空")
    @Length(min=1,max = 20,message = "目录名称为1-20个字符")
    /**目录名称 */
    private String title;

    /**目录父编号   */
    private Long parentId;

    /** 递归子目录 */
    private List<TrainingPathDto> children = new ArrayList<>(8);


}
