package com.cube001.cubeboot.course.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;

import com.cube001.cubeboot.base.controller.util.ExcelUtil;
import com.cube001.cubeboot.base.controller.util.ExportExcel;
import com.cube001.cubeboot.base.controller.util.ZipUtils;
import com.cube001.cubeboot.base.entity.Label;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.authorization.shiro.ShiroUser;
import com.cube001.cubeboot.core.config.properties.CubeBootProperties;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.dto.CommonSearchDto;

import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.core.util.ToolUtil;
import com.cube001.cubeboot.core.validateGroup.EditChecks;
import com.cube001.cubeboot.course.entity.Course;

import com.cube001.cubeboot.file.entity.FileResource;
import com.cube001.cubeboot.service.base.LabelService;
import com.cube001.cubeboot.service.base.initialization.Initialization;
import com.cube001.cubeboot.service.course.CourseService;
import com.cube001.cubeboot.service.course.dto.CourseConverter;
import com.cube001.cubeboot.service.course.dto.CourseDto;
import com.cube001.cubeboot.service.course.dto.ImportCourseDto;
import com.cube001.cubeboot.service.course.dto.ListCourseDto;
import com.cube001.cubeboot.service.file.FileResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import net.sf.json.JSONArray;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.groups.Default;
import java.io.*;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Enumeration;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

/**
* Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
* FileName:  CourseController
* 课程controller
*
* @author 张浩杰
* @date   2019/3/11 15:28
* @version 1.0
*/
@Api(value = "course controller", description = "课程 controller")
@RestController
@RequestMapping(value = "${cubeboot.app-manager-path}/course")
@RequiresPermissions("course")
public class CourseController implements IApplicationService {

    @Autowired
    private CubeBootProperties properties;
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseConverter courseConverter;
    @Autowired
    private LabelService labelService;
    @Autowired
    private FileResourceService fileResourceService;

    /**
     * 导出模板
     */
    private final static String templatePath = "/importTemplate/courseImportTemplate.xlsx";

    /**
     * @description 学校课程添加
     * @author 张浩杰
     * @date   2019年3月13日 11:00:21
     * @param courseDto
     * @return res
     */
    @ApiOperation(value = "插入")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @RequiresPermissions("course:add")
    public Boolean add(@RequestBody @Validated CourseDto courseDto) {

        boolean res = courseService.add(courseDto);
        return res;
    }
    /**
     * @description 课程批量添加
     * @author 张浩杰
     * @date   2019年3月13日 11:01:21
     * @param
     * @return res
     */
    @ApiOperation(value = "批量插入数据")
    @RequestMapping(value = "multipleAdd", method = RequestMethod.POST)
    @RequiresPermissions("course:add")
    public Boolean multipleAdd(@RequestBody @Validated ListCourseDto courseDtos) {
        Boolean res = courseService.addList(courseDtos.getData());
        return res;

    }
    /**
     * @description 学校端课程分页
     * @author 张浩杰
     * @date   2019年3月13日 11:01:21
     * @param
     * @return page
     */
    @ApiOperation(value = "学校端课程分页")
    @RequestMapping(value = "page", method = RequestMethod.POST)
    @RequiresPermissions("course:schoolPage")
    public Page<CourseDto> page(@RequestBody CommonSearchDto<Course> searchDto) {
        Page<Course> page = searchDto.getPlusPage();
        Wrapper<Course> courseWrapper = searchDto.formToEntityWrapper(null);
        page = courseService.selectPage(page, courseWrapper);
        return courseConverter.pageEntityToDto(page);
    }

