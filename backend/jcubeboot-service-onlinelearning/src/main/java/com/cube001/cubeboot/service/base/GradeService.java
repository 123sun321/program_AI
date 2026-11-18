package com.cube001.cubeboot.service.base;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cube001.cubeboot.base.entity.Grade;
import com.baomidou.mybatisplus.service.IService;
import com.cube001.cubeboot.service.base.dto.GradeDto;
import com.cube001.cubeboot.service.base.dto.ImportGradeDto;

import java.util.List;

/**
* Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
* FileName:  GradeService
* 班级业务接口
*
* @author anonymous
* @date   2018/11/28 10:36
* @version 1.0
*/
public interface GradeService extends IService<Grade> {

    /**
     * @description 通过id获取包括学院和专业
     * @author anonymous
     * @date 2025/11/28 10:06
     * @param id 学生id
     * @return   班级详情
    */
    Grade getWithProfessions(Long id);

    /**
     * @description 分页查询
     * @author anonymous
     * @date 2025/11/28 10:07
     * @param page                    分页
     * @param formToEntityWrapper     条件构造器
     * @return                        分页详情
    */
    Page<Grade> pageWithProfessions(Page page, Wrapper<Grade> formToEntityWrapper);

    /**
     * @description 检查批量导入
     * @author anonymous
     * @date 2025/11/28 10:08
     * @param list 上传Excel解析Object集合
     * @return     校验过的班级Dto集合
    */
    List<ImportGradeDto> checkSheet(List list);


    /**
     * @description 获取所有
     * @author anonymous
     * @date 2025/11/28 10:09
     * @param ids 班级id集合
     * @return    班级集合
    */
    List<Grade> getWithProfessionsAndDepartment(List<Long> ids);

    /**
     * @description 批量添加
     * @author anonymous
     * @date 2025/11/28 10:09
     * @param data 批量添加学生Dto集合
     * @return     批量添加结果
    */
    Boolean addList(List<GradeDto> data);


    List<Grade> selectListByYear(String year);
}
