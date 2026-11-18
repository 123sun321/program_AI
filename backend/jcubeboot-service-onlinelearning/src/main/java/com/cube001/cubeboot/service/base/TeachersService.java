package com.cube001.cubeboot.service.base;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.base.entity.Teachers;
import com.cube001.cubeboot.service.base.dto.ImportTeacherDto;
import com.cube001.cubeboot.service.base.dto.TeachersDto;
import com.cube001.cubeboot.service.base.dto.base.DashboardCount;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TeachersService extends IService<Teachers> {

    /**  
    * @description  分页/搜索
    * @author 马勇超
    * @date 2025/12/4 14:16  
    * @param   page：当前页面；size：页面显示数目； name搜索的字段
     *  @param   formToEntityWrapperWithSearch：当前页面；size：页面显示数目； name搜索的字段
    * @return   分页后的数据
    */
    Page<Teachers> searchPage(Page page, Wrapper<Teachers> formToEntityWrapperWithSearch);

    /**  
    * @description  解析Excel
    * @author 马勇超
    * @date 2025/12/4 14:19  
    * @param  mapList
    * @return   List数据
    */
    List<ImportTeacherDto> importDataToDo(List<Map<String, Object>> mapList);

    /**
    * @description  新建教师的实现方法，同时创建新的user用户
    * @author 马勇超
    * @date 2025/12/4 14:21
    * @param   teachers
    * @return   Boolean
    */
    Boolean insertWithUser(Teachers teachers);


    List<Teachers> getAll();

    TeachersDto selectWithDepartment(Long userId);

    Teachers getByUserId(Long userId);


    /**
    * @description  教师首页统计
    * @author 马勇超
    * @date 2025/4/8 16:49
    * @param   id
    * @return   dashboardCount
    */
    DashboardCount getDashboardCount(Long id);

    /**
    * @description  学校管理端首页统计
    * @author 马勇超
    * @date 2025/5/14 8:40
    * @param
    * @return
    */
    DashboardCount getSchoolDashboardCount(Long userId);
}