    /**
     * @description 获取当前用户下的课程信息
     * @author 张浩杰
     * @date   2019年3月14日 11:06:50
     * @param
     * @return
    */
    @ApiOperation(value = "获取当前用户下的课程信息")
    @GetMapping(value = "/getNow")
    public List<CourseDto> now() {
        //获取当前登录用户ID
        Long userId = ShiroKit.getUserId();
        List<CourseDto> courseDtoList = courseService.getListByUserId(userId);
        return courseDtoList;
    }
    /**
     * @description 获取课程类别列表
     * @author 张浩杰
     * @date   2019年3月15日 11:28:27
     * @param
     * @return List<CourseDto>
    */
   @ApiOperation(value="获取课程类别列表", notes="获取课程类别列表")
   @GetMapping(value = "/categoryList")
   @ResponseBody
   public List<CourseDto> getCategoryList(){
        List<Course> courseList = courseService.getCategoryList();
        return courseConverter.listEntityToDto(courseList);
   }
   /**
    * @description 获取课程难易程度列表
    * @author 张浩杰
    * @date 2025年3月15日 13:43:01
    * @param
    * @return
   */
   @ApiOperation(value="获取课程难易程度列表", notes="获取课程难易程度列表")
   @GetMapping(value = "/complexityList")
   @ResponseBody
   public List<CourseDto> getComplexityList(){
       List<Course> courseList = courseService.getComplexityList();
       return courseConverter.listEntityToDto(courseList);
   }

    /**
     * @description 获取课程列表
     * @author 张浩杰
     * @date 2025年3月15日 13:43:01
     * @param
     * @return
     */
    @ApiOperation(value = "获取课程列表")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @RequiresPermissions("course:list")
    public List<CourseDto> list(String name) {
        Wrapper<Course> courseWrapper = new EntityWrapper<>();
        if (null != name && "" != name) {
            courseWrapper = courseWrapper.eq("name", name);
        }
        List<Course> courseList = courseService.selectList(courseWrapper);
        return courseConverter.listEntityToDto(courseList);
    }
    /**
     * @description 编辑/修改课程
     * @author 张浩杰
     * @date 2025年3月15日 13:43:01
     * @param
     * @return
     */
    @ApiOperation(value = "编辑")
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @RequiresPermissions("course:edit")
    public Boolean edit(@RequestBody @Validated({Default.class, EditChecks.class}) CourseDto courseDto) {
        Boolean success = courseService.update(courseDto);
        return success;
    }
    /*
    * @description  教师编辑课程
    * @author 张人杰
    * @date 2025/8/6 14:59
    * @param [courseDto]
    * @return java.lang.Boolean
    */
    @ApiOperation(value = " 教师编辑课程")
    @RequestMapping(value = "teacheredit", method = RequestMethod.POST)
    @RequiresPermissions("course:edit")
    public Boolean teacheredit(@RequestBody @Validated({Default.class, EditChecks.class}) CourseDto courseDto) {
        ShiroUser user = ShiroKit.getUser();
        courseDto.setUserId(user.getId());
        courseDto.setType(1);
        courseDto.setAuthor(user.getName());
        courseDto.setCreationTime(null);
        courseDto.setJoinNumber(0);
        courseDto.setCommentNumber(0);
        courseDto.setCode(random()+"");

        Boolean success = courseService.update(courseDto);
        return success;
    }


    @ApiOperation(value = "获取")
    @RequestMapping(value = "get", method = RequestMethod.GET)
    @RequiresPermissions("course:get")
    public CourseDto get(@RequestParam("id") Long id) {
        /**更新参与人数*/
        courseService.updateJoinNumber(id);
        CourseDto courseDto =  courseService.get(id);
        courseDto.setCollectedNumber(courseService.getCollectedNumberByCourseId(id));
        return courseDto;
    }

    /**
      * @description 单个删除和批量删除课程
      * @author 张人杰
      * @date 2025/4/15 10:13
      * @Param List<Long> ids
      * @return Boolean true false
      */

