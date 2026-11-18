package com.cube001.cubeboot.service.base.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.base.entity.Grade;
import com.cube001.cubeboot.base.entity.Profession;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;;
import com.cube001.cubeboot.service.base.GradeService;
import com.cube001.cubeboot.service.base.ProfessionsService;
import com.cube001.cubeboot.service.base.dto.ImportProfessionDto;
import com.cube001.cubeboot.service.base.dto.ProfessionDto;
import com.cube001.cubeboot.service.base.mapper.ProfessionsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/*
* Copyright (C), 2025 - 2026, zxy.
* FileName: ProfessionsServiceImpl.java
* 类的详细说明 用来实现Controller需要的接口方法
*
* @author anonymous
* @date 2025/11/27 17:03
* @version 1.0
*/
@Service
public class ProfessionsServiceImpl extends ServiceImpl<ProfessionsMapper, Profession> implements ProfessionsService {

    @Autowired
    ProfessionsService professionsService;

    @Autowired
    GradeService gradeService;

    @Autowired
    private Validator defaultValidator;

    /*
     * @description  查询所有专业内容的id与name
     * @author anonymous
     * @date 2025/11/27 15:52
     * @param 参数无
     * @return 返回一个只包含所有专业的id与name的数组
     */
    @Override
    public List<Profession> getIdAndName() {
        return this.baseMapper.getIdAndName();
    }

    /**
     * @description  专业分页
     * @author anonymous
     * @date 2025/3/8 8:51
     * @param
     * @return
     */
    @Override
    public Page<Profession> searchPage(Page<Profession> plusPage, Wrapper<Profession> formToEntityWrapper) {
        List<Profession> professions = baseMapper.searchPage(plusPage,formToEntityWrapper.eq("a.is_deleted",0).orderBy("a.creation_time",false));
        plusPage.setRecords(professions);
        return plusPage;
    }

    @Override
    public List<Profession> listWithProfessionsAndGrades() {
        List<Profession> professionList = baseMapper.selectList(new EntityWrapper<>());
        for (Profession p:professionList) {
            List<Grade> gradeList = gradeService.selectList(new EntityWrapper<Grade>().eq("profession_id",p.getId()));
            p.setGradeList(gradeList);
        }
        return professionList;
    }


    /**
     * @description  删除判断专业下面是否存在班级
     * @author anonymous
     * @date 2025/12/4 14:34
     * @param  id
     * @return   Boolean
     */
    @Override
    public Boolean deleteWithGradeById(Long id) {
        Boolean success = false;
        List<Grade> gradeList = gradeService.selectList(new EntityWrapper<Grade>().eq("profession_id",id).eq("is_deleted",0));
        if(gradeList.size() == 0 || gradeList ==null){
            success = professionsService.deleteById(id);
        }
        return success;
    }

    /**
     * @description  解析Excel并验证Excel是否存在错误字段
     * @author anonymous
     * @date 2025/12/4 14:35
     * @param   importData:Excel数据
     * @return   List
     */
    @Override
    public List<ImportProfessionDto> importDataToDo(List<Map<String, Object>> importData) {
        List<ImportProfessionDto> professionDtos = new ArrayList<>();
        Field[] fields = ProfessionDto.class.getDeclaredFields(); //反射获取专业类中的所有字段

        //查询数据库的数据
        Wrapper<Profession> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("name","code").eq("is_deleted",false);
        List<Profession> professions = baseMapper.selectList(wrapper); // 数据库的数据

        for(Map<String, Object>  data : importData){
            ImportProfessionDto professionDto = new ImportProfessionDto();
            professionDto.setSuccess(true);
            List<String> tips = new ArrayList<>();
            //错误字段
            List<String> errorField = createByReflect(professionDto,fields,data);
            //验证返回列中是否存在
            //数据库重复检验
            if(professionDto.getName() != null){
                Boolean exit = professionDtos.stream().filter(b -> professionDto.getName().equals(b.getName())).count() > 0;
                if(exit) {continue;} //存在就直接跳出
                   exit = professions.stream().filter(b -> professionDto.getName().equals(b.getName())).count() > 0;
                    if (exit) {
                        professionDto.setSuccess(false);
                        tips.add(professionDto.getName() + "专业已存在");
                    }
            }
            if(professionDto.getCode() != null){
                Boolean exit = professionDtos.stream().filter(b -> professionDto.getCode().equals(b.getCode())).count() > 0;
                if(exit) {continue;} //存在直接跳出
                exit = professions.stream().filter(b -> professionDto.getCode().equals(b.getCode())).count() >0;
                if(exit){
                    professionDto.setSuccess(false);
                    tips.add("专业代码" + professionDto.getCode() + "已存在");
                }
            }

            //字段转换失败
            if(errorField.size()>0){
                tips.add("字段转换失败：" + String.join(";",errorField));
                professionDto.setSuccess(false);
            }

            //验证
            Set<ConstraintViolation<ImportProfessionDto>> violations = defaultValidator.validate(professionDto);//验证
            if(violations.size()>0){
                professionDto.setSuccess(false);
                String err = String.join(";",violations.stream().map(b ->{
                    return b.getMessage();
                }).collect(Collectors.joining()));
                tips.add("字段验证失败："+err);
            }

            professionDto.setTips(String.join(".",tips));
            professionDtos.add(professionDto);

        }
        return professionDtos;
    }


    /**
     * 创建通过反射
     * @param professionDto
     * @param fields
     * @param data
     * @return List: 错误信息数据
     */
    private List<String> createByReflect(ImportProfessionDto professionDto, Field[] fields, Map<String, Object> data) {
        List<String> errfiledList = new ArrayList<>();
        for (Field field : fields) {
            String key = field.getName();
            Object val = data.get(key);
            if (val == null) continue;
            try {
                //取消访问权限控制
                field.setAccessible(true);
                if (field.getType().equals(Integer.class)) {
                    field.set(professionDto, new Integer(data.get(key).toString()));
                } else {
                    field.set(professionDto, data.get(key));
                }
            } catch (IllegalAccessException e) {
                errfiledList.add(e.toString() + "填入失败");
            }
        }
        return errfiledList;
    }

}
