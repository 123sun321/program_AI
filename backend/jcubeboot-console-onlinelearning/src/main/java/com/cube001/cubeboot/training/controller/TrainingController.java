package com.cube001.cubeboot.training.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.approve.entity.Approve;
import com.cube001.cubeboot.approve.entity.enums.ApproveEnum;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.service.approve.ApproveService;
import com.cube001.cubeboot.service.assignment.dto.RecommendWorkDto;
import com.cube001.cubeboot.service.base.StudentService;
import com.cube001.cubeboot.service.training.CodeService;
import com.cube001.cubeboot.service.training.TrainingService;
import com.cube001.cubeboot.service.training.dto.CodeConverter;
import com.cube001.cubeboot.service.training.dto.TrainingConverter;
import com.cube001.cubeboot.service.training.dto.TrainingDto;
import com.cube001.cubeboot.service.training.dto.TrainingPageDto;
import com.cube001.cubeboot.training.entity.Code;
import com.cube001.cubeboot.training.entity.Training;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.util.List;

/**
* Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.* FileName: TrainingController.java
* 类的详细说明 : TrainingController
*
* @author anonymous
* @date 2025/3/15 17:31
* @version 1.0
*/
@Api(value = "training controller", description = "练习表controller")
@RestController
@RequestMapping(value = "${cubeboot.app-manager-path}/training")
//@RequiresPermissions("training")
public class TrainingController  implements IApplicationService {

    @Autowired
    private TrainingService trainingService;
    @Autowired
    private TrainingConverter trainingConverter;
    @Autowired
    private ApproveService approveService;
    @Autowired
    private StudentService studentService;
    /**
     * @description 功能1: 根据练习表的id查询出对应的内容 #供他人调用的接口
     * @author anonymous
     * @date 2025/3/26 15:48
     * @param id 练习表的id
     * @return 练习表的dto
     */
    @ApiOperation(value = "功能1: 根据练习表的id查询出对应的内容", notes = "根据练习表的id查询出对应的内容")
    @GetMapping(value = "/selectById")
    public TrainingDto selectById(@RequestParam("id") Long id) {
        /** 查询出单个的练习表*/
        Training training = trainingService.selectById(id);
        return trainingConverter.entityToDto(training);
    }

    /**
     * 保持练习结果
     * @author anonymous
     * @param trainingDto
     */
    @ApiOperation(value = "保持练习结果", notes = "保持练习结果")
    @PostMapping(value = "/saveTraining")
    public void saveTraining(@RequestBody TrainingDto trainingDto){
        this.trainingService.saveTraining(ShiroKit.getUserId(),trainingDto);
    }
    /**
     * @description 功能2: 实现个人练习表的分页 --功能#771
     * @author anonymous
     * @date 2025/3/27 16:08
     * @param searchDto 分页查询
     * @return 分页结果
     */
    @ApiOperation(value = "功能2: 实现个人练习表的分页", notes = "个人练习表的分页")
    @PostMapping(value = "/pageOneself")
//    @RequiresPermissions("training:page")
    public Page<TrainingDto> pageOneself(@RequestBody CommonSearchDto<Training> searchDto){

        /**后台获取当前用户的userId, 然后查出studentId*/
        Long userId = ShiroKit.getUserId();
        Long studentId = studentService.selectOne(new EntityWrapper<Student>().eq("user_id", userId)).getId();

        Page<Training> trainingPageOneself = trainingService.pageOneself(searchDto.getPlusPage(),
                searchDto.formToEntityWrapper(null), studentId);
        return trainingConverter.pageToDto(trainingPageOneself);
    }

    /**
     * @description 功能3: 分页查询所有人的练习表的内容 --功能#792
     * @author anonymous
     * @date 2025/3/26 15:49
     * @param  searchDto  传入的分页查询,同时组合了:学生编号-实验标题-学生姓名-提交时间段
     * @return 综合处理后的分页结果
     */
    @ApiOperation(value = "功能3: 分页+模糊查询查询所有人的练习表的内容", notes = "分页+模糊查询查询")
    @PostMapping(value = "/pageAllSelf")
//    @RequiresPermissions("training:page")
    public Page<TrainingPageDto> page(@RequestBody CommonSearchDto<Training> searchDto) throws ParseException {

        Page<Training> page= searchDto.getPlusPage();

        Wrapper<Training> wrapper = trainingService.setWrapper(searchDto);

        Long userId = ShiroKit.getUserId();

        page = trainingService.pageAllSelf(page, wrapper,userId);

        return trainingConverter.pageToPageDto(page);
    }
    /**
     * @description 功能4: 实现当前用户点赞一次别人的练习表
     * @author anonymous
     * @date 2025/4/5 11:20
     * @param trainingId 练习表id
     * @return 点赞成功是否,true or false
     */
    @ApiOperation(value = "功能4: 实现当前用户点赞一次别人的练习表", notes = "点赞")
    @GetMapping(value = "/trainingApprove")
//    @RequiresPermissions("training:get")
    public Boolean trainingApprove(@RequestParam("trainingId") Long trainingId) {

        /**后台获取当前用户的userId */
        Long userId = ShiroKit.getUserId();

        /** 先判重,重复点赞的话 new FriendException */
        if(approveService.selectCount(new EntityWrapper<Approve>().eq("user_id", userId).eq("link_id", trainingId))!=0  ){
            throw new FriendException("请不要重复点赞~");
        }
        /** 没有重复,执行添加一条点赞记录的操作*/
        Approve approve=new Approve();
        approve.setUserId(userId);
        approve.setLinkId(trainingId);
        approve.setCategory(ApproveEnum.TrainingApprove);
        /** 同时,向练习表中对应的点赞数+1*/
        if(approveService.insert(approve)){
            Training t=trainingService.selectById(trainingId);
            t.setLikeNumber(t.getLikeNumber()+1);
            return trainingService.updateById(t);
        }
        else{
            return false;
        }
    }
    @ApiOperation(value = "教师端作业提交详情预览学生代码")
    @GetMapping(value = "/getTrainAndCodeById")
    public TrainingDto getTrainAndCodeById(@RequestParam("id") Long id){
        Training training = trainingService.getTrainingAndCodeById(id);
        TrainingDto trainingDto = trainingConverter.entityToDto(training);
        //若学生打开自己的练习则可以编辑
        Long userId = ShiroKit.getUserId();
        Student student = studentService.getStudentByUserId(userId);
        if(student!=null && student.getId().equals(training.getStudentId())) {
            trainingDto.setCanEdit(true);
        }
        else{
            trainingDto.setCanEdit(false);
        }
        return trainingDto;
    }
    @ApiOperation(value = "学生端获取学生所有练习")
    @GetMapping(value = "/getTrainingList")
    public List<Training> getTrainList(){
        return  trainingService.getTrainingList();
    }

    /**
     * 将优秀作品Fork成自己的作品
     * @author anonymous
     * @param recommendWorkDto
     */
    @ApiOperation(value = "将优秀作品Fork成自己的作品")
    @PostMapping(value = "/fork")
    public Long fork(@RequestBody RecommendWorkDto recommendWorkDto){
        return trainingService.fork(recommendWorkDto,ShiroKit.getUserId());
    }

    /**
     * 保存练习
     * @param trainingDto
     */
    @ApiOperation(value = "保存练习")
    @PostMapping(value = "/save")
    public void save(@RequestBody TrainingDto trainingDto){
        trainingService.save(trainingDto);
    }
}

