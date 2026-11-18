package com.cube001.cubeboot.service.course.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.course.entity.CourseJoin;
import com.cube001.cubeboot.course.entity.Exercise;
import com.cube001.cubeboot.course.entity.ExerciseJoin;
import com.cube001.cubeboot.service.base.StudentService;
import com.cube001.cubeboot.service.base.mapper.StudentMapper;
import com.cube001.cubeboot.service.community.IntegralScoreService;
import com.cube001.cubeboot.service.course.ExerciseJoinRecordService;
import com.cube001.cubeboot.service.course.ExerciseJoinService;
import com.cube001.cubeboot.service.course.dto.ExerciseJoinDetailDto;
import com.cube001.cubeboot.service.course.dto.ExerciseJoinRecord.ExerciseJoinRecordDto;
import com.cube001.cubeboot.service.course.dto.ExerciseJoinRecord.JoinExerciseDto;
import com.cube001.cubeboot.service.course.mapper.CourseJoinMapper;
import com.cube001.cubeboot.service.course.mapper.ExerciseJoinMapper;
import com.cube001.cubeboot.service.course.mapper.ExerciseJoinRecordMapper;
import com.cube001.cubeboot.service.course.mapper.ExerciseMapper;
import com.cube001.cubeboot.service.training.TrainingService;
import com.cube001.cubeboot.service.training.mapper.TrainingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author myc
 * @since 2019-03-12
 */
@Service
public class ExerciseJoinServiceImpl extends ServiceImpl<ExerciseJoinMapper, ExerciseJoin> implements ExerciseJoinService {

    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private CourseJoinMapper courseJoinMapper;
    @Autowired
    private ExerciseMapper exerciseMapper;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TrainingService trainingService;
    @Autowired
    private ExerciseJoinRecordService exerciseJoinRecordService;
    @Autowired
    private IntegralScoreService integralScoreService;
    /**
     * @description 通过课程id查找相关的练习记录/参与记录
     * @author 宋泽山
     * @date 2025/4/27 16:18
     * @param courseId 课程Id
     * @return
     */
    @Override
    public List<ExerciseJoin> getExerciseListByCourseId(Long courseId) {
        Long userId = ShiroKit.getUserId();
        Long studentId  = studentService.selectOne(new EntityWrapper<Student>().eq("user_id", userId)).getId();
        return baseMapper.getExerciseListByCourseId(courseId, studentId);
    }

