package com.cube001.cubeboot.service.training.dto;


import com.baomidou.mybatisplus.annotations.TableField;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.core.authorization.entity.User;
import com.cube001.cubeboot.core.validateGroup.EditChecks;
import com.cube001.cubeboot.training.entity.Code;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: TrainingDto.java
 * 练习表类的详细说明
 *
 * @author 宋泽山
 * @version 1.0
 * @date 2025/3/16 14:41
 */
@Data
public class TrainingDto {
    @NotNull(groups = EditChecks.class)
    private Long id;

    /**标题 */
    @NotBlank(message = "标题不能为空")
    @Length(min=1,max = 20,message = "标题在1-20个字符之间")
    private String title;

    /**代码编号*/
    private Long codeId;

    /**简介 */
    private String summary;
    /**是否私有,私有1,非私有0 */
    private Boolean privateOrNot;
    /**是否可编辑代码*/
    private Boolean canEdit;
    /**类型 */
    @NotBlank(message = "类型不能为空")
    @Length(min=1,max = 20,message = "标题在1-20个字符之间")
    private String type;
    /**创建时间*/
    private Date creationTime;
    /**练习代码*/
    private Code code;
    /**练习代码，含练习时间*/
    private CodeDto codeDto;
    /**点赞数 */
    private Integer likeNumber;
    /**学生*/
    private Student student;
    /**用户*/
    private User user;
}
