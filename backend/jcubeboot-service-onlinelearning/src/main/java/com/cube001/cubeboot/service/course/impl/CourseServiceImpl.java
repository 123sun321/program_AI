package com.cube001.cubeboot.service.course.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.base.entity.Label;
import com.cube001.cubeboot.base.entity.LabelContent;
import com.cube001.cubeboot.base.entity.Student;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.authorization.shiro.ShiroUser;
import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.core.util.ToolUtil;
import com.cube001.cubeboot.course.entity.*;
import com.cube001.cubeboot.service.base.LabelContentService;
import com.cube001.cubeboot.service.base.LabelService;
import com.cube001.cubeboot.service.base.StudentService;
import com.cube001.cubeboot.service.base.initialization.InitialCourse;
import com.cube001.cubeboot.service.base.initialization.InitialExercise;
import com.cube001.cubeboot.service.base.initialization.Initialization;
import com.cube001.cubeboot.service.base.mapper.LabelContentMapper;
import com.cube001.cubeboot.service.course.CoursePathService;
import com.cube001.cubeboot.service.course.CourseService;
import com.cube001.cubeboot.service.course.dto.*;
import com.cube001.cubeboot.service.course.mapper.*;
import com.cube001.cubeboot.service.training.dto.CodeConverter;
import com.cube001.cubeboot.service.training.dto.CodeDto;
import com.cube001.cubeboot.service.training.mapper.CodeMapper;
import com.cube001.cubeboot.training.entity.Code;
import com.cube001.cubeboot.training.entity.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;


