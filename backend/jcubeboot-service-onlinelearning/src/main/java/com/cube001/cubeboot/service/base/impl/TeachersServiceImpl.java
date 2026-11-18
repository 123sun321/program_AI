package com.cube001.cubeboot.service.base.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.base.entity.Teachers;
import com.cube001.cubeboot.community.entity.CommunityTheme;
import com.cube001.cubeboot.core.authorization.entity.User;
import com.cube001.cubeboot.core.authorization.service.UserService;
import com.cube001.cubeboot.course.entity.Course;
import com.cube001.cubeboot.course.entity.Exercise;
import com.cube001.cubeboot.service.assignment.AssignmentSubmitService;
import com.cube001.cubeboot.service.base.RoleExtendService;
import com.cube001.cubeboot.service.base.StudentService;
import com.cube001.cubeboot.service.base.TeachersService;
import com.cube001.cubeboot.service.base.dto.ImportTeacherDto;
import com.cube001.cubeboot.service.base.dto.TeachersConverter;
import com.cube001.cubeboot.service.base.dto.TeachersDto;
import com.cube001.cubeboot.service.base.dto.base.DashboardCount;
import com.cube001.cubeboot.service.base.mapper.TeacherMapper;
import com.cube001.cubeboot.service.community.CommunityThemeService;
import com.cube001.cubeboot.service.course.CourseService;
import com.cube001.cubeboot.service.course.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeachersServiceImpl extends ServiceImpl<TeacherMapper, Teachers> implements TeachersService {
    @Autowired
    private TeachersService teachersService;

    @Autowired
    private TeachersConverter converter;

    @Autowired
    private Validator defaultValidator;

    @Autowired
    private UserService userService;
    @Autowired
    private StudentService studentService;

    @Autowired
    private RoleExtendService roleExtendService;

    /**
     * @param page：当前页面；size：页面显示数目； name搜索的字段
     * @return 分页后的数据
     * @description 分页/搜索
     * @author anonymous
     * @date 2025/12/4 14:16
     */
    @Override
    public Page<Teachers> searchPage(Page page, Wrapper<Teachers> formToEntityWrapperWithSearch) {
        List<Teachers> teachersList = this.baseMapper.searchPage(page, formToEntityWrapperWithSearch.eq("a.is_deleted", 0).orderBy("a.creation_time",false));
        page.setRecords(teachersList);
        return page;
    }


    /**
     * @param importData 数据
     * @return list
     * @description 解析Excel，并查询Excel表中是否存在重复的或者数据库已存在的
     * @author anonymous
     * @date 2025/12/4 14:24
     */
    @Override
    public List<ImportTeacherDto> importDataToDo(List<Map<String, Object>> importData) {

        List<ImportTeacherDto> teacherDtos = new ArrayList<>();
        //反射获取教师类中的所有字段
        Field[] fields = TeachersDto.class.getDeclaredFields();

        //查询数据库的数据
        Wrapper<Teachers> wrapper = new EntityWrapper<>();
        wrapper = wrapper.setSqlSelect("code", "name").eq("is_deleted", false);
        List<Teachers> teachersList = baseMapper.selectList(wrapper);

        for (Map<String, Object> data : importData) {
            ImportTeacherDto teacherDto = new ImportTeacherDto();
            teacherDto.setSuccess(true);
            List<String> tips = new ArrayList<>();
            //错误字段
            List<String> errFields = createByReflect(teacherDto, fields, data);

            //验证返回列中是否存在
            //数据库重复检验
            if (teacherDto.getName() != null && teacherDto.getCode() != null) {
                Boolean exit = teacherDtos.stream().filter(b -> teacherDto.getName().equals(b.getName()) && teacherDto.getCode().equals(b.getCode())).count() > 0;
                //如果表格中重复存在则跳出继续下一个
                if (exit) {
                    continue;
                }
                //判断数据库中是否存在同一工号
                exit = teachersList.stream().filter(b -> teacherDto.getCode().equals(b.getCode())).count() > 0;
                if (exit) {
                    teacherDto.setSuccess(false);
                    tips.add("教师工号" + teacherDto.getCode() + "已存在");
                }
            }
            //判断所选院系是否存在
 //           if (teacherDto.getDepartmentName() != null) {
//                Department department = departmentService.selectOne(new EntityWrapper<Department>().eq("name", teacherDto.getDepartmentName()));
//                if (department == null) {
//                    tips.add("不存在" + teacherDto.getDepartmentName() + "院系");
//                } else {
//                    teacherDto.setDepartmentId(department.getId());
//                }
 //           }

            //字段转换失败
            if (errFields.size() > 0) {
                tips.add("字段转换失败：" + String.join(";", errFields));
                teacherDto.setSuccess(false);
            }

            //验证
            Set<ConstraintViolation<ImportTeacherDto>> validatorSet = defaultValidator.validate(teacherDto);
            if (validatorSet.size() > 0) {
                teacherDto.setSuccess(false);
                String err = String.join(";", validatorSet.stream().map(b -> {
                    return b.getMessage();
                }).collect(Collectors.joining()));
                tips.add("字段验证失败：" + err);
            }
            teacherDto.setTips(String.join(".", tips));
            teacherDtos.add(teacherDto);
        }
        return teacherDtos;
    }

    /**
     * @param teachers
     * @return Boolean
     * @description 新建教师的实现方法，同时创建新的user用户
     * @author anonymous
     * @date 2025/12/4 14:21
     */
    @Override
    public Boolean insertWithUser(Teachers teachers) {
        Boolean success = false;
        User user = new User();
        user.setName(teachers.getName());
        user.setUserType(2);
        user.setSurname(teachers.getName());
        user.setUserName(teachers.getCode());
        // 获取学校信息
        // 判断身份证号是否为空 若不为空默认密码设置身份证后六位 否则默认密码为000000
        String idnumber = teachers.getCarNo();
        if(null != idnumber && idnumber.length() > 5){
            user.setPassword(StrUtil.sub(idnumber,idnumber.length()-6,idnumber.length()));
        }
        else{
            user.setPassword("000000");
        }
        user.setPhoneNumber(teachers.getContact());
        user.setEmailAddress(teachers.getEmail());
        user.setTenantid(-1);
        user.setSex("男"==teachers.getSex()?1:0);
        user.setCreationTime(new Date());
        userService.createUser(user);
        Long userid = user.getId();
        //获得最新生产的user信息，得到userId
        teachers.setUserId(userid);
        if (teachersService.insert(teachers)) {
            // 添加教师角色
            roleExtendService.setTeacherRole(userid);
            success = true;
        }

        return success;
    }

    @Override
    public List<Teachers> getAll() {
        return baseMapper.getAll();
    }

    /**
     * @param
     * @return
     * @description 获取当前教师信息和相关院系信息
     * @author anonymous
     * @date 2025/1/4 17:01
     */
    @Override
    public TeachersDto selectWithDepartment(Long userId) {
        Teachers teachers = baseMapper.selectWithUser(userId);
        TeachersDto teachersDto = converter.entityToDto(teachers);
        Long sId = null;
        if (teachersDto != null) {
            teachersDto = converter.entityToDto(teachers);
        }
        return teachersDto;
    }

    /**
     * @param
     * @return
     * @description 创建通过反射
     * @author anonymous
     * @date 2025/11/28 9:46
     */
    private List<String> createByReflect(ImportTeacherDto teacherDto, Field[] fields, Map<String, Object> data) {
        List<String> errfileds = new ArrayList<>();

        for (Field field : fields) {
            String key = field.getName();
            Object val = data.get(key);
            if (val == null) continue;
            try {
                //取消访问权限控制
                field.setAccessible(true);
                if (field.getType().equals(Integer.class)) {
                    field.set(teacherDto, new Integer(data.get(key).toString()));
                } else {
                    field.set(teacherDto, data.get(key));
                }
            } catch (IllegalAccessException e) {
                errfileds.add(e.toString() + "填入失败");
            }
        }
        return errfileds;
    }

    @Override
    @Cacheable(value = "redis-teacher", key = "#userId")
    public Teachers getByUserId(Long userId) {
        Wrapper<Teachers> teachersWrapper = new EntityWrapper<>();
        teachersWrapper.eq("user_id", userId);
        List<Teachers> teachers = baseMapper.selectList(teachersWrapper);
        if (teachers == null || teachers.size() == 0) {
            return null;
        } else {
            return teachers.get(0);
        }

    }
    /**
     * @description  教师首页统计
     * @author anonymous
     * @date 2025/4/8 16:49
     * @param   id
     * @return   dashboardCount
     */
    @Autowired
    CourseService courseService;
    @Autowired
    AssignmentSubmitService assignmentSubmitService;
    @Autowired
    ExerciseService exerciseService;

    @Autowired
    CommunityThemeService communityThemeService;
    /**
    * @description  教师首页统计
    * @author anonymous
    * @date 2025/5/14 8:41
    * @param   id
    * @return
    */
    @Override
    public DashboardCount getDashboardCount(Long id) {
        DashboardCount dashboardCount = new DashboardCount();
        // 课程数目
        dashboardCount.setNewCourseCount(courseService.getListByUserId(id).size());
        // 未评分数目
        dashboardCount.setNoScoreCount(assignmentSubmitService.getNoScoreList().size());
        // 实验数目
        dashboardCount.setExerciseCount(exerciseService.selectList(new EntityWrapper<Exercise>().eq("user_id",id)).size());
        // 帖子数目
        dashboardCount.setThemeCount(communityThemeService.selectList(new EntityWrapper<CommunityTheme>().eq("user_id",id)).size());
        return dashboardCount;
    }

    /**
    * @description  学校管理端首页统计
    * @author anonymous
    * @date 2025/5/14 8:41
    * @param
    * @return
    */
    @Override
    public DashboardCount getSchoolDashboardCount(Long userId) {
        DashboardCount dashboardCount = new DashboardCount();
        // 课程数目
        dashboardCount.setNewCourseCount(courseService.selectCount(new EntityWrapper<>()));
        // 学校学生数目
        dashboardCount.setStudentCount(studentService.selectCount(new EntityWrapper<>()));
        // 实验数目
        dashboardCount.setExerciseCount(exerciseService.selectCount(new EntityWrapper<Exercise>().eq("user_id",userId)));
        // 帖子数目
        dashboardCount.setThemeCount(communityThemeService.selectCount(new EntityWrapper<>()));
        return dashboardCount;
    }
}
