package com.cube001.cubeboot.service.assignment;

import com.cube001.cubeboot.assignment.entity.AssignmentResource;
import com.baomidou.mybatisplus.service.IService;
import com.cube001.cubeboot.service.assignment.dto.AssignmentResourceDto;
import com.cube001.cubeboot.service.assignment.dto.ListAssignmentResourceDto;

/*
* Copyright (C), 2018 - 2019, ZhengZho uChuangZhi. Co., Ltd
* FileName:
* 类的详细说明
*
* @author 张人杰
* @date   2019/4/24 10:35
* @version 1.0
*/


public interface AssignmentResourceService extends IService<AssignmentResource> {
   /*
     * @description 作业资源的保存
     * @author 张人杰
     * @date 2025/4/24 17:08
     * @Param
     * @return
     */

   void bacthinsert(ListAssignmentResourceDto listAssignmentResourceDto);
}
