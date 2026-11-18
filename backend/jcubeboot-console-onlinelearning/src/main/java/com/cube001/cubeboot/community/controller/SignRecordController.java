package com.cube001.cubeboot.community.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.community.entity.SignRecord;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.service.base.StudentService;
import com.cube001.cubeboot.service.community.SignRecordService;
import com.cube001.cubeboot.service.community.dto.SignRecordConverter;
import com.cube001.cubeboot.service.community.dto.SignRecordDto;
import com.cube001.cubeboot.service.community.dto.SignStudentGradeRecord;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
* @description  签到记录控制类
* @author 马勇超
* @date 2025/3/26 9:16
* @param
* @return
*/
@Api(value = "签到记录",description = "签到记录")
@RestController
@RequestMapping(value = "${cubeboot.app-manager-path}/SignRecord")
@RequiresPermissions("signRecord")
public class SignRecordController  implements IApplicationService {

    @Autowired
    private SignRecordService service;
    @Autowired
    private StudentService studentService;

    @Autowired
    private SignRecordConverter signRecordConverter;
    /**
     * @description 分页展示签到记录搜索
     * @author qw
     * @date 2025/3/21 11:14
     * @param searchDto
     * @return com.baomidou.mybatisplus.plugins.Page<com.cube001.cubeboot.service.course.dto.JoinCoursePathDto>
    */
    @ApiOperation(value = "搜索",notes = "搜索")
    @PostMapping("/findpage")
    @RequiresPermissions("signRecord:page")
    public Page<SignRecordDto> findPage(@RequestBody CommonSearchDto<SignRecordDto> searchDto){
        HashMap<String,String> map = new HashMap<>();
        map.put("year","tg.");
        map.put("grade_id","t.");
        Wrapper<SignRecordDto> signRecordDtoWrapper = searchDto.formToEntityWrapperWithSearch(new String[]{"code","name"},"ts.",map);
        Page<SignRecordDto> page = service.page(searchDto.getPlusPage(),signRecordDtoWrapper);
        return signRecordConverter.pageEntityToDto(page);
    }

    /**
    * @description  增加签到记录
    * @author 马勇超
    * @date 2025/4/25 10:59
    * @param
    * @return
    */
    @ApiOperation(value = "增加签到记录")
    @PostMapping(value = "add")
    @RequiresPermissions("signRecord:add")
    public void add(){
        Long userId = ShiroKit.getUserId();
        Student student = studentService.getStudentByUserId(userId);
        SignRecord signRecord = new SignRecord();
        signRecord.setSignTime(new Date());
        signRecord.setGradeId(student.getGradeId());
        signRecord.setGradeName(student.getGrade().getName());
        signRecord.setStudentId(student.getId());
        signRecord.setStudentName(student.getName());
        signRecord.setUserId(userId);
        if(!service.insert(signRecord)){
            throw new FriendException("签到失败！");
        }
    }

    /**
    * @description  判断该用户今天是否签到
    * @author 马勇超
    * @date 2025/4/25 11:08
    * @return
    */
    @ApiOperation(value = "判断该用户今天是否签到")
    @GetMapping(value = "getIsSign")
    @RequiresPermissions("signRecord:get")
    public Boolean isSignRecord(){
        Long userId = ShiroKit.getUserId();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String signTime = df.format(date);
        SignRecord signRecord = service.selectOne(new EntityWrapper<SignRecord>().eq("user_id",userId).eq("sign_time",signTime));
        if (null != signRecord){
            return true;
        }else{
            return false;
        }
    }

    /**
    * @description  签到记录删除
    * @author 马勇超
    * @date 2025/3/25 11:19
    * @param   id
    */
    @ApiOperation(value = "删除",notes = "删除")
    @GetMapping("/delete")
    @RequiresPermissions("signRecord:del")
    public void del(@RequestParam("id") Long id){
        service.deleteById(id);
    }

    @ApiOperation(value = "累计签到学生")
    @GetMapping(value = "cumulativeSign")
    @RequiresPermissions("signRecord:get")
    public SignStudentGradeRecord cumulative(@RequestParam("starTime") String startTime, @RequestParam("endTime") String endTime){

        SignStudentGradeRecord studentGradeRecord = new SignStudentGradeRecord();

        List<SignRecordDto> studentSignRecordDtoList = service.selectCountByStudent(startTime,endTime);
        List<SignRecordDto> gradeSignRecordDtoList = service.selectCountByGrade(startTime,endTime);
        List<SignRecordDto> daySignRecordDtoList = service.selectCountByEveryDay(startTime,endTime);

        studentGradeRecord.setStudent(studentSignRecordDtoList);
        studentGradeRecord.setGrade(gradeSignRecordDtoList);
        studentGradeRecord.setEveryDay(daySignRecordDtoList);

        return studentGradeRecord;
    }

}
