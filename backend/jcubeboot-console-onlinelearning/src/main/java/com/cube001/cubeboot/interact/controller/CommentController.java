package com.cube001.cubeboot.interact.controller;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.core.util.ToolUtil;
import com.cube001.cubeboot.interact.entity.Comment;
import com.cube001.cubeboot.interact.entity.enums.CategoryEnum;
import com.cube001.cubeboot.service.base.dto.BaseConverter;
import com.cube001.cubeboot.service.interact.CommentService;
import com.cube001.cubeboot.service.interact.Dto.CommentConverter;
import com.cube001.cubeboot.service.interact.Dto.CommentDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 *  路径评论控制器
 *  CommentController
 * </p>
 *
 * @author qw
 * @since 2019-03-12
 */
@Api(value = "CommentController",produces = "评论功能")
@RestController
@RequestMapping(value = "${cubeboot.app-manager-path}/comment")
public class CommentController implements IApplicationService {

    @Autowired
    private CommentService commentService;
    @Autowired
    private BaseConverter baseConverter;
    @Autowired
    private CommentConverter commentConverter;
    /**
     * @description 学生端的课程评价分页
     * @author 宋泽山
     * @date   2019年3月19日 08:43:28
     * @param
     * @return
     */
    @ApiOperation(value = "学生端的课程评价分页",notes = "学生端的课程评价分页")
    @RequestMapping(value = "/selStuCourseCommentPage",method = RequestMethod.POST)
    @ResponseBody
    public Page<CommentDto> selStuCourseCommentPage(@RequestBody CommonSearchDto<Comment> commonSearchDto){
        Long userId = ShiroKit.getUserId();
        Wrapper<Comment> courseWrapper = commonSearchDto.formToEntityWrapperWithSearch(null, null);
        //添加查找条件
        courseWrapper.eq("tc.category", CategoryEnum.CourseEvaluation);

        if(ToolUtil.isEmpty(commonSearchDto.getForm().get("linkId"))){
            throw new FriendException("课程ID参数为空,请重新尝试!");
        }

        Page<Comment> commentPage = commentService.selStuCourseCommentPage(commonSearchDto.getPlusPage(),courseWrapper);
        return baseConverter.commentDtoPage(commentPage);
    }
    /**
    * @description  展示路径详情评论
    * @author 马勇超
    * @date 2025/4/2 17:20
    * @param   commonSearchDto
    * @return   commentPathPage
    */
    @ApiOperation(value = "展示路径详情评论",notes = "展示路径详情评论")
    @RequestMapping(value = "/commentPathPage",method = RequestMethod.POST)
    @ResponseBody
    public Page<CommentDto> commentPathPage(@RequestBody CommonSearchDto<CommentDto> commonSearchDto){
        Long userId = ShiroKit.getUserId();
        Wrapper<CommentDto> courseWrapper = commonSearchDto.formToEntityWrapperWithSearch(null, null);
        courseWrapper.eq("category", CategoryEnum.PathEvaluation);
        Page<CommentDto> commentPage = commentService.selectPageAndLike(commonSearchDto.getPlusPage(),userId,courseWrapper);
        return commentPage;
    }
    /**
     * @description 分页查询课程评价
     * @author 张浩杰
     * @date   2019年3月19日 08:43:28
     * @param
     * @return
    */
    @ApiOperation(value = "课程评价分页",notes = "课程评价分页")
    @RequestMapping(value = "/coursePage",method = RequestMethod.POST)
    @ResponseBody
    public Page<CommentDto> coursePage(@RequestBody CommonSearchDto<Comment> commonSearchDto){
        Wrapper<Comment> courseWrapper = commonSearchDto.formToEntityWrapperWithSearch(null, null);
        courseWrapper.eq("category", CategoryEnum.CourseEvaluation);
        Page<Comment> commentPage = commentService.selectPage(commonSearchDto.getPlusPage(),courseWrapper);
        return baseConverter.commentDtoPage(commentPage);
    }

    /**
     * 删除评论
     * @author Juci
     * @date 2025/5/14 09:26
     * @param id
     */
    @ApiOperation(value = "删除评论",notes = "删除评论")
    @GetMapping("/deleteComment")
    public void del(@RequestParam("id") Long id){
        Long userId = ShiroKit.getUserId();
        commentService.deleteComment(id,userId);
    }
    /**
     * 点赞！
     * @author qw
     * @date 2025/3/19 17:33
     * * @Param id
     *
     */
    @ApiOperation(value = "评价点赞",notes = "评价点赞")
    @GetMapping("/recommend")
    public void recommend(@RequestParam("id") Long id){
        Long userId = ShiroKit.getUserId();
        commentService.recommend(id,userId);
    }

    /**
     * 报告评价分页
     * @author Juci
     * @param commonSearchDto
     * @return
     */
    @ApiOperation(value = "报告评价分页",notes = "报告评价分页")
    @RequestMapping(value = "/reportPage",method = RequestMethod.POST)
    @ResponseBody
    public Page<CommentDto> reportPage(@RequestBody CommonSearchDto<CommentDto> commonSearchDto){
        Long userId = ShiroKit.getUserId();
        Wrapper<CommentDto> courseWrapper = commonSearchDto.formToEntityWrapperWithSearch(null, null);
        courseWrapper.eq("category", CategoryEnum.Report);
        Page<CommentDto> commentPage = commentService.selectPageAndLike(commonSearchDto.getPlusPage(),userId,courseWrapper);
        return commentPage;
    }

    /**
     * 创建评论（通用方法）
     * @author Juci
     * @param commentDto
     */
    @ApiOperation(value = "创建评价",notes = "创建评价")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public void add(@RequestBody @Validated CommentDto commentDto){
        Long userId = ShiroKit.getUserId();
        commentService.add(commentDto,userId);
    }
    /**
    * @description社区主题管理中帖子标题对应的所有评论回复
    * @author 张人杰
    * @date 2025/4/1 14:50
    * @Param
    * @return
    */
    @ApiOperation(value = "社区帖子对应所有评论分页展示",notes = "社区帖子对应所有评论")
    @RequestMapping(value = "/themeCommentPage",method = RequestMethod.POST)
    public Page<CommentDto> themeCommentPage(@RequestBody CommonSearchDto<Comment> searchDto){
        Long userId = ShiroKit.getUserId();
        Map<String,String> map = new HashMap<>();
        //社区主题编号
        map.put("link_id","tc.");
        Page<Comment>    page = commentService.getCommentList(userId,searchDto.getPlusPage(), searchDto.formToEntityWrapper(null,map));
        return commentConverter.pageEntityToDto(page);
    }
}
