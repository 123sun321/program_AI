package com.cube001.cubeboot.service.base;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.cube001.cubeboot.base.entity.Label;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Copyright (C), 2025 - 2026, zxy.
* FileName: LabelService.java
* 类的详细说明
*
* @author 马勇超
* @date 2025/3/8 17:55
* @version 1.0
*/
public interface LabelService extends IService<Label> {

    Page<Label> pageWith(Page<Label> plusPage, Wrapper<Label> wrapper);

    List<Label> selectListWithKeyWord(@Param("str") String str);

    List<Label> selectListByName(String name);

    Label getByName(String name);
    /**
     * @description 根据课程ID来获取课程对应的所有标签列表
     * @author 宋泽山
     * @date 2025/4/22 14:52
     * @param courseId 课程id
     * @return List<Label>
     */
    List<Label> selectListByCourseId(Long courseId);
    /**
     * @description 根据选择的技术类型来获取课程对应的所有标签列表
     * @author 宋泽山
     * @date 2025/4/22 14:52
     * @param techType 技术类型
     * @return List<Label>
     */
    List<Label> selectListByTechType(String techType);
    /**
     * @description  学生端案例库标签缓存功能
     * @author 马勇超
     * @date 2025/5/6 9:22
     * @return
     */
    List<Label> selectLabelList(String category);
}
