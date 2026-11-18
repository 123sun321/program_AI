package com.cube001.cubeboot.service.base.mapper;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cube001.cubeboot.base.entity.Grade;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
* FileName: GradeMapper
* 班级dao接口
*
* @author anonymous
* @date   2018/11/28 10:37
* @version 1.0
*/
public interface GradeMapper extends BaseMapper<Grade> {

    /**
     * @description 根据id查询包括专业和学院
     * @author anonymous
     * @date 2025/11/28 10:37
     * @param id    学生id
     * @return      班级
    */
    Grade getWithProfessions(Long id);

    /**
     * @description 分页查询
     * @author anonymous
     * @date 2025/11/28 10:37
     * @param page      分页
     * @param wrapper   条件构造器
     * @return          班级集合
     */
    List<Grade> pageWithProfessions(Page<Grade> page, @Param("ew") Wrapper<Grade> wrapper);

    /**
     * @description 查询所有
     * @author anonymous
     * @date 2025/11/28 10:37
     * @param wrapper   条件构造器
     * @return          班级集合
     */
    List<Grade> getWithProfessionsAndDepartment(@Param("ew")Wrapper<Grade> wrapper);

    /**
     * @description  通过年级查询所有班级
     * @author anonymous
     * @date 2025/3/15 10:45
     * @param
     * @return
     */
    List<Grade> selectListByYear(String year);
}
