package com.cube001.cubeboot.base.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.base.entity.RewardPunishCategory;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.base.entity.Studenthonor;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.domain.tenant.TenantIdUtil;
import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.core.runtime.web.AppResponse;
import com.cube001.cubeboot.core.util.ToolUtil;
import com.cube001.cubeboot.core.util.ValidateUtil;
import com.cube001.cubeboot.core.validateGroup.AddChecks;
import com.cube001.cubeboot.core.validateGroup.EditChecks;
import com.cube001.cubeboot.service.base.RewardPunishCategoryService;
import com.cube001.cubeboot.service.base.StudentService;
import com.cube001.cubeboot.service.base.StudenthonorService;
import com.cube001.cubeboot.service.base.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import javax.websocket.server.PathParam;
import java.util.List;

/**
 * Copyright (C),2018-2019, ZhengZhouChuangZhi. Co.,Ltd.
 * FileName: [SchoolAdminController]
 * 用于学生奖惩记录功能实现数据交互的控制器
 *
 * @author HuangPengju
 * @Date 2018-11-27
 * @version 1.0
 */

@Api(value = "学生奖惩controller",tags = {"学生奖惩操作接口"})
@RestController
@RequestMapping(value = "${cubeboot.app-manager-path}/studenthonor")
@RequiresPermissions("studenthonor")
public class StudenthonorController implements IApplicationService {

    @Autowired
    private StudenthonorConverter studenthonorConverter;
    @Autowired
    private StudenthonorService studenthonorService;

    @Autowired
    private StudentConverter studentConverter;
    @Autowired
    private StudentService studentService;

    @Autowired
    private RewardPunishCategoryService rewardPunishCategoryService;
    /**
     * 功能：添加奖惩数据
     * @param studenthonorDto  需要添加到数据得数据
     * @return                  添加成功返回得信息
     */
    @ApiOperation(value = "添加学生奖惩信息")
    @RequestMapping(value = "add",method = RequestMethod.POST)
    @RequiresPermissions("studenthonor:add")
    public Boolean add(@RequestBody @Validated({Default.class, AddChecks.class}) StudenthonorDto studenthonorDto){
        Boolean status = studenthonorService.getStudenthonorEntity(studenthonorDto);
        return status;
    }
    /**
     * 功能：根据id查询需要修改的实体
     * @param id   //需要修改数据得id
     * @return     //需要修改的实体
     */
    @ApiOperation(value = "获取需要修改的奖惩信息")
    @RequestMapping(value = "get", method = RequestMethod.GET)
    @RequiresPermissions("studenthonor:get")
    public Studenthonor get(@RequestParam("id") Long id) {
        Studenthonor studenthonor = studenthonorService.getStuRewardPunish(id);
        return studenthonor;
    }
    /**
     * 功能：修改学生奖惩类型信息
     * @param studenthonorEditDto  需要修改得信息
     * @return                            修改成功对象
     */
    @ApiOperation(value = "编辑奖惩类型信息")
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @RequiresPermissions("studenthonor:edit")
    public void edit(@RequestBody @Validated({Default.class, EditChecks.class}) StudenthonorEditDto studenthonorEditDto){
        Studenthonor studenthonor = studenthonorConverter.editDtoToEntity(studenthonorEditDto);
        studenthonorService.updateById(studenthonor);
    }
    /**
     * 功能：查询学生奖惩信息列表（可能附加name搜索、类型、级别组合搜索）
     * @param  studenthonorPageDto 查询条件
     * @return 分页后的数据列表
     */
    @ApiOperation(value = "分页获取学生奖惩信息列表")
    @RequestMapping(value = "page",method = RequestMethod.POST)
    @RequiresPermissions("studenthonor:page")
    public Page<Studenthonor> page(@RequestBody StudenthonorPageDto studenthonorPageDto){
        Page<Studenthonor> page = studenthonorPageDto.getPlusPage();
        EntityWrapper<Studenthonor> wrapper = studenthonorService.setWrapper(studenthonorPageDto);
        page = studenthonorService.getStuRewardPunishList(page, wrapper);
        return page;
    }
    /**
     * 功能：根据id删除需要删除得数据
     * @param id  需要删除得数据id
     * @return true/fase
     */
    @ApiOperation(value = "根据id删除学生奖惩信息",notes = "注意参数id")
    @RequestMapping(value = "/del/{id}", method = RequestMethod.GET)
    @RequiresPermissions("studenthonor:del")
    public Boolean delete(@PathVariable("id") Long id){
        boolean status = studenthonorService.deleteById(id);
        return status;
    }
    /**
     * 功能：根据ids数组批量删除数据
     * @param ids 需要删除得数据全部id
     * @return true/false
     */
    @ApiOperation(value = "根据id数组批量删除学生奖惩信息",notes = "注意参数id")
    @RequestMapping(value = "/deleteBatch",method = RequestMethod.POST)
    @RequiresPermissions("studenthonor:del")
    public Boolean deleteBatch(@RequestBody List<Long> ids){
        boolean status = studenthonorService.deleteBatchIds(ids);
        return status;
    }

    /**
     * 功能：根据学号、姓名、证件号码 查询学生
     * @param studentDto  查询条件
     * @return 学生列表
     */
    @ApiOperation(value = "根据学生姓名、学号、身份证号查询学生")
    @RequestMapping(value = "/getStudent",method = RequestMethod.POST)
    @RequiresPermissions("studenthonor:list")
    public List<Student> getAll(@RequestBody StudentDto studentDto){
        Student student = studentConverter.dtoToStudent(studentDto);
        List<Student> studentList = studentService.getStudent(student.getName(), student.getCode(), student.getIdnumber());
        return studentList;
    }

    /**
     * 功能：获取所有的奖惩种类
     * @return 所有的奖惩信息
     */
    @ApiOperation(value = "获取所有的奖惩种类")
    @RequestMapping(value = "/getRewardPunish",method = RequestMethod.GET)
    @RequiresPermissions("studenthonor:list")
//    @Cacheable(value = "redis-rewardPunishCategoryList",key = "aaa")
    public List<RewardPunishCategory> getAll(){
        List<RewardPunishCategory> rewardPunish = rewardPunishCategoryService.getRewardPunish();
        return rewardPunish;
    }
}
