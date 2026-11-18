package com.cube001.cubeboot.service.community;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.cube001.cubeboot.community.entity.SignRecord;
import com.cube001.cubeboot.service.community.dto.SignRecordDto;

import java.time.LocalDateTime;
import java.util.List;

/**
* @description  签到实现类
* @author anonymous
* @date 2025/3/26 9:14
*/
public interface SignRecordService extends IService<SignRecord> {

    /**
     * @description分页展示带查询
     * @author anonymous
     * @date 2025/3/21 13:41
     * @param page,formToEntityWrapperWithSearch
     * @return com.baomidou.mybatisplus.plugins.Page<com.cube001.cubeboot.service.community.dto.SignRecordDto>
    */
    Page<SignRecordDto> page(Page<SignRecordDto> page, Wrapper<SignRecordDto> formToEntityWrapperWithSearch);


    /**
    * @description  根据时间范围查询签到学生
    * @author anonymous
    * @date 2025/3/25 15:03
    * @param   startTime,endTime
    * @return   List<SignRecordDto>
    */
    List<SignRecordDto> selectCountByStudent(String startTime,String endTime);

    /**
    * @description  根据时间范围查询签到班级
    * @author anonymous
    * @date 2025/3/25 15:03
    * @param   startTime,endTime
    * @return   List<SignRecordDto>
    */
    List<SignRecordDto> selectCountByGrade(String startTime,String endTime);

    /**
    * @description  根据时间范围查询每日签到
    * @author anonymous
    * @date 2025/3/25 15:03
    * @param   startTime,endTime
    * @return   List<SignRecordDto>
    */
    List<SignRecordDto> selectCountByEveryDay(String startTime,String endTime);
}
