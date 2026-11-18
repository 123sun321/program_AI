package com.cube001.cubeboot.service.course.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.base.entity.Label;
import com.cube001.cubeboot.base.entity.LabelContent;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.base.entity.Teachers;
import com.cube001.cubeboot.core.authorization.entity.Role;
import com.cube001.cubeboot.core.authorization.service.RoleService;
import com.cube001.cubeboot.core.authorization.service.UserroleService;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.course.entity.Course;
import com.cube001.cubeboot.course.entity.Exercise;
import com.cube001.cubeboot.course.entity.ExerciseJoin;
import com.cube001.cubeboot.course.entity.ExerciseResource;
import com.cube001.cubeboot.service.base.LabelContentService;
import com.cube001.cubeboot.service.base.LabelService;
import com.cube001.cubeboot.service.base.TeachersService;
import com.cube001.cubeboot.service.base.mapper.LabelContentMapper;
import com.cube001.cubeboot.service.base.mapper.StudentMapper;
import com.cube001.cubeboot.service.course.CourseService;
import com.cube001.cubeboot.service.course.mapper.CourseMapper;
import com.cube001.cubeboot.service.course.mapper.ExerciseJoinMapper;
import com.cube001.cubeboot.service.training.CodeService;
import com.cube001.cubeboot.service.course.ExerciseResourceService;
import com.cube001.cubeboot.service.course.ExerciseService;
import com.cube001.cubeboot.service.course.dto.*;
import com.cube001.cubeboot.service.training.TrainingService;
import com.cube001.cubeboot.service.training.dto.CodeConverter;
import com.cube001.cubeboot.service.training.dto.TrainingDto;
import com.cube001.cubeboot.service.training.mapper.CodeMapper;
import com.cube001.cubeboot.service.course.mapper.ExerciseMapper;
import com.cube001.cubeboot.service.course.mapper.ExerciseResourceMapper;
import com.cube001.cubeboot.service.training.dto.CodeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: ExerciseServiceImpl
 * 实验练习Service实现类
 *
 * @author Juci
 * @version 1.0
 * @date 2025/3/11 13:56
 */
@Service
public class ExerciseServiceImpl extends ServiceImpl<ExerciseMapper, Exercise> implements ExerciseService {

