package com.cube001.cubeboot.course.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.course.entity.Course;
import com.cube001.cubeboot.course.entity.CourseCollect;
import com.cube001.cubeboot.service.course.CourseCollectService;
import com.cube001.cubeboot.service.course.dto.CourseCollectConverter;
import com.cube001.cubeboot.service.course.dto.CourseCollectDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author anonymous
 * @since 2019-03-11
 */
@RestController
@RequestMapping(value = "${cubeboot.app-manager-path}/coursecollect")
public class CourseCollectController implements IApplicationService {

    @Autowired
    private CourseCollectService courseCollectService;
    @Autowired
    private CourseCollectConverter courseCollectConverter;
    /**
     * @description
     * @author anonymous
     * @date 2025/4/24 20:03
     * @param
     * @return
     */
    @ApiOperation(value = "通过课程ID删除当前用户的对应收藏")
    @GetMapping("/delByCourseId")
    public Boolean delByCourseId(@RequestParam("courseId") Long courseId){
        Long userId = ShiroKit.getUserId();
        return this.courseCollectService.delete(new EntityWrapper<CourseCollect>().eq("user_id", userId).eq("course_id", courseId));
    }

    /**
     * 添加实验练习
     * @author anonymous
     * @param courseid 课程编号
     * @return
     */
    @ApiOperation(value = "添加收藏")
    @GetMapping("/add")
    public void add(@RequestParam("courseid") Long courseid) {
        Long userid = ShiroKit.getUserId();
        this.courseCollectService.add(userid,courseid);
    }

    /**
     * 删除收藏
     * @author anonymous
     * @param id
     */
    @ApiOperation(value = "删除收藏")
    @GetMapping("/del")
    public void del(@RequestParam("id") Long id){
        Long userid = ShiroKit.getUserId();
        this.courseCollectService.delete(userid,id);
    }
    /**
     * 分页获取实验列表
     * @author anonymous
     * @param searchDto
     * @return
     */
    @ApiOperation(value = "分页获取实验列表")
    @RequestMapping(value = "page", method = RequestMethod.POST)
    public Page<CourseCollectDto> page(@RequestBody CommonSearchDto<CourseCollectDto> searchDto) {
        Page<CourseCollectDto> page = searchDto.getPlusPage();
        Wrapper<CourseCollectDto> courseWrapper = searchDto.formToEntityWrapperWithSearch(null, null);
        Long userId = ShiroKit.getUserId();
        courseWrapper.eq("a.user_id",userId);
        page = courseCollectService.searchPage(page, courseWrapper);
        return page;
    }
}
