package com.cube001.cubeboot.service.course;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.cube001.cubeboot.course.entity.ExerciseJoinReport;
import com.cube001.cubeboot.service.course.dto.ExerciseJoinReportDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: ExerciseJoinReportService
 * 实验参与报告Service
 *
 * @author Juci
 * @version 1.0
 * @date 2025/3/28 18:02
 */
public interface ExerciseJoinReportService extends IService<ExerciseJoinReport> {
    /**
     * 创建实验报告
     * @author Juci
     * @param exerciseJoinReport
     */
    void create(ExerciseJoinReport exerciseJoinReport);

    /**
     * 编辑
     * @author Juci
     * @param userId
     * @param exerciseJoinReportDto
     */
    void edit(Long userId,ExerciseJoinReportDto exerciseJoinReportDto);

    /**
     * 分页获取报告
     * @author Juci
     * @param plusPage
     * @param wrapper
     * @return
     */
    Page<ExerciseJoinReportDto> searchPage(Page<ExerciseJoinReportDto> plusPage, Wrapper<ExerciseJoinReportDto> wrapper);

    /**
     * 查询一条记录
     * @param ew
     * @return
     */
    ExerciseJoinReportDto selectReport(Wrapper<ExerciseJoinReportDto> ew);

    /**
     * 更新浏览量
     * @author Juci
     * @param id
     */
    void updateClick(Long id);

    /**
     * 相关报告
     * @author Juci
     * @param exerciseId 实验编号
     * @return
     */
    List<ExerciseJoinReportDto> correlationReport(Long exerciseId);

    /**
     * 通过实验编号获取当前用户的实验报告
     * @author Juci
     * @param userId
     * @param id
     * @return
     */
    ExerciseJoinReport getByExerciseId(Long userId,Long id);

}
