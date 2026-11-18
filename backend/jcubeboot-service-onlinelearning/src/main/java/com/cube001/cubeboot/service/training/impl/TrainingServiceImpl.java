package com.cube001.cubeboot.service.training.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.core.util.ToolUtil;
import com.cube001.cubeboot.course.entity.Exercise;
import com.cube001.cubeboot.course.entity.ExerciseJoin;
import com.cube001.cubeboot.service.assignment.dto.RecommendWorkDto;
import com.cube001.cubeboot.service.base.mapper.StudentMapper;
import com.cube001.cubeboot.service.course.mapper.ExerciseJoinMapper;
import com.cube001.cubeboot.service.course.mapper.ExerciseMapper;
import com.cube001.cubeboot.service.training.CodeService;
import com.cube001.cubeboot.service.training.dto.CodeConverter;
import com.cube001.cubeboot.service.training.dto.CodeDto;
import com.cube001.cubeboot.service.training.dto.TrainingConverter;
import com.cube001.cubeboot.service.training.dto.TrainingDto;
import com.cube001.cubeboot.service.training.mapper.CodeMapper;
import com.cube001.cubeboot.training.entity.Code;
import com.cube001.cubeboot.training.entity.Training;
import com.cube001.cubeboot.service.training.TrainingService;
import com.cube001.cubeboot.service.training.mapper.TrainingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
* Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.*
* FileName: TrainingServiceImpl.java
* 全部显示作业功能TrainingServiceImpl的详细说明 :
*
* @author 宋泽山
* @date 2025/3/15 17:35
* @version 1.0
*/
@Service
public class TrainingServiceImpl extends ServiceImpl<TrainingMapper, Training> implements TrainingService {

    @Autowired
    private ExerciseMapper exerciseMapper;
    @Autowired
    private ExerciseJoinMapper exerciseJoinMapper;
    @Autowired
    private CodeService codeService;
    @Autowired
    private CodeConverter codeConverter;
    @Autowired
    private TrainingConverter trainingConverter;
    @Autowired
    private StudentMapper studentMapper;
    /**
     * @description  根据模糊查询的条件设置wrapper
     * @author 宋泽山
     * @date 2025/3/29 19:48
     * @param searchDto 分页查询
     * @return 分页查询对应的实体wrapper
     * @throws ParseException 处理日期转换发生的异常
     */
    @Override
    public EntityWrapper<Training> setWrapper(CommonSearchDto<Training> searchDto) throws ParseException {
        EntityWrapper<Training> wrapper = new EntityWrapper<>();
        if(!ToolUtil.isEmpty(searchDto.getForm().get("stuId"))){
            String stuId = String.valueOf(searchDto.getForm().get("stuId"));
            //如何输入其他非Long数据,抛出异常
            try {
                wrapper.like("t.student_id", stuId);
            }catch (Exception e){
                throw new FriendException("请不要输入除数字外的其他字符进行学号查询");
            }
        }
        if(!ToolUtil.isEmpty(searchDto.getForm().get("title"))){
            wrapper.like("t.title","%"+searchDto.getForm().get("title")+"%");
        }
        if(!ToolUtil.isEmpty(searchDto.getForm().get("studentName") )){
            wrapper.like("stu.name","%"+searchDto.getForm().get("studentName")+"%");
        }
        if(!ToolUtil.isEmpty(searchDto.getForm().get("beginTime")) && !ToolUtil.isEmpty(searchDto.getForm().get("endTime"))){
            String beginTime = (String)searchDto.getForm().get("beginTime");
            String endTime = (String)searchDto.getForm().get("endTime");

            //格式化时间 将"2019-01-31T16:00:00.000Z" --> 转换成"2019-01-31 16:00:00"
            int pos1=beginTime.lastIndexOf(".");
            if(pos1!=-1){
                beginTime=beginTime.substring(0,pos1);
            }
            int pos2=endTime.lastIndexOf(".");
            if(pos2!=-1){
                endTime=endTime.substring(0,pos2);
            }

            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

            wrapper.between("t.last_modification_time",sdf.parse(beginTime) ,sdf.parse(endTime));
        }

        return wrapper;
    }

