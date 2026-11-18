package com.cube001.cubeboot.service.base.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.base.entity.Teachers;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TeacherMapper extends BaseMapper<Teachers> {

    List<Teachers> searchPage(Page page,@Param("ew") Wrapper<Teachers> wrapper);
    List<Teachers> getAll();
    Teachers selectWithUser(Long userId);

}