/*
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: CourseServiceImpl
 * 班课service实现类
 *
 * @author anonymous
 * @date 2025/11/28 9:06
 * @version 1.0
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    private Validator defaultValidator;
    @Autowired
    private CourseConverter courseConverter;
    @Autowired
    private LabelContentMapper labelContentMapper;
    @Autowired
    private LabelService labelService;
    @Autowired
    private LabelContentService labelContentService;
    @Autowired
    private StudentService studentService;

    @Autowired
    private CodeMapper codeMapper;
    @Autowired
    private CodeConverter codeConverter;
    @Autowired
    private ExerciseMapper exerciseMapper;
    @Autowired
    private ExerciseConverter exerciseConverter;

    @Autowired
    private ExerciseResourceMapper exerciseResourceMapper;
    @Autowired
    private ExerciseResourceConverter exerciseResourceConverter;
    @Autowired
    private CoursePathMapper coursePathMapper;
    @Autowired
    private CoursePathConverter coursePathConverter;
    /**
     * @description 根据课程id获取该课程总收藏数量
     * @author anonymous
     * @date 2025/7/5 11:01
     * @param
     * @return
     */
    @Override
    public Integer getCollectedNumberByCourseId(Long courseId) {
        return baseMapper.getCollectedNumberByCourseId(courseId);
    }
    /**
     * @description  判断当前课程是否被收藏了
     * @author anonymous
     * @date 2025/7/5 9:51
     * @param
     * @return
     */
    @Override
    public Boolean judgeCourseIsCollected(Long courseId) {
        Long userId = ShiroKit.getUserId();
        return baseMapper.judgeCourseIsCollected(userId,courseId)>0;
    }
    /**
     * @description  更新参与人数
     * @author anonymous
     * @date 2025/7/5 9:51
     * @param
     * @return
     */
    @Override
    public Boolean updateJoinNumber(Long id) {
        return baseMapper.updateJoinNumber(id);
    }

    /**
     * @description 学生端课程 相关课程推荐的相关处理及逻辑的实现类,同时对重名的课程去重
     * @author anonymous
     * @date 2025/4/27 12:00
     * @param courseId 课程Id
     * @param labelList 标签列表
     * @return
     */
    @Override
    public List<Course> courseRecommend(Long courseId,List<Label> labelList) {
        /** 根据标签id,推荐相关的课程的link_id,再查询出对应的courseName  */
        List<Course> courseList = new ArrayList<>(8);
        //控制数量
        for (Label label: labelList){
            courseList.addAll( getCourseListByLabelId(label.getId()));
            //把自己去掉
            for (int i=0;i<courseList.size();i++){
                Course course = courseList.get(i);
                if(course.getId().equals(courseId)){
                    courseList.remove(i);
                }
            }
            // 去掉重名，出现重复，去掉后者即可
            for (int i=0;i<courseList.size();i++){
                for (int j=courseList.size()-1;j>i;j--){
                    if(courseList.get(i).getName().equals( courseList.get(j).getName() )){
                        courseList.remove(j);
                    }
                }
            }
            if(courseList.size()>=6){
                //只向前台发送五个即可
                courseList=courseList.subList(0,6);
                break;
            }
        }
        //添加收藏字段
        Long userId = ShiroKit.getUserId();
        for (int i=0;i<courseList.size();i++){
            Course course = courseList.get(i);
            course.setCourseCollect(new CourseCollect().setCourseId(baseMapper.judgeCourseIsCollected(userId, course.getId())));
        }
        return courseList;
    }

    /**
     * @description 为每个课程实体添加标签列表
     * @author anonymous
     * @date 2025/4/23 17:58
     * @param page 分页结果
     * @return
     */
    @Override
    public Page<Course> setLabelsToPage(Page<Course> page) {
        List<Course> records = page.getRecords();
        for(Course course : records){
            //课程类型（0-公开课1-教师课程), 仅公开课程可以设置标签
            if(course.getType()==1){
                continue;
            }
            List<Label> labels = labelService.selectListByCourseId(course.getId());
            List<String> list = new ArrayList<>(4);
            for(Label label : labels){
                //顶多每个课程添加4个标签
                if(list.size()<4){
                    list.add(label.getName());
                }
            }
            course.setTags(list);
        }
        page.setRecords(records);
        return page;
    }

    /**
     * @description 根据标签id,查询相关的课程的link_id,再查询出对应的course实体
     * @author anonymous
     * @date 2025/4/22 16:08
     * @param
     * @return
     */
    @Override
    public List<Course> getCourseListByLabelId(Long labelId) {
        return baseMapper.getCourseListByLabelId(labelId);
    }

    /**
     * @description 设置学生端的课程页面的模糊查询的wrapper
     * @author anonymous
     * @date 2025/4/22 9:28
     * @param searchDto
     * @return EntityWrapper<Course>
     */
    @Override
    public EntityWrapper<Course> setWrapper(CommonSearchDto<Course> searchDto) {
        EntityWrapper<Course> wrapper = new EntityWrapper<>();

        if(!ToolUtil.isEmpty(searchDto.getForm().get("searchContent"))) {
            wrapper.like("t.name","%" + searchDto.getForm().get("searchContent") + "%");
        }else{
            wrapper.like("t.name", "%");
        }
        if(!ToolUtil.isEmpty(searchDto.getForm().get("isPublic"))) {
            wrapper.eq("t.type", searchDto.getForm().get("isPublic"));
        }
        /**技术类型*/
        if(!ToolUtil.isEmpty(searchDto.getForm().get("techType"))){
            wrapper.like("t.category","%"+searchDto.getForm().get("techType")+"%");
        }else {
            wrapper.like("t.category","%");
        }
        if(!ToolUtil.isEmpty(searchDto.getForm().get("difficultLevel"))){
            Integer difficultLevel =  (Integer) searchDto.getForm().get("difficultLevel");
            if(difficultLevel > 0){
                wrapper.eq("t.complexity",difficultLevel);
            }
        }
        /** 实现是否参加进当前课程
         *      <if test="joinCategory != null and joinCategory = 1"> and j.`student_id` is not null</if>
         *       <if test="joinCategory != null and joinCategory = 0"> and j.`student_id` is  null</if>
         */
        if(!ToolUtil.isEmpty(searchDto.getForm().get("joinCategory"))){
            Integer joinCategory =  (Integer) searchDto.getForm().get("joinCategory");
            if(joinCategory ==1 ){
                wrapper.isNotNull("j.student_id");
            }else{
                wrapper.isNull("j.student_id");
            }
        }
        /**学生端设置课程标签查询的参数  <if test="tagName != null">label.name like #{tagName} </if>*/
        if(!ToolUtil.isEmpty(searchDto.getForm().get("tag"))) {
            //按标签进行查找
            String tagName = searchDto.getForm().get("tag").toString();
            if(tagName.equals("全部")){
                tagName=null;
            }else  if(tagName!=null){
                wrapper.like("label.name", "%"+tagName+"%");
            }
        }
        /**区分是基础课程 /进阶课程 / 还是收藏课程  */
        if(!ToolUtil.isEmpty(searchDto.getForm().get("courseChooseType"))){

            String courseChooseType  =(String) searchDto.getForm().get("courseChooseType");
            if(courseChooseType.equals("myMark")){
                wrapper.ne("c.course_id", 0);
            }
        }
        /**设置排序的orderBy 的wrapper*/
        if(!ToolUtil.isEmpty(searchDto.getForm().get("orderBy"))){
            String orderBy = (String)searchDto.getForm().get("orderBy");
            Boolean isAsc = searchDto.getForm().get("isAsc") .equals("1");
            if(orderBy.equals("default")) {
                wrapper.orderBy("t.join_number", false).orderBy("t.`creation_time`", false);
            }else if(orderBy.equals("complexity")){
                orderBy = "t.complexity";
            }else if(orderBy .equals("joinNumber")){
                orderBy = "t.join_number";
            }else if("creationTime".equals(orderBy)){
                orderBy = "t.creation_time";
            }else if(orderBy.equals("recommendDegree")){
                orderBy = "t.recommend_degree";
            }
                //加上前缀和转为蛇形, 不是排
            if(!orderBy.equals("default")){
                wrapper.orderBy(orderBy, isAsc);
            }

            if(!orderBy.equals("t.join_number")){
                wrapper.orderBy("t.join_number", false);
            }else{
                wrapper.orderBy("t.recommend_degree", false);
            }
        }else{
            //为空按默认值
            wrapper.orderBy("t.join_number", false).orderBy("t.`creation_time`", false);
        }
        return wrapper;
    }

    /**
     * @description 学生端设置课程查询的参数 的实现类
     * @author anonymous
     * @date 2025/4/19 9:56
     * @param  plusPage 分页的实体
     * @return Page<PageDto>
     */
    @Override
    public Page<Course> stuWebCoursePage(Page<Course> plusPage,Wrapper<Course> wrapper,Long userId,String tagName,Integer joinCategory) {

        Long studentId = studentService.selectOne(new EntityWrapper<Student>().eq("user_id", userId)).getId();

        List<Course> pages;
        if(tagName==null) {
             pages = baseMapper.getCourseListWithoutTagsSearch(plusPage, wrapper, userId, studentId, joinCategory);
        }else{
            pages = baseMapper.getCourseListWithTagsSearch(plusPage, wrapper, userId, tagName, studentId, joinCategory);
        }
        List<Course> pagesAddLabels = new ArrayList<>(8);
        //设置每个课程实体的全部标签,然后添加进pagesAddLabels列表中
        for(Course course : pages){
            List<Label> labels = labelService.selectListByCourseId(course.getId());
            Set<String> set = new HashSet<>(8);
            for(Label label : labels){
                set.add(label.getName());
            }
            //设置标签
            course.setTags(new ArrayList<>(set));
            pagesAddLabels.add(course);
        }

        return  plusPage.setRecords(pagesAddLabels);
    }

    /**
     * @description 根据id查询课程信息
     * @author anonymous
     * @date  2019年3月18日 16:02:15
     * @param
     * @return
     */
    @Override
    public CourseDto get(Long courseId) {
      CourseDto courseDto = new CourseDto();
        //获取基本信息
      Course course = baseMapper.selectById(courseId);
      if(null != course){
          courseDto = courseConverter.entityToDto(course);
          // 获取标签
          courseDto.setTags(labelContentService.getNameByLinkId(courseId));
      }
        return courseDto;
    }

    /**
     * 导入数据转换成Do
     *
     * @param importData
     * @return
     */
    @Override
    public List<ImportCourseDto> importDataToListDo(List<Map<String, Object>> importData) {

        List<ImportCourseDto> courseDtos = new ArrayList<>();
        Field[] fields = CourseDto.class.getDeclaredFields();

        Wrapper<Course> wrapper = new EntityWrapper<>();
        //wrapper = wrapper.setSqlSelect("name", "code").eq("isDeleted", false);
        wrapper = wrapper.setSqlSelect("name");
        List<Course> courses = baseMapper.selectList(wrapper);

        for (Map<String, Object> data : importData) {

            ImportCourseDto courseDto = new ImportCourseDto();
            courseDto.setSuccess(true);
            List<String> tips = new ArrayList<>();
            // 错误字段
            List<String> errFields = createByReflect(courseDto, fields, data);
            //验证返回列中是否存在
            //数据库重复检验
            if (courseDto.getName() != null) {
                Boolean exit = courseDtos.stream().filter(b -> courseDto.getName().equals(b.getName())).count() > 0;
                if (exit) {
                    continue;
                } //存在直接跳出
                exit = courses.stream().filter(b -> courseDto.getName().equals(b.getName())).count() > 0;
                if (exit) {
                    courseDto.setSuccess(false);
                    tips.add("课程名称" + courseDto.getName() + "已存在");
                }
            }
    /*          if (courseDto.getCode() != null) {
                Boolean exit = courseDtos.stream().filter(b -> courseDto.getCode().equals(b.getCode())).count() > 0;
                if (exit) {
                    continue;
                } //存在直接跳出
                exit = courses.stream().filter(b -> courseDto.getCode().equals(b.getCode())).count() > 0;
                if (exit) {
                    courseDto.setSuccess(false);
                    tips.add("课程代码" + courseDto.getCode() + "已存在");
                }
            }*/
            //字段转换失败
            if (errFields.size() > 0) {
                tips.add("字段转换失败:" + String.join(";", errFields));
                courseDto.setSuccess(false);
            }
            //验证
            Set<ConstraintViolation<ImportCourseDto>> violationSet = defaultValidator.validate(courseDto); //验证
            if (violationSet.size() > 0) {
                courseDto.setSuccess(false);
                String err = String.join(";", violationSet.stream().map(b -> {
                    return b.getMessage();
                }).collect(Collectors.joining()));
                tips.add("字段验证失败:" + err);

            }
            courseDto.setTips(String.join(".", tips));
            courseDtos.add(courseDto);
        }

        return courseDtos;
    }


    /**
     * 批量插入
     *
     * @param courses
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addList(List<CourseDto> courses) {

        List<Course> courseList = courseConverter.listDtoToEntity(courses);
        //获取当前用户的id
        Long userId = ShiroKit.getUserId();
        for (Course course : courseList) {
            course.setUserId(userId);
            for (CourseDto courseDto : courses) {
                //保存标签
                SaveCourseTags(courseDto.getTags(),course.getId());
            }
        }
       // Long tenantId = Long.parseLong(TenantIdUtil.getTenantId());
       /* for (Course course : courseList) {
            course.setSchoolId(tenantId);
        }*/
        Boolean res = super.insertBatch(courseList);
       /* List<Long> ids = courseList.stream().map(b -> b.getId()).collect(Collectors.toList());
        buildAbility(ids, TenantIdUtil.getTenantId());*/
        return res;
    }

    /**
     * @description 添加课程
     * @author anonymous
     * @date 2025年3月15日 09:51:08
     * @param courseDto
     * @return
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean add(CourseDto courseDto) {
        Course course = courseConverter.dtoToEntity(courseDto);
        //获取当前登录用户的id
        Long userId = ShiroKit.getUserId();
        course.setUserId(userId);
        Boolean suc = this.insert(course);
        //保存标签
        SaveCourseTags(courseDto.getTags(),course.getId());
        return suc;
    }
    /**
     * @description 修改课程
     * @author anonymous
     * @date 2025年3月15日 09:51:08
     * @param courseDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(CourseDto courseDto) {
        //修改课程信息
        Course course = courseConverter.dtoToEntity(courseDto);
        //保存标签
        SaveCourseTags(courseDto.getTags(),course.getId());
        Boolean suc = this.updateById(course);
        return suc;
    }

    /**
     * 保存实验标签
     * @param tags
     * @param courseId
     */
    private void SaveCourseTags(List<String> tags,Long courseId){
        // 删除已有的标签
        Wrapper<LabelContent> wrapper = new EntityWrapper<>();
        wrapper.eq("link_id",courseId);
        labelContentMapper.delete(wrapper);
        for (String tag : tags) {
            Label label = this.labelService.getByName(tag);
            if(null == label){
                throw new FriendException("创建标签失败！");
            }
            LabelContent labelContent = new LabelContent();
            labelContent.setLabelId(label.getId());
            labelContent.setType(0);
            labelContent.setLinkId(courseId);
            if(labelContentMapper.insert(labelContent)==0){
                throw new FriendException("实验添加标签失败！");
            }
        }
    }
    /**
     * @description 获取当前用户下的课程信息
     * @author anonymous
     * @date   2019年3月14日 11:06:50
     * @param
     * @return
     */
    @Override
    public List<CourseDto> getListByUserId(Long userId) {
        List<Course> courseList =  baseMapper.selectList(new EntityWrapper<Course>().eq("user_id",userId));
        List<CourseDto> courseDtoList = courseConverter.listEntityToDto(courseList);
       return courseDtoList;
    }
    /**
     * @description 获取所有课程类型列表
     * @author anonymous
     * @date  2019年3月15日 11:28:11
     * @param
     * @return
     */
    @Override
    public List<Course> getCategoryList() {
        List<Course> list = this.baseMapper.getCategoryList();
        return list;
    }
    /**
     * @description 获取课程难易程度列表
     * @author anonymous
     * @date 2025年3月15日 13:43:01
     * @param
     * @return
     */
    @Override
    public List<Course> getComplexityList() {
        List<Course> list = baseMapper.getComplexityList();
        return list;
    }
    /**
     * @description 批量导入课程
     * @author anonymous
     * @date 2025/4/8 16:49
     * @Param List<Long> ids Long userId
     * @return
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long batchImport(List<Long> ids,Long userId,String coursecode) {
        List<Course> courses = baseMapper.selectBatchIds(ids);
        ShiroUser user = ShiroKit.getUser();
        for (Course course:courses) {
            //判断是否重复导入课程
//            Wrapper<Course> courseWrapper = new EntityWrapper<>();
//            courseWrapper.eq("name", course.getName()).eq("user_id", userId);
//            Integer integer = baseMapper.selectCount(courseWrapper);
//            if (integer > 0) {
//                throw new FriendException("不可导入相同的数据");
//            } else {


            //判断实验个数
            if (course.getExerciseNumber() > 0) {
                //通过课程ID查看对应的实验

                EntityWrapper<Exercise> exerciseEntityWrapper = new EntityWrapper<>();
                exerciseEntityWrapper.eq("course_id", course.getId());
                List<Exercise> exercises = exerciseMapper.selectList(exerciseEntityWrapper);
                //通过课程ID查询课程对应的标签
                EntityWrapper<LabelContent> labelContentEntityWrapper = new EntityWrapper<>();
                labelContentEntityWrapper.eq("link_id", course.getId());
                List<LabelContent> labelContents = labelContentMapper.selectList(labelContentEntityWrapper);
                course.setUserId(userId);
                course.setId(null);
                course.setType(1);
                course.setAuthor(user.getName());
                course.setCreationTime(null);
                course.setTenantId(null);
                course.setJoinNumber(0);
                course.setCommentNumber(0);
                course.setCode(coursecode);
                course.setLastModificationTime(null);
                course.setLastModefierUserId(null);
                baseMapper.insert(course);
                if (labelContents != null) {
                    for (LabelContent labelContent : labelContents) {
                        labelContent.setLinkId(course.getId());
                        labelContent.setType(0);
                        labelContent.setId(null);
                        labelContent.setCreationTime(null);
                        labelContent.setTenantId(null);
                        labelContentMapper.insert(labelContent);
                    }
                }
                for (Exercise E : exercises) {
                    //查询实验下的标签
                    EntityWrapper<LabelContent> labelContentEntityWrapper1 = new EntityWrapper<>();
                    labelContentEntityWrapper1.eq("link_id", E.getId());
                    List<LabelContent> labelContents1 = labelContentMapper.selectList(labelContentEntityWrapper1);
                    //查询实验对应的资源
                    EntityWrapper<ExerciseResource> exerciseResourceEntityWrapper=new EntityWrapper<>();
                    exerciseResourceEntityWrapper.eq("exercise_id",E.getId());
                    List<ExerciseResource> exerciseResources = exerciseResourceMapper.selectList(exerciseResourceEntityWrapper);
                    //判断实验是否有初始化数据
                    if (E.getHaveCode()) {
                        Code code = codeMapper.selectById(E.getCodeId());
                        code.setId(null);
                        code.setCreationTime(null);
                        code.setTenantId(null);
                        codeMapper.insert(code);
                        E.setCodeId(code.getId());
                    }
                    E.setUserId(userId);
                    E.setId(null);
                    E.setCourseId(course.getId());
                    E.setTenantId(null);
                    E.setCreationTime(null);
                    exerciseMapper.insert(E);
                    if (labelContents1 != null) {
                        for (LabelContent labelContent : labelContents1) {
                            labelContent.setLinkId(E.getId());
                            labelContent.setType(1);
                            labelContent.setId(null);
                            labelContent.setCreationTime(null);
                            labelContent.setTenantId(null);
                            labelContentMapper.insert(labelContent);
                        }
                    }
                    if (exerciseResources!=null){
                        for (ExerciseResource resource: exerciseResources
                             ) {
                            resource.setExerciseId(E.getId());
                            resource.setId(null);
                            resource.setCreationTime(null);
                            resource.setTenantId(null);
                            exerciseResourceMapper.insert(resource);

                        }
                    }
                }
                return course.getId();
            } else {
                EntityWrapper<LabelContent> labelContentEntityWrapper = new EntityWrapper<>();
                labelContentEntityWrapper.eq("link_id", course.getId());
                List<LabelContent> labelContents = labelContentMapper.selectList(labelContentEntityWrapper);
                course.setUserId(userId);
                course.setId(null);
                course.setType(1);
                course.setCreationTime(null);
                course.setAuthor(user.getName());
                course.setTenantId(null);
                course.setJoinNumber(0);
                course.setCommentNumber(0);
                course.setCode(coursecode);
                course.setLastModificationTime(null);
                course.setLastModefierUserId(null);
                baseMapper.insert(course);
                if (labelContents != null) {
                    for (LabelContent labelContent : labelContents) {
                        labelContent.setLinkId(course.getId());
                        labelContent.setId(null);
                        labelContent.setType(0);
                        labelContent.setCreationTime(null);
                        labelContent.setTenantId(null);
                        labelContentMapper.insert(labelContent);
                    }
                }
                return course.getId();
            }
//          }
        }
        return null;
    }

    @Override
    public List<Course> getCourseType() {
        List<Course> courseType = baseMapper.getCourseType();
        return courseType;
    }

    @Override
    public boolean deleteBatchids(List<Long> ids) {
        Wrapper<Exercise> courseWrapper=new EntityWrapper<>();
        Integer id = exerciseMapper.selectCount(courseWrapper.in("course_id", ids));
        if (id>0){
            return false;
        }else {
                baseMapper.deleteBatchIds(ids);
        }
        return  true;

    }

    /**
     * 创建通过反射
     *
     * @param courseDto
     * @param fields
     * @param data
     * @return
     */

    private List<String> createByReflect(ImportCourseDto courseDto, Field[] fields, Map<String, Object> data) {
        List<String> errfileds = new ArrayList<>();

        for (Field field : fields) {
            String key = field.getName();
            Object val = data.get(key);
            if (val == null) {
                continue;
            }
            try {
                //取消访问权限控制
                field.setAccessible(true);
                if (field.getType().equals(Integer.class)) {
                    field.set(courseDto, new Integer(data.get(key).toString()));
                }else if (field.getType().equals(String.class)){
                    field.set(courseDto, data.get(key).toString());
                }  else{
                    field.set(courseDto, data.get(key));
                }
            } catch (IllegalAccessException e) {
                errfileds.add(e.toString() + "填入失败");
            }
        }
        return errfileds;
    }

    private void buildAbility(Long  courseId,String tenantId){
        List<Long> ids=new ArrayList<>();
        ids.add(courseId);
        this.buildAbility(ids,tenantId);
    }

    /**
     * 创建课程关联
     *
     * @param
     * @return void
     * @description
     * @author anonymous
     * @date 2025/12/26 10:07
     */
    private void buildAbility(List<Long> courseIds, String tenantId) {

//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Wrapper<AbilityCate> wrapper = new EntityWrapper<>();
//                List<AbilityCate> abilityCates = abilityCateService.selectList(wrapper.eq("system", true));
//                List<CourseAbility> courseAbilityList = new ArrayList<>();
//                for (Long courseId : courseIds) {
//                    for (AbilityCate abilityCate : abilityCates) {
//                        CourseAbility courseAbility = new CourseAbility();
//                        courseAbility.setAbilityId(abilityCate.getId());
//                        courseAbility.setCourseId(courseId);
//                        courseAbilityList.add(courseAbility);
//                    }
//                    try (TenantFliter tenantFliter = new TenantFliter(true, tenantId)) {
//                        courseAbilityService.insertBatch(courseAbilityList);
//                    }
//                    courseAbilityList.clear();
//                }
//
//            }
//        });
//        thread.start();

    }

    /**
      * @description 初始化数据
      * @author anonymous
      * @date 2025/8/8 11:19
      * @return
     */
    @Override
    public Initialization getInitialization() {
        // 初始化总数据
        Initialization initialization = new Initialization();
        // 初始化课程
        List<InitialCourse> initialCourseList = new ArrayList<>();
        // 初始化实验
        List<InitialExercise> initialExerciseList = new ArrayList<>();
        List<Course> courseList = this.baseMapper.selectList(new EntityWrapper<Course>().eq("type",0));
        for (Course c:courseList) {
            InitialCourse initialCourse = new InitialCourse();
            // 添加课程数据; 创建日期置为空
            c.setCreationTime(null);
            initialCourse.setCourse(courseConverter.entityToDto(c));
            // 获取所有标签
            List<LabelContent> labelContents = labelContentMapper.selectList(new EntityWrapper<LabelContent>().eq("link_id",c.getId()));
            List<Long> ids = labelContents.stream().map(m -> m.getLabelId()).collect(Collectors.toList());
            List<Label> labels = labelService.selectList(new EntityWrapper<Label>().in("id",ids));
            List<String> tags = labels.stream().map(m->m.getName()).collect(Collectors.toList());
            initialCourse.setCourseTags(tags);
            // 获取课程下的所有实验数据
            if(null!=getInitialExercise(c.getId())){
                initialCourse.setExerciseList(getInitialExercise(c.getId()));
            }
            // 加入 课程数组中
            initialCourseList.add(initialCourse);
        }
        // 获取所有实验数据
        initialExerciseList = getInitialExercise(null);
        // 获取学习的路径
        List<CoursePathDto> coursePath = coursePathConverter.listToDto(coursePathMapper.selectWithCourseName());
        initialization.setInitialCourseList(initialCourseList);
        initialization.setInitialExerciseList(initialExerciseList);
        initialization.setCoursePath(coursePath);
        return initialization;
    }

    /**
      * @description 实验导出方式
      * @author anonymous
      * @date 2025/8/8 14:04
      * @param courseId
      * @return
     */
    public List<InitialExercise> getInitialExercise(@RequestParam(value = "courseId",required = false) Long courseId){
        List<InitialExercise> initialExerciseList = new ArrayList<>();
        Wrapper<Exercise> wrapper = new EntityWrapper<>();
        if(null != courseId){
            wrapper.eq("course_id",courseId).eq("type",0);
        }else{
            wrapper.eq("type",1);
        }
        List<Exercise> exerciseList = exerciseMapper.selectList(wrapper);
        for (Exercise e:exerciseList) {
            // 初始化 导出实验格式
            InitialExercise initialExercise = new InitialExercise();
            e.setCreationTime(null);
            initialExercise.setExerciseDto(exerciseConverter.entityToDto(e));
            // 获取所有标签名字
            List<LabelContent> labelContents = labelContentMapper.selectList(new EntityWrapper<LabelContent>().eq("link_id",e.getId()));
            List<Long> ids = labelContents.stream().map(m -> m.getLabelId()).collect(Collectors.toList());
            List<Label> labels = labelService.selectList(new EntityWrapper<Label>().in("id",ids));
            List<String> tags = labels.stream().map(m->m.getName()).collect(Collectors.toList());
            initialExercise.setExerciseTags(tags);
            // 判断是否有初始数据
            if(null!=e.getCodeId()){
                CodeDto code = codeConverter.entityToDto(codeMapper.selectById(e.getCodeId()));
                initialExercise.setCode(code);
            }
            // 获取实验资源
            List<ExerciseResourceDto> resourceDtoList = exerciseResourceConverter.listEntityToDto(exerciseResourceMapper
                    .selectList(new EntityWrapper<ExerciseResource>().eq("exercise_id",e.getId())));
            for(ExerciseResourceDto resource:resourceDtoList){
                resource.setCreationTime(null);
            }
            initialExercise.setResourceList(resourceDtoList);
            initialExerciseList.add(initialExercise);
        }
        return initialExerciseList;
    }

    /**
      * @description 初始化数据上传
      * @author anonymous
      * @date 2025/8/9 11:22
      * @param initialization
      * @return
     */
    @Override
    public void uploadInitialization(Initialization initialization) {
        // 判断课程信息是否为空
       if(null != initialization.getInitialCourseList()){
           // 遍历课程信息
           for (InitialCourse initialCourse:initialization.getInitialCourseList()) {
               // 获取单个课程信息
               Course course = courseConverter.dtoToEntity(initialCourse.getCourse());
               course.setId(null);
               course.setUserId(ShiroKit.getUserId());
               if(this.baseMapper.insert(course)<= 0){
                   throw new FriendException("课程信息保存出错！");
               }else {
                   // 保存标签
                   List<Long> labels = uploadLabels(initialCourse.getCourseTags());
                   // 内容标签库
                   for (Long labelId:labels) {
                       uploadLabelContent(course.getId(),labelId,0);
                   }
                   // 保存实验内容
                   if(null!=initialCourse.getExerciseList()){
                       uploadExercise(initialCourse.getExerciseList());
                   }
               }
           }
       }
       // 判断实验数据是否为空；进行上传
       if(null != initialization.getInitialExerciseList()){
           uploadExercise(initialization.getInitialExerciseList());
       }
       // 判断课程路径是否为空
       if(null!=initialization.getCoursePath()){
           List<CoursePath> coursePathList = coursePathConverter.listDtoToEntity(initialization.getCoursePath());
           for (CoursePath coursePath:coursePathList) {
               Course course = this.selectOne(new EntityWrapper<Course>().eq("name",coursePath.getCourse().getName()));
               if(null != course){
                   coursePath.setCourseId(course.getId());
                   coursePath.setId(null);
                   if(coursePathMapper.insert(coursePath) <= 0){
                       throw new FriendException("课程路径保存出错！");
                   }
               }
           }
       }
    }

    /**
      * @description 实验保存
      * @author anonymous
      * @date 2025/8/9 13:46
      * @param initialExerciseList
      * @return
     */
    public void uploadExercise(List<InitialExercise> initialExerciseList){
        if(null!=initialExerciseList){
            for (InitialExercise initialExercise:initialExerciseList) {
                Exercise exercise = exerciseConverter.dtoToEntity(initialExercise.getExerciseDto());
                exercise.setId(null);
                exercise.setUserId(ShiroKit.getUserId());
                if(exerciseMapper.insert(exercise)>0){
                    // 保存标签
                    List<Long> ids = uploadLabels(initialExercise.getExerciseTags());
                    // 保存内容标签数据
                    for (Long labelId:ids) {
                        uploadLabelContent(exercise.getId(),labelId,1);
                    }
                    // 保存代码
                    if(null != initialExercise.getCode()){
                        Code code = codeConverter.dtoToEntity(initialExercise.getCode());
                        code.setId(null);
                        if(codeMapper.insert(code)>0){
                            exercise.setCodeId(code.getId());
                        }else{
                            throw new FriendException("代码保存出错！");
                        }
                    }
                    // 保存实验资源
                    List<ExerciseResourceDto> exerciseResourceDtoList = initialExercise.getResourceList();
                    if(exerciseResourceDtoList.size() > 0){
                        for (ExerciseResourceDto resource:exerciseResourceDtoList) {
                            ExerciseResource exerciseResource = exerciseResourceConverter.dtoToEntity(resource);
                            exerciseResource.setId(null);
                            exerciseResource.setExerciseId(exercise.getId());
                            exerciseResource.setName(exercise.getName());
                            if(exerciseResourceMapper.insert(exerciseResource) <= 0){
                                throw new FriendException("实验资源保存出错！");
                            }
                        }
                    }
                }else {
                    throw new FriendException("实验数据保存出错");
                }
            }
        }
    }

    /**
      * @description 上传标签数据保存到数据库
      * @author anonymous
      * @date 2025/8/9 13:14
      * @param labels
      * @return
     */
    public List<Long> uploadLabels(List<String> labels){
        List<Long> ids = new ArrayList<>();
        for (String s:labels){
            // 判断数据库是否存在该标签
            Label labelEixt = labelService.getByName(s);
            if(null == labelEixt){
                throw new FriendException("标签保存出错!");
            }else {
                ids.add(labelEixt.getId());
            }
        }
        return ids;
    }

    /**
      * @description 相关课程或试验保存到标签相关库内
      * @author anonymous
      * @date 2025/8/9 13:25
      * @param linkId,labelId
      * @return
     */
    public void uploadLabelContent(Long linkId,Long labelId,Integer type){
        LabelContent labelContent = new LabelContent();
        labelContent.setLabelId(labelId);
        labelContent.setLinkId(linkId);
        labelContent.setType(type);
        labelContent.setSort(1);
        if(labelContentMapper.insert(labelContent) <= 0){
            throw new FriendException("内容标签库保存失败！");
        }
    }

}