    /**
     * @description 功能#792: 实现分页查询所有人的练习表
     * @author 宋泽山
     * @date 2025/3/27 14:18
     * @param  plusPage 分页
     * @param  wrapper 条件构造器
     * @param  userId 当前用户id
     * @return 所有人的分页详情
     */
    @Override
    public Page<Training> pageAllSelf(Page<Training> plusPage, Wrapper<Training> wrapper,Long userId) {
        List<Training> pages = baseMapper.pageAllSelf(plusPage, wrapper,userId);
        plusPage.setRecords(pages);
        return plusPage;
    }

    /**
     * @description 功能2: 实现个人练习表的分页
     * @author 宋泽山
     * @date 2025/3/27 14:18
     * @param plusPage 初始的分页结果
     * @param wrapper 条件构造器
     * @param studentId 用户id
     * @return 单人分页详情
     */
    @Override
    public Page<Training> pageOneself(Page<Training> plusPage, Wrapper<Training> wrapper,Long studentId) {
        List<Training> pages = baseMapper.pageOneself(plusPage, wrapper.eq("is_deleted", "0").
                eq("student_id", studentId), studentId);
        plusPage.setRecords(pages);
        return plusPage;
    }

    /**
     * @description 全部显示作业功能
     * @author 宋泽山
     * @date 2025/3/16 14:19
     * @param stuID: 学生id
     * @return 练习列表
     */
    @Override
    public List<Training> showAll(Long stuID) {
        return baseMapper.showAll(stuID);
    }
    /**
     * @description 根据文件夹的id 得到某个学生对应的文件夹下 的所有练习 以及 练习目录
     * @author 宋泽山
     * @date 2025/3/18 10:16
     * @param studentId 用户ID
     * @param dbId 文件夹的id
     * @return 练习列表
     */
    @Override
    public List<Training> getFolderListById(Long studentId, Long dbId) {
        if(dbId==0){
            return baseMapper.getListByHome(studentId);
        }
        else{
            return baseMapper.getFolderListById(studentId, dbId);
        }
    }
    /**
     * @description
     * @author 张人杰
     * @date 2025/4/12 17:03
     * @Param Long id练习ID
     * @return Training 联系内容和练习代码
     */

