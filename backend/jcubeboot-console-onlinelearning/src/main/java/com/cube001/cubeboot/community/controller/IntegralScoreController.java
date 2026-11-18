package com.cube001.cubeboot.community.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.community.entity.IntegralScore;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.service.base.StudentService;
import com.cube001.cubeboot.service.community.IntegralScoreService;
import com.cube001.cubeboot.service.community.dto.CommunityScoreConverter;
import com.cube001.cubeboot.service.community.dto.IntegralScoreDto;
import com.cube001.cubeboot.service.community.dto.PersonalScoreSearch;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *功能描述    积分查询
 * @author     赵金毅
 * @date       2019/3/23
 * @param
 * @return
*/
@RestController
@RequestMapping(value = "${cubeboot.app-manager-path}/integral-score")
@Api(description = "积分控制类",value = "integral-score-controller")
public class IntegralScoreController implements IApplicationService {

    @Autowired
    private IntegralScoreService integralScoreService;

    @Autowired
    private CommunityScoreConverter communityScoreConverter;
    @Autowired
    private StudentService studentService;
    /**
     * @description 系统管理员指定对某个学生添加一条积分记录,然后同步添加积分到student表
     * @author 宋泽山
     * @date 2025/4/12 17:12
     * @param name 积分类型的说明
     * @param num 积分大小
     * @param studentId  指定的学生id
     * @return 添加是否成功
     */
    @ApiOperation(value = "指定学生id添加一条积分记录,然后同步添加积分到student表",notes = "指定添加一条积分记录并同步到student表")
    @PostMapping(value = "/systemAdd")
//  @RequiresPermissions("IntegralScore:add")
    public Boolean systemAdd(@RequestParam("name")String name,@RequestParam("num")Integer num,
                             @RequestParam("studentId")Long studentId){
        IntegralScore integralScore = new IntegralScore();
        integralScore.setNum(num).setName(name).setStudentId(studentId);
        return integralScoreService.add(ShiroKit.getUserId(),name,num,studentId);
    }
    /**
     * @description 每次自动完成相应任务后,自动添加一条积分记录,然后同步添加积分到student表
     * @author 宋泽山
     * @date 2025/4/12 17:12
     * @param name 积分类型名称的说明
     * @param num 积分大小
     * @return 添加是否成功
     */
    @ApiOperation(value = "对当前学生添加一条积分记录,然后同步添加积分到student表",notes = "对当前学生添加一条积分记录并同步到student表")
    @PostMapping(value = "/autoAdd")
//  @RequiresPermissions("IntegralScore:add")
    public Boolean autoAdd(@RequestParam("name")String name,@RequestParam("num")Integer num) {
        IntegralScore integralScore = new IntegralScore();
        integralScore.setNum(num).setName(name);
        return integralScoreService.add(ShiroKit.getUserId(),name,num,null);
    }
    /**
     *功能描述     获取当前用户id的所有积分列表
     * @author     赵金毅
     * @date       2019/3/22
    */
    @ApiOperation(value = "分页")
    @RequestMapping(value = "page", method = RequestMethod.POST)
    public Page<IntegralScoreDto> page(@RequestBody PersonalScoreSearch personalScoreSearch){
        Page<IntegralScore> page = new Page<>(
                personalScoreSearch.getPageIndex(),
                personalScoreSearch.getPageSize(),
                personalScoreSearch.getOrderFiled(),
                personalScoreSearch.getIsAsc()
        );
        EntityWrapper<IntegralScore> integralScoreEntityWrapper = new EntityWrapper<>();
        if (personalScoreSearch.getStartDate() != null) {
            integralScoreEntityWrapper.ge("issue_time", personalScoreSearch.getStartDate());
        }
        if (personalScoreSearch.getEndDate() != null) {
            integralScoreEntityWrapper.le("issue_time", personalScoreSearch.getEndDate());
        }
        //设置学生id
        Long userId = ShiroKit.getUserId();
        Long studentId = studentService.selectOne(new EntityWrapper<Student>().eq("user_id", userId)).getId();
        integralScoreEntityWrapper.eq("student_id", studentId);

        integralScoreService.selectPage(page, integralScoreEntityWrapper);
        return communityScoreConverter.pageCommunityScoreDto(page);
    }

    /**
     *功能描述      获取 当前用户id的全部积分
     * @author     赵金毅
     * @date       2019/3/22
     * @param
     * @return
    */
    @ApiOperation(value = " 当前用户id的全部积分")
    @RequestMapping(value = "scoreNum", method = RequestMethod.POST)
    public Map<String, Integer> getPersonScore(){
        Long userId = ShiroKit.getUserId();
        Long studentId = studentService.selectOne(new EntityWrapper<Student>().eq("user_id", userId)).getId();
        Map<String, Integer> map = integralScoreService.selectPersonalScore(studentId);
        return map;
    }

    /**
    * @description  通过用户id获取该学生的所有积分
    * @author 马勇超
    * @date 2025/5/8 17:30
    * @param   userId
    * @return
    */
    @ApiOperation(value = "通过用户id获取该学生的所有积分")
    @RequestMapping(value = "studentPersonScore", method = RequestMethod.GET)
    public Map<String, Integer> getStudentPersonScore(@RequestParam("userId") Long userId){
        Student student = studentService.selectOne(new EntityWrapper<Student>().eq("user_id",userId));
        if(null!=student){
            Map<String, Integer> map = integralScoreService.selectPersonalScore(student.getId());
            return map;
        }else{
             throw new FriendException("非学生用户！");
        }
    }
}