    @Autowired
    private ExerciseResourceService exerciseResourceService;
    @Autowired
    private ExerciseResourceMapper exerciseResourceMapper;
    @Autowired
    private CodeService codeService;
    @Autowired
    private ExerciseConverter exerciseConverter;
    @Autowired
    private CodeMapper codeMapper;
    @Autowired
    private LabelService labelService;
    @Autowired
    private LabelContentMapper labelContentMapper;
    @Autowired
    private LabelContentService labelContentService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private TeachersService teachersService;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private ExerciseJoinMapper exerciseJoinMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private CodeConverter codeConverter;
    @Autowired
    private TrainingService trainingService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(ExerciseDto exerciseDto){
        // 判断是否有初始代码
        if(exerciseDto.getHaveCode()) {
            Long codeId = codeService.insert(exerciseDto.getCodeDto());
            if(codeId>0){
                exerciseDto.setCodeId(codeId);
            }
            else{
                throw new FriendException("初始代码保存失败！");
            }
        }
        // 保存实验练习信息
        List<Role> roles = roleService.getRolesByUserId(exerciseDto.getUserId());
        // 判断是否含有教师角色，初始化作者信息
        if(roles.stream().filter(m->"teacher".equals(m.getName())).count()>0){
            Teachers teachers = teachersService.getByUserId(exerciseDto.getUserId());
            if(teachers==null){
                throw new FriendException("教师信息不存在！");
            }
            exerciseDto.setAuthor(teachers.getName());
        }
        Exercise exercise = exerciseConverter.dtoToEntity(exerciseDto);
        if (this.baseMapper.insert(exercise)>0){
            if(exercise.getType()==0 && exercise.getCourseId()!=null){
                this.courseMapper.updateExerciseNumber(exercise.getCourseId());
            }
            //保存标签
            SaveExerciseTags(exerciseDto.getTags(),exercise.getId());
            //保存实验资源
            exerciseDto.setId(exercise.getId());
            SaveExerciseResource(exerciseDto);
        }
        else {
            throw new FriendException("实验保存失败！");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ExerciseDto exerciseDto){
        // 判断是否有初始代码
        if(exerciseDto.getHaveCode()) {
            Long codeId = null;
            if(exerciseDto.getCodeId()!=null){
                codeId = codeService.update(exerciseDto.getCodeDto());
            }
            else {
                codeId = codeService.insert(exerciseDto.getCodeDto());
            }
            if (codeId != null && codeId > 0) {
                exerciseDto.setCodeId(codeId);
            } else {
                throw new FriendException("初始代码保存失败！");
            }
        }
        // 保存实验练习信息
        Exercise exercise = exerciseConverter.dtoToEntity(exerciseDto);
        if (this.baseMapper.updateById(exercise)>0){
            //保存标签
            SaveExerciseTags(exerciseDto.getTags(),exercise.getId());
            // 保存课程资源
            SaveExerciseResource(exerciseDto);
        }
        else {
            throw new FriendException("实验保存失败！");
        }
    }
    @Override
    public ExerciseDto get(Long exerciseId){
        ExerciseDto exerciseDto = new ExerciseDto();
        //获取基本信息
        Exercise exercise = this.baseMapper.selectById(exerciseId);
        if(null !=exercise){
            exerciseDto = exerciseConverter.entityToDto(exercise);
            // 获取标签
            exerciseDto.setTags(labelContentService.getNameByLinkId(exerciseId));
            //获取实验数据
            if(exerciseDto.getHaveCode()){
                exerciseDto.setCodeDto(codeService.get(exerciseDto.getCodeId()));
            }
            else {
                exerciseDto.setCodeDto(new CodeDto());
            }
            //获取实验资源
            exerciseDto.setExerciseResourceDtoList(exerciseResourceService.getList(exerciseId));
        }
        return exerciseDto;
    }

    /**
     * 开始实验
     * @author Juci
     * @param exerciseJoinId
     * @param userId
     * @param exerciseId
     * @return
     */
//    @Override
//    public ExerciseDto start(Long exerciseJoinId,Long userId,Long exerciseId){
//        Student student = studentMapper.getStudentByUserId(userId);
//        if(student==null){
//            throw new FriendException("非学生用户无法加入实验！");
//        }
//        ExerciseDto exerciseDto = new ExerciseDto();
//        //获取基本信息
//        Exercise exercise = this.baseMapper.selectById(exerciseId);
//        if(null !=exercise){
//            exerciseDto = exerciseConverter.entityToDto(exercise);
//            // 获取实验数据
//            CodeDto codeDto = new CodeDto();
//            if (exerciseDto.getHaveCode()) {
//                codeDto = codeService.get(exerciseDto.getCodeId());
//            }
//            //创建新的练习记录
//            exerciseDto.setCodeDto(this.trainingService.createExerciseTraing(exerciseJoinId,codeDto));
//            //获取实验资源
//            exerciseDto.setExerciseResourceDtoList(exerciseResourceService.getList(exerciseId));
//        }
//        return exerciseDto;
//    }

    /**
     * 获取实验信息
     * @author Juci
     * @param userId
     * @param exerciseId
     * @param type
     * @return
     */
    @Override
    public ExerciseDto getExerciseInfo(Long userId,Long exerciseId,Integer type){
        Student student = studentMapper.getStudentByUserId(userId);
        if(student==null){
            throw new FriendException("非学生用户无法加入实验！");
        }
        ExerciseDto exerciseDto = new ExerciseDto();
        //获取基本信息
        Exercise exercise = this.baseMapper.selectById(exerciseId);
        if(null !=exercise){
            exerciseDto = exerciseConverter.entityToDto(exercise);
            if(type == 0){
                // 获取实验数据
                if (exerciseDto.getHaveCode()) {
                    CodeDto codeDto = codeService.get(exerciseDto.getCodeId());
                    codeDto.setId(null);
                    exerciseDto.setCodeDto(codeDto);
                }
            }
            else {
                // 获取已做过的实验代码
                ExerciseJoinDetailDto exerciseJoinDetailDto = this.exerciseJoinMapper.joinDetailByExerciseIdAndStudentId(exerciseId,student.getId());
                if(exerciseJoinDetailDto!=null && exerciseJoinDetailDto.getTrainingDto()!=null && exerciseJoinDetailDto.getTrainingDto().getCode()!=null) {
                    exerciseDto.setCodeDto(codeConverter.entityToDto(exerciseJoinDetailDto.getTrainingDto().getCode()));
                }
            }
            // 获取实验资源
            exerciseDto.setExerciseResourceDtoList(exerciseResourceService.getList(exerciseId));
        }
        return exerciseDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> ids){
        for (Long id : ids) {
            // 判断是否有人参加该实验
            Wrapper<ExerciseJoin> exerciseJoinWrapper = new EntityWrapper<>();
            exerciseJoinWrapper.eq("exercise_id",id);
            if(exerciseJoinMapper.selectCount(exerciseJoinWrapper)>0){
                throw new FriendException("已有学生参与实验，删除失败！");
            }
            Exercise exercise = this.baseMapper.selectById(id);
            if (null != exercise) {
                // 删除关联代码
                if (exercise.getCodeId() != null) {
                    if (this.codeMapper.deleteById(exercise.getCodeId()) == 0) {
                        throw new FriendException("删除关联代码失败！");
                    }
                }
                //删除标签
                Wrapper<LabelContent> labelContentWrapper = new EntityWrapper<>();
                labelContentWrapper.eq("link_id",id);
                labelContentMapper.delete(labelContentWrapper);
                //删除实验资源
                Wrapper<ExerciseResource> wrapper = new EntityWrapper<>();
                wrapper.eq("exercise_id", id);
                exerciseResourceMapper.delete(wrapper);
                //删除实验
                if (this.baseMapper.deleteById(id) == 0) {
                    throw new FriendException("删除实验失败！");
                }
                //同步课程实验数减一
                if(exercise.getCourseId()!=null && exercise.getCourseId()!=0){
                    Course course = this.courseMapper.selectById(exercise.getCourseId());
                    if(course!=null){
                        course.setExerciseNumber(course.getExerciseNumber()>1 ? course.getExerciseNumber()-1 : 0);
                        this.courseMapper.updateById(course);
                    }
                }
            }
            else {
                throw new FriendException("删除实验失败！");
            }
        }
    }
    /**
     * 保存实验资源
     * @author Juci
     * @param exerciseDto
     */
    private void SaveExerciseResource(ExerciseDto exerciseDto){
        Wrapper<ExerciseResource> wrapper = new EntityWrapper<>();
        wrapper.eq("exercise_id",exerciseDto.getId());
        List<ExerciseResourceDto> exerciseResourceDtos = exerciseDto.getExerciseResourceDtoList();
        List<Long> ids = exerciseResourceDtos.stream().map(m->m.getId()).collect(Collectors.toList());
        if(ids.size() == 0){
            exerciseResourceMapper.delete(wrapper);
        }
        else {
            wrapper.notIn("id", ids);
            exerciseResourceMapper.delete(wrapper);
        }
        for (ExerciseResourceDto exerciseResourceDto : exerciseResourceDtos) {
            if (exerciseResourceDto.getId() == null) {
                exerciseResourceDto.setExerciseId(exerciseDto.getId());
                if (exerciseResourceService.insert(exerciseResourceDto) == 0) {
                    throw new FriendException("实验资源保存失败！");
                }
            } else {
                if (exerciseResourceService.update(exerciseResourceDto) == 0) {
                    throw new FriendException("实验资源保存失败！");
                }
            }
        }
    }

    /**
     * 保存实验标签
     * @param tags
     * @param exerciseId
     */
    private void SaveExerciseTags(List<String> tags,Long exerciseId){
        // 删除已有的标签
        Wrapper<LabelContent> wrapper = new EntityWrapper<>();
        wrapper.eq("link_id",exerciseId);
        labelContentMapper.delete(wrapper);
        for (String tag : tags) {
            Label label = this.labelService.getByName(tag);
            if(null == label){
                throw new FriendException("创建标签失败！");
            }
            LabelContent labelContent = new LabelContent();
            labelContent.setLabelId(label.getId());
            labelContent.setType(1);
            labelContent.setLinkId(exerciseId);
            if(labelContentMapper.insert(labelContent)==0){
                throw new FriendException("实验添加标签失败！");
            }
        }
    }
    /**
     * 分页获取实验列表
     * @author Juci
     * @param page
     * @param wrapper
     * @return
     */
    @Override
    public Page<ExerciseDto> searchPage(Page<ExerciseDto> page, Wrapper<ExerciseDto> wrapper){
        wrapper.andNew().eq("a.is_deleted",0);
        List<ExerciseDto> exerciseDtos = this.baseMapper.searchPage(page, wrapper);
        page.setRecords(exerciseDtos);
        return page;
    }

    /**
     * @description  获取实验列表
     * @author 马勇超
     * @date 2025/4/9 15:40
     * @param   wrapper
     * @return   list
     */
    @Override
    public Page<ExerciseDto> selectWithCourse(Page page,Wrapper<Exercise> wrapper) {
        List<ExerciseDto> exerciseList = baseMapper.selectWithCourse(page,wrapper
                .eq("a.type",1)
                .eq("a.is_deleted",0)
                .groupBy("a.id"));
        page.setRecords(exerciseList);
        return page;
    }
}
