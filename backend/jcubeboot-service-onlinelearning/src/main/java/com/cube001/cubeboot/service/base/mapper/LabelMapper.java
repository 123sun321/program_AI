package com.cube001.cubeboot.service.base.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.base.entity.Label;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Copyright (C), 2025 - 2026, zxy.
* FileName: LabelMapper.java
* 类的详细说明
*
* @author anonymous
* @date 2025/3/8 17:55
* @version 1.0
*/
public interface LabelMapper extends BaseMapper<Label> {

    List<Label> pageWith(Page<Label> plusPage,@Param("ew") Wrapper<Label> wrapper);

    /**
    * @description  模糊查询
    * @author anonymous
    * @date 2025/3/13 13:36
    * @param   str
    * @return
    */
    List<Label> selectListWithKeyWord(@Param("str") String str);

    /**
    * @description  精确查询
    * @author anonymous
    * @date 2025/3/13 13:36
    * @param   name
    * @return
    */
    List<Label> selectListByName(@Param("name") String name);
    /**
     * @description 根据课程ID来获取课程对应的所有标签列表
     * @author anonymous
     * @date 2025/4/22 14:52
     * @param courseId 课程id
     * @return List<Label>
     */
    List<Label> selectListByCourseId(@Param("courseId")Long courseId);
    /**
     * @description 没有课程ID,默认获取所有课程对应的所有标签列表
     * @author anonymous
     * @date 2025/4/22 14:52
     * @param
     * @return List<Label>
     */
    List<Label> selectListWithoutCourseId();
    /**
     * @description 根据选择的技术类型来获取课程对应的所有标签列表
     * @author anonymous
     * @date 2025/4/22 14:52
     * @param techType 技术类型
     * @return List<Label>
     */
     List<Label> selectListByTechType(@Param("techType") String techType);
    /**
     * @description  学生端案例库标签缓存功能
     * @author anonymous
     * @date 2025/5/6 9:22
     * @return
     */
    List<Label> selectLabelList(@Param("category") String category);
}
