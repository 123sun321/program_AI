package com.cube001.cubeboot.service.community.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/*
 *功能描述     个人积分dto
 * @author     anonymous
 * @date       2019/3/23
 * @param
 * @return
*/
@Data
public class IntegralScoreDto {

    /**
     * 积分类型
     */
    @NotBlank
    private String name;
    /**
     * 发放积分数
     */
    @NotBlank
    private Integer num;
    /**
     * 发放日期
     */
    @NotBlank
    @TableField("issue_time")
    private Date issueTime;
    /**
     * 到期日期
     */
    @NotBlank
    @TableField("end_time")
    private Date endTime;
    /**
     * 备注
     */
    @NotBlank
    private String remark;

}