    /**
     * @description 搜索/分页查询参与课程的学生完成的实验
     * @author 张浩杰
     * @date 2025年3月15日 10:06:10
     * @param
     * @return
     */
    @Override
    public Page<ExerciseJoin> selectWithStudent(Page<ExerciseJoin> page, Wrapper<ExerciseJoin> wrapper) {
        List<ExerciseJoin> exerciseJoinList = baseMapper.selectWithStudent(page,wrapper.eq("a.is_deleted",0));
        page.setRecords(exerciseJoinList);
        return page;
    }
    /**
     * @description 删除/批量删除
     * @author 张浩杰
     * @date 2025年3月15日 10:01:23
     * @param ids
     * @return
     */
    @Override
    public Boolean delete(List<Long> ids) {
        Boolean result = null;
        if(ids.size()>0){
            for (Long id : ids) {
                result = this.deleteById(id);
            }
        }
        return result;
    }
    @Override
    public ExerciseJoinDetailDto joinDetail(Long exerciseJoinId)
    {
        return this.baseMapper.joinDetail(exerciseJoinId);
    }
    /**
     * 参与实验
     * @author juci
     * @param userId 用户编号
     * @param exerciseId 实验编号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExerciseJoin insert(Long userId,Long exerciseId){
        Student student = studentMapper.getStudentByUserId(userId);
        if(student==null){
            throw new FriendException("非学生用户无法加入实验！");
        }
        ExerciseJoin exerciseJoin = new ExerciseJoin();
        Exercise exercise = this.exerciseMapper.selectById(exerciseId);
        if(exercise==null){
            throw new FriendException("实验不存在！");
        }
        Wrapper<ExerciseJoin> wrapper = new EntityWrapper<>();
        wrapper.eq("student_id",student.getId());
        wrapper.eq("exercise_id",exerciseId);
        List<ExerciseJoin> exerciseJoins = this.baseMapper.selectList(wrapper);
        if(exerciseJoins.size() == 0){
            //若是课程实验判断是否先加入课程学习
            if(exercise.getType()==0) {
                CourseJoin courseJoin = new CourseJoin();
                Wrapper<CourseJoin> courseJoinWrapper = new EntityWrapper<>();
                courseJoinWrapper.eq("course_id", exercise.getCourseId());
                courseJoinWrapper.eq("student_id", student.getId());
                List<CourseJoin> courseJoinList = this.courseJoinMapper.selectList(courseJoinWrapper);
                if (courseJoinList.size() == 0) {
                    courseJoin.setJoinExercise(1);
                    courseJoin.setStudentId(student.getId());
                    courseJoin.setCourseId(exercise.getCourseId());
                    if (this.courseJoinMapper.insert(courseJoin) == 0) {
                        throw new FriendException("参与课程失败！");
                    }
                }
                else{
                    courseJoin = courseJoinList.get(0);
                    courseJoin.setJoinExercise(courseJoin.getJoinExercise()+1);
                    this.courseJoinMapper.updateById(courseJoin);
                }
            }
            //加入实验
            exerciseJoin.setExerciseId(exercise.getId());
            exerciseJoin.setStudentId(student.getId());
            if(this.baseMapper.insert(exerciseJoin)==0){
                throw new FriendException("加入实验失败！");
            }
            //实验参与人数+1
            exercise.setJoinNumber(exercise.getJoinNumber()>0 ?  exercise.getJoinNumber()+1 : 1);
            this.exerciseMapper.updateById(exercise);
        }
        else
        {
            exerciseJoin= exerciseJoins.get(0);
        }
        return  exerciseJoin;
    }

    /**
     * @description  获取当前用户参与的实验---作品展示
     * @author 马勇超
     * @date 2025/4/9 15:45
     * @param   id,wrapper
     * @return   List
     */
    @Override
    public List<ExerciseJoin> selectWithExerciseStudent(Long id,Wrapper<ExerciseJoin> wrapper) {
        List<ExerciseJoin> exerciseJoins = baseMapper.selectWithExerciseStudent(wrapper.eq("a.is_deleted",0).eq("s.user_id",id));
        return exerciseJoins;
    }

    /**
     * 结束实验
     * @author Juci
     * @param userId
     * @param joinExerciseDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void stopExercise(Long userId,JoinExerciseDto joinExerciseDto){
        Exercise exercise = this.exerciseMapper.selectById(joinExerciseDto.getExerciseId());
        if(exercise==null){
            throw new FriendException("实验不存在！");
        }
        // 结束实验一共分为以下几步：
        //一、判断是否加入过该实验，若没有加入过则创建加入记录
        ExerciseJoin exerciseJoin = this.insert(userId,joinExerciseDto.getExerciseId());
        //二、创建结束实验时代码及练习记录
        //创建新的练习记录
        this.trainingService.createExerciseTraing(exerciseJoin,joinExerciseDto.codeDto,joinExerciseDto.getType());
        //三、登记实验时间
        ExerciseJoinRecordDto exerciseJoinRecordDto = new ExerciseJoinRecordDto();
        exerciseJoinRecordDto.setMinute(joinExerciseDto.getStudyTime()/60);
        exerciseJoinRecordDto.setExerciseId(joinExerciseDto.getExerciseId());
        this.exerciseJoinRecordService.insert(exerciseJoinRecordDto,userId);
        //四、参与实验奖励积分
        this.integralScoreService.add(userId,"参与实验“"+exercise.getName()+"”",10,null);
    }
}
