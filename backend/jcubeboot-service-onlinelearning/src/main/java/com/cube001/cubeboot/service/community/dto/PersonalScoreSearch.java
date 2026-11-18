package com.cube001.cubeboot.service.community.dto;

import com.cube001.cubeboot.core.dto.PageDto;
import com.cube001.cubeboot.core.page.PageBasePara;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/*
 *功能描述     传参
 * @author     anonymous
 * @date       2019/3/23
 * @param
 * @return
*/
@Data
public class PersonalScoreSearch extends PageBasePara {

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
