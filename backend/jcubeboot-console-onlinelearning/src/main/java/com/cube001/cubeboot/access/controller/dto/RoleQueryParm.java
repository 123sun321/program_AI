package com.cube001.cubeboot.access.controller.dto;

import com.cube001.cubeboot.core.page.PageBasePara;
import lombok.Data;

/**
 * @Author:anonymous
 * @Description:
 * @Date:Create in 16:21 2018/07/13
 * @Modified By:
 */
@Data
public class RoleQueryParm extends PageBasePara {
    /**
     * 查询关键字
     * */
    private String keyword;
}
