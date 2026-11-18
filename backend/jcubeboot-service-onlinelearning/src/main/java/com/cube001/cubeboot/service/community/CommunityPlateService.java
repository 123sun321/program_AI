package com.cube001.cubeboot.service.community;

import com.cube001.cubeboot.community.entity.CommunityPlate;
import com.baomidou.mybatisplus.service.IService;
import com.cube001.cubeboot.service.community.dto.CommunityPlateDto;

import java.util.List;
import java.util.Map;

/*** 
* Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd. 
* FileName: CommunityPlateService
* 社区板块service
* 
* @author anonymous 
* @date   2019/3/19 16:17
* @version 1.0 
*/ 
public interface CommunityPlateService extends IService<CommunityPlate> {
    /**
     * @description 删除/批量删除社区板块
     * @author anonymous
     * @date  2019年3月19日 17:36:08
     * @param  ids
     * @return  result
     */
    void delete(List<Long> ids);
    /**
     * @description 编辑修改社区板块
     * @author anonymous
     * @date 2025年3月19日 17:49:57
     * @param communityPlateDto
     * @return Boolean
    */
    void edit(CommunityPlateDto communityPlateDto);
    /**
     * @description 添加社区板块
     * @author anonymous
     * @date  2019年3月20日 15:04:58
     * @param  communityPlateDto
     * @return  Boolean
    */
    void add(CommunityPlateDto communityPlateDto);
    /**
     * @description 通过角色获取权限社区板块
     * @author anonymous
     * @date 2025年3月22日 13:45:31
     * @param
     * @return communityPlateList
     */
    Map<Object,String> getPlateByRole(Long userIds);
    /**
     * @description 验证排序是否重复
     * @author anonymous
     * @date 2025/3/22 16:21
     * @param  col,value
     * @return
     */
    Integer getCommunityPlateSort(String col,String value);
}
