package com.cube001.cubeboot.service.base;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.base.entity.Profession;
import com.baomidou.mybatisplus.service.IService;
import com.cube001.cubeboot.service.base.dto.ImportProfessionDto;
import com.cube001.cubeboot.service.base.impl.dto.InsertBatchProfessionDto;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 马勇超
 * @since 2018-11-16
 */
public interface ProfessionsService extends IService<Profession> {

    /**
    * @description  获取专业id和name
    * @author 马勇超
    * @date 2025/12/4 14:27
    * @return   list数据
    */
    List<Profession> getIdAndName();

    /**
    * @description  删除判断专业下面是否存在班级
    * @author 马勇超
    * @date 2025/12/4 14:34
    * @param  ID
    * @return   Boolean
    */
    Boolean deleteWithGradeById(Long id);

    /**
    * @description  解析Excel并验证Excel是否存在错误字段
    * @author 马勇超
    * @date 2025/12/4 14:35
    * @param   MapList:Excel数据
    * @return   List 验证结果
    */
    List<ImportProfessionDto> importDataToDo(List<Map<String, Object>> mapList);

    /**
    * @description  专业的列表分页
    * @author 马勇超
    * @date 2025/3/8 8:50
    * @param
    * @return
    */
    Page<Profession> searchPage(Page<Profession> plusPage, Wrapper<Profession> formToEntityWrapper);

    /**
    * @description  专业列表包含班级
    * @author 马勇超
    * @date 2025/3/8 15:14
    * @param
    * @return
    */
    List<Profession> listWithProfessionsAndGrades();

}