    @Override
    public Training getTrainingAndCodeById(Long id) {
        Training trainingAndCode = baseMapper.getTrainingAndCode(id);
        return trainingAndCode;
    }

//    /**
//     * 创建实验练习
//     * @author Juci
//     * @param exerciseJoinId 参与实验编号
//     * @param codeDto 代码
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public CodeDto createExerciseTraing(Long exerciseJoinId,CodeDto codeDto){
//        ExerciseJoin exerciseJoin = this.exerciseJoinMapper.selectById(exerciseJoinId);
//        // 新建练习记录
//        Exercise exercise = this.exerciseMapper.selectById(exerciseJoin.getExerciseId());
//        String title = "参与实验“" + exercise.getName() + "”";
//        Code code = codeConverter.dtoToEntity(codeDto);
//        code.setId(null);
//        this.codeMapper.insert(code);
//        codeDto.setId(code.getId());
//        Training training = new Training();
//        training.setCodeId(code.getId());
//        training.setPrivateOrNot(true);
//        training.setStudentId(exerciseJoin.getStudentId());
//        training.setTitle(title);
//        training.setSummary(title);
//        training.setDifficultLevel(exercise.getComplexity());
//        this.baseMapper.insert(training);
//        // 将练习编号存入实验参与表中
//        exerciseJoin.setTrainingId(training.getId());
//        this.exerciseJoinMapper.updateById(exerciseJoin);
//        return codeDto;
//    }
    /**
     * 创建实验练习
     * @author Juci
     * @param exerciseJoin 参与实验
     * @param codeDto 代码
     * @param type 类型 0-创建新练习 1-修改已有练习
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createExerciseTraing(ExerciseJoin exerciseJoin,CodeDto codeDto,Integer type){
        if(type==1 && exerciseJoin.getTrainingId()!=null) {
            Training training =this.baseMapper.selectById(exerciseJoin.getTrainingId());
            Code code = this.codeService.selectById(training.getCodeId());
            code.setCss(codeDto.getCss());
            code.setCssConfig(codeDto.getCssConfig());
            code.setJs(codeDto.getJs());
            code.setJsConfig(codeDto.getJsConfig());
            code.setHtml(codeDto.getHtml());
            code.setHtmlConfig(codeDto.getHtmlConfig());
            this.codeService.updateById(code);
        }
        else
        {
            // 新建练习记录
            Exercise exercise = this.exerciseMapper.selectById(exerciseJoin.getExerciseId());
            String title = "参与实验“" + exercise.getName() + "”";
            Code code = codeConverter.dtoToEntity(codeDto);
            code.setId(null);
            this.codeService.insert(code);
            codeDto.setId(code.getId());
            Training training = new Training();
            training.setCodeId(code.getId());
            training.setPrivateOrNot(true);
            training.setStudentId(exerciseJoin.getStudentId());
            training.setTitle(title);
            training.setSummary(title);
            training.setDifficultLevel(exercise.getComplexity());
            this.baseMapper.insert(training);
            // 将练习编号存入实验参与表中
            exerciseJoin.setTrainingId(training.getId());
            this.exerciseJoinMapper.updateById(exerciseJoin);
        }
    }

    @Override
    public List<Training> getTrainingList() {
        Long userId = ShiroKit.getUserId();
//        Long userId=117L;
        Wrapper<Student> studentWrapper=new EntityWrapper<>();
        studentWrapper.eq("user_id",userId);
        List<Student> students = studentMapper.selectList(studentWrapper);
        Wrapper<Training> trainingWrapper=new EntityWrapper<>();
        List<Training> trainings=null;
        for (Student s:students) {
            trainingWrapper.eq("student_id",s.getId());
           trainings = baseMapper.selectList(trainingWrapper);
        }
        return trainings;
    }
    /**
     * 将优秀作品Fork成自己的作品
     * @author Juci
     * @date 2025/5/10
     * @param recommendWorkDto
     * @param userId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long fork(RecommendWorkDto recommendWorkDto,Long userId){
        Student student = studentMapper.getStudentByUserId(userId);
        if(student==null){
            throw new FriendException("非学生用户无法Fork优秀作品！");
        }
        Code code = codeConverter.dtoToEntity(recommendWorkDto.getCodeDto());
        code.setId(null);
        codeService.insert(code);
        Training training = new Training();
        training.setTitle("fork作品“"+recommendWorkDto.getTitle()+"”");
        training.setSummary(training.getTitle());
        training.setPrivateOrNot(true);
        training.setStudentId(student.getId());
        training.setCodeId(code.getId());
        this.baseMapper.insert(training);
        return training.getId();
    }

    /**
     * 保存代码
     * @author Juci
     * @date 2025/5/13
     * @param trainingDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(TrainingDto trainingDto){
        Training training = trainingConverter.dtoToEntity(trainingDto);
        this.baseMapper.updateById(training);
        //Code code =trainingDto.getCode();
        //if(code==null){
        //    throw new FriendException("保存代码失败！");
        //}
        //code.setTenantId(null);
        //this.codeMapper.updateById(code);
    }

    /**
     * 保存实验代码
     * @author Juci
     * @param userId
     * @param trainingDto
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveTraining(Long userId,TrainingDto trainingDto){
        Student student = studentMapper.getStudentByUserId(userId);
        if(student==null){
            throw new FriendException("非学生用户无法参与实验！");
        }
        if(trainingDto.getId()!=null && trainingDto.getId()!=0){
            this.codeService.updateCodeDto(userId,trainingDto.getCodeDto());
        }
        else
        {
            Code code = codeConverter.dtoToEntity(trainingDto.getCodeDto());
            codeService.insert(code);
            trainingDto.setCodeId(code.getId());
            Training training = trainingConverter.dtoToEntity(trainingDto);
            training.setStudentId(student.getId());
            this.baseMapper.insert(training);
        }
    }
}
