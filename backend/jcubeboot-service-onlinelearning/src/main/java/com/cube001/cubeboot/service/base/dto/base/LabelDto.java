package com.cube001.cubeboot.service.base.dto.base;

import com.cube001.cubeboot.core.validateGroup.EditChecks;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: [ 文件名 ]
 * 类的详细说明
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/3/8$ 17:57$
 */
@Data
public class LabelDto {

    /**
     * 标签编号
     */
    @NotNull(groups = EditChecks.class)
    private Long id;
    /**
     * 标签名字
     */
    @NotBlank(message = "标签不能为空")
    @Length(min=1,max = 20,message = "标签为1-20个字符")
    private String name;

    private String pinyin;
}