    @ApiOperation(value = "删除")
    @RequestMapping(value = "del", method = RequestMethod.POST)
    @RequiresPermissions("course:del")
    public Boolean delete(@RequestBody List<Long> ids) {
        Boolean result = courseService.deleteBatchids(ids);
        return result;
    }
    /**
     * @description 导入数据
     * @author 张浩杰
     * @date 2025年3月15日 13:43:01
     * @param file
     * @return List<ImportCourseDto>
     */
    @ApiOperation(value = "导入数据")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    @RequiresPermissions("course:import")
    public List<ImportCourseDto> importData(MultipartFile file) {
        Workbook workbook = null;
        try {
            workbook = ExcelUtil.getWorkbook(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        String[] columns = new String[]{"name", "code", "nature", "score", "summary", "useBook", "recommendBook"};
        //课程名称 name,课程类别 category,难易程度 complexity,课程阶段 stage,课程简介 summary,
        // 课程Logo logo,课程类型（0-公开课1-教师课程) type,实验个数 exercise_number,参与学习人数 join_number,
        //课程评价 comment_number
        String[] columns = new String[]{"name","category","complexity","stage","summary","logo","type",
                "exerciseNumber","joinNumber","commentNumber"};
        List<Map<String, Object>> importData = ExcelUtil.readFilds(workbook, Arrays.asList(columns));
        List<ImportCourseDto> courseDtoList = courseService.importDataToListDo(importData);
        return courseDtoList;
    }

    @ApiOperation(value = "验证数据重复")
    @RequestMapping(value = "validate", method = RequestMethod.GET)
    @RequiresPermissions("course:validate")
    public Boolean validRepeat(@RequestParam("col") String col, String value, Long id,Long userId) {

        System.out.println(userId+"用户登陆ID");
        Wrapper<Course> courseWrapper=new EntityWrapper<>();
        courseWrapper.eq(col,value);
       if (id==null){

           int i = courseService.selectCount(courseWrapper);
           if (i>0){
               return false;
           }
       }else {
            courseWrapper.notIn("id",id).eq("user_id",userId);
           int i = courseService.selectCount(courseWrapper);
           if (i>0){
               return false;
           }
       }
       return  true;
    }

    @ApiOperation(value = "导出")
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    @RequiresPermissions("course:export")
    public void export(@RequestBody List<Long> ids, HttpServletResponse response) {
        //String[] columns = new String[]{"课程名称", "课程代码", "课程性质", "学分", "描述", "使用书籍", "推荐书籍"};
        String[] columns = new String[]{"课程名称", "课程类别", "难易程度", "课程阶段", "课程简介",
                "课程Logo", "课程类型","实验个数","参与学习人数","课程评价"};
        Wrapper<Course> wrapper = new EntityWrapper<>();
        wrapper = wrapper.in("id", ids);
        List<Course> courses = courseService.selectList(wrapper);
        String[][] data = new String[courses.size()][columns.length];
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            data[i][0] = course.getName();
            data[i][1] = course.getCategory();
            data[i][2] = course.getComplexity().toString();
            data[i][3] = course.getStage();
            data[i][4] = course.getSummary().toString();
            data[i][5] = course.getLogo().toString();
            if(course.getType().toString().equals("0")){
                data[i][6] = "公开课";
            }else if(course.getType().toString().equals("1")){
                data[i][6] = "教师课程";
            }
            data[i][7] = course.getExerciseNumber().toString();
            data[i][8] = course.getJoinNumber().toString();
            data[i][9] = course.getCommentNumber().toString();
           // data[i][9] = course.getCode();

        }
        HSSFWorkbook hssfWorkbook = ExportExcel.getHSSFWorkbook("课程列表", columns, data, null);
        ExportExcel.exportByResponse(response, hssfWorkbook, "课程导出");
    }

    private String nullStrToEmpty(String source) {
        if (null == source) {
            return "";
        } else {
            return source;
        }
    }
    /**
      * @description分页获取以及模糊搜寻获取当前用户下的课程信息
      * @author 张人杰
      * @date 2025/4/8 9:14
      * @Param
      * @return
      */
    @ApiOperation(value = "教师端分页and模糊搜寻获取当前用户下的课程信息")
    @PostMapping(value = "/getNowCourse")
    @RequiresPermissions("course:teacherPage")
    public Page<CourseDto> getNowCourse(@RequestBody  CommonSearchDto<Course> searchDto) {
        //获取当前登录用户ID
        Long userId = ShiroKit.getUserId();
        HashMap<String,String> map = new HashMap<>();
        Wrapper<Course> courseWrapper = searchDto.formToEntityWrapperWithSearch(null,null,map);
        courseWrapper.eq("user_id",userId);
        courseWrapper.eq("type",1);
        Page<Course>  page = courseService.selectPage(searchDto.getPlusPage(),courseWrapper);
        return courseConverter.pageEntityToDto(page) ;
    }
    /**
      * @description批量导入学校的课程
      * @author 张人杰
      * @date 2025/4/8 16:48
      * @Param List<Long> ids
      * @return
      */

    @ApiOperation(value = "批量导入课程",notes = "教师根据课程ID批量导入课程")
    @RequestMapping(value = "/batchImport",method = RequestMethod.POST)
    public Long delBatch(@RequestBody List<Long> ids){
        Long userId = ShiroKit.getUserId();
//        Long userId=677L;
        Long batchImport = courseService.batchImport(ids, userId,random()+"");
        return batchImport;
    }/**
      * @description教师端添加课程
      * @author 张人杰
      * @date 2025/4/10 9:25
      * @Param CourseDto
      * @return Boolean
      */
    @ApiOperation(value = "插入教师课程")
    @RequestMapping(value = "/addTeacherCourse", method = RequestMethod.POST)
    public Boolean addTeacherCourse(@RequestBody @Validated CourseDto courseDto) {
        ShiroUser user = ShiroKit.getUser();
        courseDto.setAuthor(user.getName());
        courseDto.setCode(random()+"");
        courseDto.setType(1);
        boolean res = courseService.add(courseDto);
        return res;
    }
    /**
      * @description 获取公开课程
      * @author 张人杰
      * @date 2025/4/10 10:09
      * @Param
      * @return
      */
    @ApiOperation(value = "获取公开课程")
    @RequestMapping(value = "publicPage", method = RequestMethod.POST)
    @RequiresPermissions("course:publicPage")
    public Page<CourseDto> publicPage(@RequestBody CommonSearchDto<Course> searchDto) {
        Page<Course> page = searchDto.getPlusPage();
        Wrapper<Course> courseWrapper = searchDto.formToEntityWrapper(null);
        courseWrapper.eq("type",0);
        page = courseService.selectPage(page, courseWrapper);
        return courseConverter.pageEntityToDto(page);
    }
    /**
      * @description 获取课程类型
      * @author 张人杰
      * @date 2025/4/10 14:08
      * @Param
      * @return List<Course>
      */

    @ApiOperation(value = "获取课程类型")
    @RequestMapping(value = "/getCourseType", method = RequestMethod.GET)
    public List<Course> getCourseType(){
        return  courseService.getCourseType();
    }
    /**
     * @description
     * @author 张人杰
     * @date 2025/4/8 17:59
     * @Param
     * @return 整形六位数
     */
    public int random(){
        int  i = cn.hutool.core.util.RandomUtil.randomInt(1000000);
//        int i=673216;
        EntityWrapper<Course> courseEntityWrapper=new EntityWrapper<>();
        courseEntityWrapper.eq("code",i);
        //与随机生成的验证码相同的数量
        int a = courseService.selectCount(courseEntityWrapper);
        if (a>0){
            System.out.println("验证码二次生成了-----------------------------------------------");
            return random();
        }else{
            return i;
        }

    }
    @ApiOperation(value = "刷新邀请码")
    @GetMapping(value = "/regainCode")
    public int regainCode(@RequestParam("courseid") Long courseid){
      int random=random();
        Course course=new Course();
        course.setId(courseid);
        course.setCode(random+"");
        boolean b = courseService.updateById(course);
        if (!b){
            throw  new FriendException("更新邀请码失败");
        }
        return random;
    }


    /**
     * @description 学生端课程分页
     * @author 宋泽山
     * @date   2019年4月19日 09:27:00
     * @param  searchDto 查询页dto
     * @return page
     */
    @ApiOperation(value = "学生端课程分页")
    @RequestMapping(value = "stuWebPage", method = RequestMethod.POST)
    @RequiresPermissions("course:studentPage")
    public Page<CourseDto> stuWebPage(@RequestBody CommonSearchDto<Course> searchDto) {
        /**获取相关分页信息:页数/该页的条目数*/
        Page<Course> page= searchDto.getPlusPage();

        Wrapper<Course> wrapper = courseService.setWrapper(searchDto);

        //获取当前登录用户ID
        Long userId = ShiroKit.getUserId();
        page = courseService.stuWebCoursePage(page,wrapper,userId,null,null);
        return courseConverter.pageEntityToDto(page);
    }
    /**
     * @description  判断当前课程是否被收藏了
     * @author SongZeShan
     * @date 2025/7/5 9:51
     * @param
     * @return
     */
    @RequiresPermissions("course:get")
    @ApiOperation(value = "判断当前课程是否被收藏了")
    @RequestMapping(value = "judgeCourseIsCollected", method = RequestMethod.GET)
    public Boolean judgeCourseIsCollected(@RequestParam("courseId") Long courseId){
        return courseService.judgeCourseIsCollected(courseId);
    }
    /**
     * @description 学生端课程 相关课程推荐
     * @author 宋泽山
     * @date   2019年4月19日 09:27:00
     * @param  courseId 课程id
     * @return List<Course>
     */
    @ApiOperation(value = "学生端课程的相关课程推荐")
    @RequestMapping(value = "courseRecommend", method = RequestMethod.GET)
    @RequiresPermissions("course:page")
    public List<CourseDto> courseRecommend(@RequestParam("id") Long courseId) {
        /**根据课程id查询出对应的全部标签*/
        List<Label> labelList = labelService.selectListByCourseId(courseId);
        /**实现相关逻辑*/
        List<Course> courseList = new ArrayList<>( courseService.courseRecommend(courseId,labelList));

        return courseConverter.listEntityToDto(courseList);
    }

    /**
     * @description 实现学生端课程的公共标签对应相应课程的缓存功能,默认为
     * @author 宋泽山
     * @date 2025/4/24 14:22
     * @param
     * @return List<Course>
     */
    @ApiOperation(value = "实现学生端课程的公共标签的缓存功能,同时联动课程技术类型")
    @Cacheable(value = "labelToCourseCache" , key="#techType" )
    @RequestMapping(value = "labelToCourseCache", method = RequestMethod.GET)
    @RequiresPermissions("course:page")
    public Set<String> labelToCourseCache(@RequestParam("techType")String techType) {
        List<Label> list ;
        if(ToolUtil.isNotEmpty(techType)){
            list = labelService.selectListByTechType(techType);
        }else{
            list = labelService.selectListByCourseId(null);
        }
        Set<String> arrayList= new HashSet<>(16);
        for(Label label : list){
            arrayList.add(label.getName());
        }
        return arrayList;
    }

    /**
      * @description 初始化数据测试
      * @author 马勇超
      * @date 2025/8/8 11:17
      * @return
     */
    @ApiOperation(value = "初始化数据测试")
    @GetMapping(value = "initialization")
    public Initialization initialization(){
        Initialization initialization = courseService.getInitialization();
        return initialization;
    }
    /**
      * @description 生成.json格式文件
      * @author 马勇超
      * @date 2025/8/9 11:17
      * @param
      * @return
     */
    @ApiOperation(value = "初始化数据")
    @PostMapping(value = "InitializationJSON")
    public void importInitialization(HttpServletResponse response) {
        // 标记文件生成是否成功
        boolean flag = true;
        Initialization initialization = courseService.getInitialization();
        JSONArray jsonObject = JSONArray.fromObject(initialization);
        String jsonString = jsonObject.toString();
//        createJsonFile(jsonString1, "/fileStorage/download/json", "初始化数据");
        String fileName = "初始化数据";
        // 拼接文件完整路径
        String fullPath = "/fileStorage/download/json" + File.separator + fileName + ".json";
        // 生成json格式文件
        try {
            // 保证创建一个新文件
            File file = new File(fullPath);
            if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
                file.getParentFile().mkdirs();
            }
            if (file.exists()) { // 如果已存在,删除旧文件
                file.delete();
            }
            file.createNewFile();

//            if(jsonString.indexOf("'")!=-1){
//                //将单引号转义一下，因为JSON串中的字符串类型可以单引号引起来的
//                jsonString = jsonString.replaceAll("'", "\\'");
//            }
//            if(jsonString.indexOf("\"")!=-1){
//                //将双引号转义一下，因为JSON串中的字符串类型可以单引号引起来的
//                jsonString = jsonString.replaceAll("\"", "\\\"");
//            }
//
//            if(jsonString.indexOf("\r\n")!=-1){
//                //将回车换行转换一下，因为JSON串中字符串不能出现显式的回车换行
//                jsonString = jsonString.replaceAll("\r\n", "\\u000d\\u000a");
//            }
//            if(jsonString.indexOf("\n")!=-1){
//                //将换行转换一下，因为JSON串中字符串不能出现显式的换行
//                jsonString = jsonString.replaceAll("\n", "\\u000a");
//            }

            // 格式化json字符串
//            jsonString = JsonFormatTool.formatJson(jsonString);
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("GBK"),"iso8859-1"));
            // 将格式化后的字符串写入文件
            OutputStream outputStream = new FileOutputStream(file);
            outputStream = response.getOutputStream();
            Writer write = new OutputStreamWriter(outputStream);
            write.write(jsonString);
            write.flush();
            write.close();
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        // 返回是否成功的标记
//        return flag;
    }

    /**
      * @description 资源图片下载打包
      * @author 马勇超
      * @date 2025/8/9 11:20
      * @return
     */
    @ApiOperation(value = "资源图片下载打包")
    @PostMapping(value = "downLoadImgs")
    public void downLoadImgs(HttpServletResponse response) throws Exception{
        Wrapper<FileResource> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id",ShiroKit.getUserId());
        List<FileResource> resources= fileResourceService.selectList(wrapper);
        List<String> files = new ArrayList<>();
        for (FileResource resource : resources) {
            String urlPath = properties.getFileUploadPath().split("\\\\upload")[0]+resource.getPath();
            files.add(urlPath);
        }
        //2.开始批量下载功能
        try {
            //文件的名称
            String downloadFilename = "初始数据图片包.zip";
            //转换中文否则可能会产生乱码
            downloadFilename = URLEncoder.encode(downloadFilename, "UTF-8");
            // 指明response的返回对象是文件流
            response.setContentType("application/octet-stream");
            // 设置在下载框默认显示的文件名
            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFilename);
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
            for (int i = 0; i < files.size(); i++) {
                FileInputStream in = new FileInputStream(files.get(i));
                String type = resources.get(i).getType();
                zos.putNextEntry(new ZipEntry(resources.get(i).getName()+"."+type));
                byte[] buffer = new byte[1024];
                int r = 0;
                while ((r = in.read(buffer)) != -1) {
                    zos.write(buffer, 0, r);
                }
                in.close();
            }
            zos.flush();
            zos.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
      * @description 初始化数据上传
      * @author 马勇超
      * @date 2025/8/9 11:20
      * @param initialization
      * @return
     */
    @ApiOperation(value = "初始化数据上传")
    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(value = "uploadInitialization",method = RequestMethod.POST)
    public void uploadInitialization(@RequestBody Initialization initialization){
        System.out.print(initialization);
        courseService.uploadInitialization(initialization);
    }

    /**
      * @description 资源压缩包解压
      * @author 马勇超
      * @date 2025/8/13 12:50
      * @param zipFilePath
      * @return
     */
    @ApiOperation(value = "资源压缩包解压")
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "decompressionZip")
    public void openZip(@RequestParam("zipFilePath") String zipFilePath)throws Exception{
//        //获取今天的日期当作文件夹
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        String profixTime = sdf1.format(new Date());
        //截取长度
        int num = zipFilePath.lastIndexOf("/");
        // 截取到/upload/145/20190614，压缩包所在文件夹
        String path = zipFilePath.substring(0,num);
        // 截取到/upload/145
        int num2 = path.lastIndexOf("/");
        String path2 = path.substring(0,num2)+"/"+profixTime+"/";
        zipFilePath = properties.getFileUploadPath().split("\\\\upload")[0] + zipFilePath;
        String destDir = properties.getFileUploadPath().split("\\\\upload")[0]+path2;
        System.out.println("++++++++++++++++++"+destDir);
//        ZipUtils.unZip(zipFilePath,destDir);
        String CHINESE_CHARSET = "UTF-8";
        /**
         * 文件读取缓冲区大小
         */
        int CACHE_SIZE = 1024;
        ZipFile zipFile = new ZipFile(zipFilePath, CHINESE_CHARSET);
        Enumeration<?> emu = zipFile.getEntries();
        BufferedInputStream bis;
        FileOutputStream fos;
        BufferedOutputStream bos;
        File file, parentFile;
        ZipEntry entry;
        byte[] cache = new byte[CACHE_SIZE];
        while (emu.hasMoreElements()) {
            entry = (ZipEntry) emu.nextElement();
            if (entry.isDirectory()) {
                new File(destDir + entry.getName()).mkdirs();
                continue;
            }
            bis = new BufferedInputStream(zipFile.getInputStream(entry));
            file = new File(destDir + entry.getName());
            parentFile = file.getParentFile();
            if (parentFile != null && (!parentFile.exists())) {
                parentFile.mkdirs();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos, CACHE_SIZE);
            int nRead = 0;
            while ((nRead = bis.read(cache, 0, CACHE_SIZE)) != -1) {
                fos.write(cache, 0, nRead);
            }
            // 保存到文件资源列表
            FileResource fileResource = new FileResource();
            long b = entry.getSize();
            Double size = 0.0;
            if(b < 1024){
                fileResource.setSize(String.valueOf(b)+"字节");
            }else if(b/1024 < 1024){
                size = (double)b/1024;
                size = (double) Math.round(size * 100) / 100;
                fileResource.setSize(String.valueOf(size)+"KB");
            }else{
                size = (double)b/1024/1024;
                size = (double) Math.round(size * 100) / 100;
                fileResource.setSize(String.valueOf(size)+"MB");
            }
            String str = entry.getName();
            int le = str.lastIndexOf(".");
            List<String> list = Arrays.asList(str.split("\\\\"));
            int n  = list.get(list.size() - 1).lastIndexOf(".");
            String name = list.get(list.size() - 1).substring(0,n);
            fileResource.setUserId(ShiroKit.getUserId());
            fileResource.setName(name);
            fileResource.setType(str.substring(le+1,str.length()));
            fileResource.setPath((destDir+list.get(list.size()-1)).split(":")[1].replaceAll("\\\\", "/"));
            if(!fileResourceService.insert(fileResource)){
                throw new FriendException("资源解析出错！");
            }
            bos.flush();
            bos.close();
            fos.close();
            bis.close();
        }
        zipFile.close();
    }

}

