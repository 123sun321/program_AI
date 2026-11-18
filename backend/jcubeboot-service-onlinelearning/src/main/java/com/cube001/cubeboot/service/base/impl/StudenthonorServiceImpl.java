package com.cube001.cubeboot.service.base.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.base.entity.Studenthonor;
import com.cube001.cubeboot.core.util.ToolUtil;
import com.cube001.cubeboot.service.base.StudenthonorService;
import com.cube001.cubeboot.service.base.dto.StudenthonorConverter;
import com.cube001.cubeboot.service.base.dto.StudenthonorDto;
import com.cube001.cubeboot.service.base.dto.StudenthonorPageDto;
import com.cube001.cubeboot.service.base.mapper.StudenthonorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C),2018-2019, ZhengZhouChuangZhi. Co.,Ltd.
 * FileName: [StudenthonorServiceImpl]
 * StudenthonorServiceImpl是服务层接口实现类
 *
 * @author anonymous
 * @Date 2018-11-30
 * @version 1.0
 */
@Service
public class StudenthonorServiceImpl extends ServiceImpl<StudenthonorMapper, Studenthonor> implements StudenthonorService {

    @Autowired
    private StudenthonorConverter studenthonorConverter;
    @Autowired
    private StudenthonorService studenthonorService;
    /**
     * 功能：获取分页、模糊查询获取学生奖惩列表
     * @param page
     * @param formToEntityWrapperWithSearch
     * @return 分页、模糊查询获取学生奖惩列表
     */
    @Override
    public Page<Studenthonor> getStuRewardPunishList(Page page, Wrapper<Studenthonor> formToEntityWrapperWithSearch) {
        List<Studenthonor> stuRewardPunishList = this.baseMapper.getStuRewardPunishList(page, formToEntityWrapperWithSearch.eq("h.is_deleted", 0).orderBy("h.id",false));
        page.setRecords(stuRewardPunishList);
        return page;
    }

    /**
     * 功能：获取需要修改的单条数据
     * @param id 学生奖惩id
     * @return
     */
    @Override
    public Studenthonor getStuRewardPunish(Long id) {
        return this.baseMapper.getStuRewardPunish(id);
    }

    /**
     * @description 处理同时添加多个学生的奖惩信息
     * @author anonymous
     * @date 2025/12/5 9:26
     * @param studenthonorDto  提交的数据
     * @return void
    */
    @Override
    @Transactional
    public Boolean getStudenthonorEntity(StudenthonorDto studenthonorDto) {
        List<Studenthonor> list = new ArrayList<>();
        if(studenthonorDto.getIds().size()>0){
            for (Long id:studenthonorDto.getIds() ) {   //获取学生id
                Studenthonor studenthonor = studenthonorConverter.dtoToEntity(studenthonorDto);
                studenthonor.setStudentId(id);
                list.add(studenthonor);
            }
        }

        boolean status = studenthonorService.insertBatch(list);
        if (!status) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return status;
        }
        return status;
    }
    /**
     * @description 组合搜索处理
     * @author anonymous
     * @date 2025/12/5 9:34
     * @param studenthonorPageDto 搜索分页、条件
     * @return com.baomidou.mybatisplus.mapper.EntityWrapper<com.cube001.cubeboot.base.entity.Studenthonor> 组合搜索处理sql
    */
    @Override
    public EntityWrapper<Studenthonor> setWrapper(StudenthonorPageDto studenthonorPageDto) {
        EntityWrapper<Studenthonor> wrapper = new EntityWrapper<>();
        if(!ToolUtil.isEmpty(studenthonorPageDto.getDepartmentId())){
            wrapper.eq("s.department_id",studenthonorPageDto.getDepartmentId());
        }
        if(!ToolUtil.isEmpty(studenthonorPageDto.getProfessionId())){
            wrapper.eq("s.profession_id",studenthonorPageDto.getProfessionId());
        }
        if(!ToolUtil.isEmpty(studenthonorPageDto.getGrade())){
            wrapper.in("g.year",studenthonorPageDto.getGrade());
        }
        if(!ToolUtil.isEmpty(studenthonorPageDto.getStudentName())){
            wrapper.like("s.name",studenthonorPageDto.getStudentName());
        }
        if(!ToolUtil.isEmpty(studenthonorPageDto.getType())){
            wrapper.eq("rpc.type",studenthonorPageDto.getType());
        }
        if(!ToolUtil.isEmpty(studenthonorPageDto.getLevel())){
            wrapper.eq("rpc.level",studenthonorPageDto.getLevel());
        }
        return wrapper;
    }
}
