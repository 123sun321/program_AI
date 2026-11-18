package com.cube001.cubeboot.service.community.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.community.entity.IntegralScore;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.course.entity.Course;
import com.cube001.cubeboot.service.base.StudentService;
import com.cube001.cubeboot.service.community.IntegralScoreService;
import com.cube001.cubeboot.service.community.mapper.IntegralScoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 *  服务实现类    计算积分
 * </p>
 *
 * @author ZJY
 * @since 2019-03-18
 */
@Service
public class IntegralScoreServiceImpl extends ServiceImpl<IntegralScoreMapper, IntegralScore> implements IntegralScoreService {

    @Autowired
    private IntegralScoreMapper integralScoreMapper;
    @Autowired
    private IntegralScoreService integralScoreService;
    @Autowired
    private StudentService studentService;
    /**
     *
     * @description 对某个学生添加一条积分记录,然后同步添加积分到student表
     * @author 宋泽山
     * @date 2025/4/17 17:18
     * @param userId 用户编号
     * @param name 积分类型
     * @param num 积分数
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(Long userId,String name, Integer num,Long studentId) {
        try {
            IntegralScore integralScore = new IntegralScore();
            integralScore.setName(name).setNum(num);
            integralScore.setStudentId(studentId);
            if(integralScore.getStudentId() == null){
                //自动添加类型,非指定, 添加studentId字段值
                Student stu= studentService.selectOne(new EntityWrapper<Student>().eq("user_id", userId));
                if(stu==null){
                    throw new FriendException("非学生用户！");
                }
                integralScore.setStudentId(stu.getId());
                studentId = stu.getId();
            }
            if(integralScore.getNum()<=0){
                throw new FriendException("积分必须大于0");
            }
            //设置添加时间,添加积分记录
            integralScore.setIssueTime(new Date());
            integralScoreService.insert(integralScore);

            //添加一条积分记录并同步到student表
            Student student= studentService.selectById(studentId);
            student.setIntegralScore(student.getIntegralScore()+integralScore.getNum());
            studentService.updateById(student);
            //添加成功返回true
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FriendException("添加积分失败!");
        }
    }

    /**
     *功能描述     根据 用户id 查找用户的积分列表
     * @author     赵金毅
     * @date       2019/3/22
     * @param
     * @return  map存储本周积分，本月积分，所有积分
    */
    @Override
    public Map<String, Integer> selectPersonalScore(long studentId) {
        Map<String, Integer> map = new HashMap<>(20);
        Integer scoreAll = 0;
        Integer scoreWeek = 0;
        Integer scoreMonth = 0;
        //存储当前日期
        Date date = new Date();

        List<IntegralScore> integralScoresList = integralScoreMapper.selectList(new EntityWrapper<IntegralScore>().eq("student_id", studentId));
        for (IntegralScore integralScore: integralScoresList){
            //  全部积分
            scoreAll += integralScore.getNum();
            if(integralScore.getIssueTime().after(startMonth(date))){
                // 筛选出本月的积分
                scoreMonth += integralScore.getNum();
                if(integralScore.getIssueTime().after(startWeek(date))){
                    //  筛选出本周的积分
                    scoreWeek += integralScore.getNum();
                }
            }
        }
        map.put("scoreAll", scoreAll);
        map.put("scoreWeek", scoreWeek);
        map.put("scoreMonth", scoreMonth);

        return map;
    }

    /*
     *功能描述    计算出本周的开始时间
     * @author     赵金毅
     * @date       2019/3/23
     * @return
    */
    public static Date startWeek(Date date) {
        // 使用Calendar类进行时间的计算
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会计算到下一周去。
        // dayWeek值（1、2、3...）对应周日，周一，周二...
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayWeek == 1) {
            dayWeek = 7;
        } else {
            dayWeek -= 1;
        }
        // 计算本周开始的时间
        cal.add(Calendar.DAY_OF_MONTH, 1 - dayWeek);
        //设置时间为 0:00
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE, 0);
        return cal.getTime();
    }

    /*
     *功能描述     获取本月的开始时间
     * @author     赵金毅
     * @date       2019/3/23
     * @param
     * @return
    */
    public Date startMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 设置为1号,当前日期既为本月第一天
        cal.set(Calendar.DAY_OF_MONTH, 1);
        //设置时间为 0:00
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE, 0);
        return cal.getTime();
    }
}
