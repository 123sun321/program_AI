package com.cube001.cubeboot.service.assignment.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.assignment.entity.Assignment;
import com.cube001.cubeboot.assignment.entity.AssignmentSubmit;
import com.cube001.cubeboot.base.entity.Grade;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.base.entity.Studenthonor;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.service.assignment.AssignmentService;
import com.cube001.cubeboot.service.assignment.dto.AssignmentConverter;
import com.cube001.cubeboot.service.assignment.dto.AssignmentDto;
import com.cube001.cubeboot.service.assignment.dto.ListAssignmentDto;
import com.cube001.cubeboot.service.assignment.mapper.AssignmentMapper;

import com.cube001.cubeboot.service.assignment.mapper.AssignmentSubmitMapper;
import com.cube001.cubeboot.service.base.mapper.StudentMapper;
import net.sf.ehcache.search.Query;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.object.MappingSqlQueryWithParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author renjie
 * @since 2019-03-18
 */
@Service
public class AssignmentServiceImpl extends ServiceImpl<AssignmentMapper, Assignment> implements AssignmentService {
    @Autowired
     private   AssignmentMapper assignmentMapper;
    @Autowired
     private AssignmentSubmitMapper assignmentSubmitMapper;

    @Autowired
     private   AssignmentConverter assignmentConverter;
    @Autowired
    private StudentMapper studentMapper;
    /**
      * @description
      * @author 张人杰
      * @date 2025/3/21 9:02
      * @Param 作业assignmentid
      * @return 操作成功或者抛出友好异常
      */

    @Override
    @Transactional(rollbackFor =Exception.class)
    public void delete(Long assignmentid) {
        EntityWrapper<AssignmentSubmit> wrapper= new EntityWrapper<>();
        Integer assignment_id = assignmentSubmitMapper.selectCount(wrapper.eq("assignment_id", assignmentid));
        if (assignment_id>0){
            throw new FriendException("有学生提交作业，不可以删除");
        }else {
            Integer integer = this.baseMapper.deleteById(assignmentid);
        }


    }
    /**
      * @description批量删除作业标题
      * @author 张人杰
      * @date 2025/3/28 9:00
      * @Param List<Long> ids
      * @return
      */

    @Override
    @Transactional(rollbackFor =Exception.class)
    public Boolean deleteBatchids(List<Long> ids) {
        EntityWrapper<AssignmentSubmit> wrapper= new EntityWrapper<>();

            Integer assignment_id = assignmentSubmitMapper.selectCount(wrapper.in("assignment_id", ids));
            if (assignment_id>0){
               return false;
            }else {
                for (Long id:ids){
                     baseMapper.deleteById(id);
                }
            }

        return null;
    }

    /**
      * @description
      * @author 张人杰
      * @date 2025/3/21 10:03
      * @Param 添加作业
      * @return 操作成功或者抛出友好异常
      */

    @Override
    @Transactional(rollbackFor =Exception.class)
    public void add(AssignmentDto assignmentDto) {
        Assignment assignment = assignmentConverter.dtoToEntity(assignmentDto);
        Integer insert = baseMapper.insert(assignment);
        if (insert<1){
            throw  new FriendException("保存失败");
        }
    }
    /**
      * @description作业的更新
      * @author 张人杰
      * @date 2025/3/28 9:04
      * @Param AssignmentDto
      * @return
      */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AssignmentDto assignmentDto) {
        Assignment assignment = assignmentConverter.editDtoToEntity(assignmentDto);
        Integer integer = baseMapper.updateById(assignment);
        if (integer<0){
            throw  new FriendException("更新失败");

        }
    }
    /**
      * @description字段校验
      * @author 张人杰
      * @date 2025/3/28 9:02
      * @Param String col字段名，String value字段值
      * @return
      */

    @Override
    public int getAssignmentSort( String col,String value) {
        EntityWrapper<Assignment> wrapper= new EntityWrapper<>();
        wrapper.eq(col,value);
       return baseMapper.selectCount(wrapper);


    }
    /**
      * @description分页/模糊查询
      * @author 张人杰
      * @date 2025/3/28 9:03
      * @Param Page<Assignment> page 分页 Wrapper<Assignment> formToEntityWrapperWithSearch查询条件
      * @return Page<Assignment>
      */

    @Override
    public Page<Assignment> getStuRewardPunishList(Page<Assignment> page, Wrapper<Assignment> formToEntityWrapperWithSearch) {
        Long userId = ShiroKit.getUserId();
        List<Assignment> stuRewardPunishList = this.baseMapper.getAssignmentList(page, formToEntityWrapperWithSearch.eq("bc.user_id",userId).orderBy("am.sort",true).eq("am.is_deleted",0));
        page.setRecords(stuRewardPunishList);
        return page;
    }

    /**
     * 通过用户编号获取学生的班级作业
     * @Author Juci
     * @param page
     * @param wrapper
     * @param userId
     * @return
     */
    @Override
    public Page<AssignmentDto> searchGradPage(Page<AssignmentDto> page, Wrapper<AssignmentDto> wrapper,Long userId) {
        Student student = studentMapper.getStudentByUserId(userId);
        if(student==null){
            throw new FriendException("非学生用户！");
        }
        List<AssignmentDto> assignmentDtos = this.baseMapper.searchGradPage(page,student.getGradeId(),student.getId(), wrapper.eq("g.is_deleted",0));
        page.setRecords(assignmentDtos);
        return page;
    }

    /**
     * 通过用户编号获取学生的小组作业
     * @Author Juci
     * @param page
     * @param wrapper
     * @param userId
     * @return
     */
    @Override
    public Page<AssignmentDto> searchGroupPage(Page<AssignmentDto> page, Wrapper<AssignmentDto> wrapper,Long userId){
        Student student = studentMapper.getStudentByUserId(userId);
        if(student==null){
            throw new FriendException("非学生用户！");
        }
        List<AssignmentDto> assignmentDtos = this.baseMapper.searchGroupPage(page,student.getId(), wrapper.eq("g.is_deleted",0));
        page.setRecords(assignmentDtos);
        return page;
    }

    /**
     * @description  提交作业人数和未评分加一
     * @author 马勇超
     * @date 2025/5/14 13:40
     * @param
     * @return
     */
    @Override
    public void addSubmitNumber(Long assignmentId) {
        this.baseMapper.addSubmitNumber(assignmentId);
    }

    /**
     * 获取当前用户该作业的提交详情
     * @author Juci
     * @param userId
     * @param id
     * @return
     */
    @Override
    public AssignmentDto getStudentAssignment(Long userId,Long id){
        Student student = studentMapper.getStudentByUserId(userId);
        if(student == null){
            throw new FriendException("该用户不是学生！");
        }
        return this.baseMapper.getStudentAssignment(student.getId(),id);
    }
}
