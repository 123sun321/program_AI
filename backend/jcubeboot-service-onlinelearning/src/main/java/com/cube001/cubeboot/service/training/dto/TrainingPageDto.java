package com.cube001.cubeboot.service.training.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.training.entity.Training;
import lombok.Data;

import java.util.Date;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: TrainingPageDto
 * 练习表分页dto 类
 *
 * @author 宋泽山
 * @version 1.0
 * @date 2025/3/28 20:19
 */
@Data
public class TrainingPageDto extends CommonSearchDto<Training> {

    private Long id;

    /**标题 */
    private String title;

    /** 练习的难易程度*/
    private Integer difficultLevel;

    /**最后的提交时间*/
    private Date submitTime;

    /**是否私有,私有1,非私有0 */
    @TableField("private_or_not")
    private boolean privateOrNot;

    /**点赞数量 */
    private Integer likeNumber;

    /** 学生实体中使用id,img_url,name 字段*/
    /**学生学号*/
    private Long stuId;

    /**学生的头像链接*/
    private String imgUrl;

    /**学生表的name 字段*/
    private String studentName;

    /** 年级实体中使用 grade的name 字段*/
    private String gradeName;

    /**模糊查询的 起始查询时间*/
    @TableField(exist = false)
    private Date beginTime;

    /**模糊查询的 终止查询时间*/
    @TableField(exist = false)
    private Date endTime;

    /**当前用户是否对目前作品点赞*/
    @TableField("link_id")
    private Boolean isApprove;

    /**该条练习对应的学生的个人积分总和 */
    private Integer integralScore;

    /**对应user_id*/
    private Long userId;
}
