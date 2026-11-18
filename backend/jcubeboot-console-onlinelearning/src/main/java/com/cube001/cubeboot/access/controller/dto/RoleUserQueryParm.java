package com.cube001.cubeboot.access.controller.dto;

import com.cube001.cubeboot.core.page.PageBasePara;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author:anonymous
 * @Description:
 * @Date:Create in 18:07 2018/07/09
 odified By:
 */
@Data
public class RoleUserQueryParm extends PageBasePara {
    /**
     * 角色id
     * */
    private Integer roleId;
    //private UserTypeEnum userTypeEnum;
    /**
     * 用户类型
     * */
    private Integer userType;
    /**
     * 查询关键字
     * */
    private String keyword;
    /**
     * 开始日期
     * */
    @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date startDate;
    /**
     * 结束日期
     * */
    @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date endDate;
}
