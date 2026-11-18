package com.cube001.cubeboot.service.base.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.base.entity.Grade;
import com.cube001.cubeboot.base.entity.Profession;
import com.cube001.cubeboot.service.base.GradeService;
import com.cube001.cubeboot.service.base.ProfessionsService;
import com.cube001.cubeboot.service.base.dto.BaseConverter;
import com.cube001.cubeboot.service.base.dto.GradeDto;
import com.cube001.cubeboot.service.base.dto.ImportGradeDto;
import com.cube001.cubeboot.service.base.mapper.GradeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
* FileName: GradeServiceImpl.java
* 班级业务实现
*
* @author 郭广明
* @date   2018/11/28 8:55
* @version 1.0
*/
@Service
public class GradeServiceImpl extends ServiceImpl<GradeMapper, Grade> implements GradeService {

    @Autowired
    private Validator defaultValidator;

    @Autowired
    private GradeMapper gradeMapper;
    @Autowired
    private ProfessionsService professionsService;
    @Autowired
    private BaseConverter baseConverter;

    /**
     * @description 通过id查询包括
     * @author 郭广明
     * @date 2025/11/28 8:55
     * @param id     班级id
     * @return       班级
    */
    @Override
    public Grade getWithProfessions(Long id) {
        return gradeMapper.getWithProfessions(id);
    }

    /**
     * @description 分页查询
     * @author 郭广明
     * @date 2025/11/28 10:02
     * @param page      分页
     * @param wrapper   条件构造器
     * @return          班级分页详情
    */
    @Override
    public Page<Grade> pageWithProfessions(Page page, Wrapper<Grade> wrapper) {
        List<Grade> gradeList =gradeMapper.pageWithProfessions(page,wrapper.eq("a.is_deleted","0").orderBy("a.creation_time",false));
        page.setRecords(gradeList);
        return page;
    }

    /**
     * @description 封装、校验导入所有实体
     * @author 郭广明
     * @date 2025/11/28 10:03
     * @param list   Excel解析后的Object集合
     * @return       校验过的班级Dto集合
    */
    @Override
    public List<ImportGradeDto> checkSheet(List list) {
        List<ImportGradeDto> importGradeDtoList = new ArrayList<>();
        List<Profession> professionList = professionsService.listWithProfessionsAndGrades();
        for(int i=0; i<list.size(); i++) {
            ImportGradeDto importGradeDto = new ImportGradeDto((List) list.get(i));
            importGradeDto.setSuccess(true);
            importGradeDto = checkImportGradeDto(importGradeDto,importGradeDtoList,professionList);
            importGradeDtoList.add(importGradeDto);
        }
        return importGradeDtoList;
    }

    /**
     * @description 校验每个ImportGradeDto是否合法
     * @author 郭广明
     * @date 2025/11/28 10:05
     * @param importGradeDto                  未校验的班级Dto
     * @param importGradeDtoList              已校验过的班级Dto集合
     * @param withProfessionsAndGrades    院系集合
     * @return                                校验过的班级Dto
     */
    private ImportGradeDto checkImportGradeDto(ImportGradeDto importGradeDto, List<ImportGradeDto> importGradeDtoList, List<Profession> withProfessionsAndGrades) {

        StringBuffer tips = new StringBuffer();

        for(ImportGradeDto importGradeDto1:importGradeDtoList) {
            if(importGradeDto1.getName().equals(importGradeDto.getName())) {
                importGradeDto.setSuccess(false);
                tips.append("班级名称"+importGradeDto.getName()+"在上面已出现过；");
                break;
            }
        }
        /*控制专业是否存在字段*/
        boolean pros = false;

                for(Profession pro:withProfessionsAndGrades) {
                    if(pro.getName().equals(importGradeDto.getProfessionName())) {
                        Profession profession = new Profession();
                        importGradeDto.setProfessionId(pro.getId());
                        profession.setId(pro.getId());
                        importGradeDto.setProfession(baseConverter.professionToDto(profession));
                        pros = true;
                        for(Grade grade:pro.getGradeList()) {
                            if(grade.getName().equals(importGradeDto.getName()) && grade.getYear().equals(importGradeDto.getYear())) {
                                importGradeDto.setSuccess(false);
                                tips.append("班级名称"+importGradeDto.getName()+"已存在；");
                                break;
                            }
                        }
                        break;
                    }
                }
                if(!pros) {
                    importGradeDto.setSuccess(false);
                    tips.append("专业名称"+importGradeDto.getProfessionName()+"不存在；");
                }

        if(null == importGradeDto.getProfession()) {
            importGradeDto.setProfession(baseConverter.professionToDto(new Profession()));
        }

        Set<ConstraintViolation<ImportGradeDto>> violationSet = defaultValidator.validate(importGradeDto); //验证
        if (violationSet.size() > 0) {
            importGradeDto.setSuccess(false);
            String err = String.join(";", violationSet.stream().map(b -> {
                return b.getMessage();
            }).collect(Collectors.joining()));
            tips.append("字段验证失败:" + err);
        }

        importGradeDto.setTips(tips.toString());
        return importGradeDto;
    }



    /**
     * @description 获取所有
     * @author 郭广明
     * @date 2025/11/28 10:05
     * @param ids    班级id集合
     * @return       班级集合
    */
    @Override
    public List<Grade> getWithProfessionsAndDepartment(List<Long> ids) {
        EntityWrapper<Grade> wrapper= new EntityWrapper<>();
        wrapper.in("a.id",ids);
        wrapper.eq("a.is_deleted",0);
        return (gradeMapper.getWithProfessionsAndDepartment(wrapper));
    }

    /**
     * @description 批量导入
     * @author 郭广明
     * @date 2025/11/28 10:05
     * @param data   班级dto集合
     * @return       批量添加结果
    */
    @Override
    public Boolean addList(List<GradeDto> data) {
        List<Grade> gradeList = baseConverter.gradeDtoList(data);
        Boolean res = this.insertBatch(gradeList);
        return res;
    }

    /**
    * @description  通过年级查询所有班级
    * @author 马勇超
    * @date 2025/3/15 10:45
    * @param
    * @return
    */
    @Override
    public List<Grade> selectListByYear(String year) {
        return baseMapper.selectListByYear(year);
    }
}
