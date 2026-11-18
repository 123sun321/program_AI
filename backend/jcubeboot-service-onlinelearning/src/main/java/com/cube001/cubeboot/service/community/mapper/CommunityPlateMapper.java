package com.cube001.cubeboot.service.community.mapper;

import com.cube001.cubeboot.community.entity.CommunityPlate;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/***
* Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
* FileName: CommunityPlateMapper
* 社区板块mapper
*
* @author 张浩杰
* @date   2019/3/19 16:19
* @version 1.0
*/
public interface CommunityPlateMapper extends BaseMapper<CommunityPlate> {
    /**
     * @description 根据排序编号查询社区板块
     * @author 张浩杰
     * @date 2025年3月20日 14:30:34
     * @param sort
     * @return CommunityPlate
    */
    CommunityPlate getCommunityPlateBySort(@Param("sort") Integer sort);

}
