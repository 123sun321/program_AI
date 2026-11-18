package com.cube001.cubeboot.community.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.community.entity.CommunityTheme;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.domain.IApplicationService;
import com.cube001.cubeboot.core.dto.CommonSearchDto;
import com.cube001.cubeboot.interact.entity.Comment;
import com.cube001.cubeboot.service.community.CommunityThemeService;
import com.cube001.cubeboot.service.community.dto.CommunityThemeConverter;
import com.cube001.cubeboot.service.community.dto.CommunityThemeDto;
import com.cube001.cubeboot.service.interact.CommentService;
import com.cube001.cubeboot.service.interact.Dto.CommentConverter;
import com.cube001.cubeboot.service.interact.Dto.CommentDto;
import com.cube001.cubeboot.vo.course.CommonSearchExtendUserIdVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/***
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: CommunityThemeController
 * 社区主题管理controller
 *
 * @author anonymous
 * @date   2019/3/21 15:26
 * @version 1.0
 */
@Api(value = "CommunityThemeController",description = "社区主题管理Controller")
@RestController
@RequestMapping(value = "${cubeboot.app-manager-path}/communityTheme")
public class CommunityThemeController implements IApplicationService {
    @Autowired
    private CommunityThemeService communityThemeService;
    @Autowired
    private CommunityThemeConverter communityThemeConverter;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentConverter commentConverter;

    /**
     * @description 社区主题通过标题搜索/分页
     * @author anonymous
     * @date 2025/3/21 15:29
     * @param commonSearchDto
     * @return Page<CommunityThemeDto>
     */
    @ApiOperation(value = "社区主题管理搜索/分页",notes = "社区主题管理搜索/分页")
    @RequestMapping(value = "/page",method = RequestMethod.POST)
    @ResponseBody
    public Page<CommunityThemeDto> pageCommunityTheme(@RequestBody CommonSearchDto<CommunityTheme> commonSearchDto){
        Page<CommunityTheme> communityThemePage = communityThemeService.selectAllThemePage
                (commonSearchDto.getPlusPage(), commonSearchDto.formToEntityWrapperWithSearch(new String[]{"a.plate"},null).orderBy("a.creation_time",false));
        return  communityThemeConverter.pageCommunityThemeDto(communityThemePage);
    }

    /**
    * @description  用户获取自己发的帖子
    * @author anonymous
    * @date 2025/4/24 10:58
    * @param   commonSearchExtendUserIdVo
    * @return
    */
    @ApiOperation(value = "用户获取自己发的帖子")
    @PostMapping(value = "/studentThemePage")
    public Page<CommunityThemeDto> studentPageCommunityTheme(@RequestBody CommonSearchExtendUserIdVo<CommunityTheme> commonSearchExtendUserIdVo){
        if(commonSearchExtendUserIdVo.getUserId()==null){
            commonSearchExtendUserIdVo.setUserId(ShiroKit.getUserId());
        }
        Page page = commonSearchExtendUserIdVo.getPlusPage();
        Wrapper<CommunityTheme> wrapper = commonSearchExtendUserIdVo.formToEntityWrapper(null,null);
        wrapper.eq("a.user_id",commonSearchExtendUserIdVo.getUserId());
        wrapper.orderBy("a.creation_time",false);
        Page<CommunityTheme> communityThemePage = communityThemeService.selectPageByStudent(page,wrapper);
        return communityThemeConverter.pageCommunityThemeDto(communityThemePage);
    }

    /**
    * @description  获取用户个人的回复帖子主题和回复内容
    * @author anonymous
    * @date 2025/4/24 15:00
    * @param   searchExtendUserIdVo
    * @return
    */
    @ApiOperation(value = "获取用户个人的回复帖子主题和回复内容")
    @PostMapping(value = "studentReplyComment")
    public Page<CommunityThemeDto> studentPageComment(@RequestBody CommonSearchExtendUserIdVo<CommunityTheme> searchExtendUserIdVo){
        if(searchExtendUserIdVo.getUserId() == null){
            searchExtendUserIdVo.setUserId(ShiroKit.getUserId());
        }
        Page page = searchExtendUserIdVo.getPlusPage();
        Wrapper<CommunityTheme> wrapper = searchExtendUserIdVo.formToEntityWrapper(null,null);
        wrapper.eq("tc.user_id",searchExtendUserIdVo.getUserId());
        wrapper.groupBy("a.id");
        wrapper.orderBy("a.creation_time",false);
        Page<CommunityTheme> communityThemePage = communityThemeService.selectBySelfReply(page,wrapper);
        return communityThemeConverter.pageCommunityThemeDto(communityThemePage);
    }

    /**
     * @description 帖子回复查看/分页
     * @author anonymous
     * @date 2025/3/28 17:43
     * @param
     * @return
     */
    @ApiOperation(value = "社区帖子回复分页",notes = "社区帖子回复分页")
    @RequestMapping(value = "/pageComment",method = RequestMethod.POST)
    @ResponseBody
    public Page<CommentDto> pageCommunityThemeComment(@RequestBody CommonSearchDto<Comment> commonSearchDto){
        Page<Comment> commentPage = commentService.searchPageComment(commonSearchDto.getPlusPage(),commonSearchDto.formToEntityWrapper(null));
        return  commentConverter.pageEntityToDto(commentPage);
    }
    /**
     * @description 删除/批量删除社区主题管理
     * @author anonymous
     * @date 2025/3/21 16:26
     * @param  ids
     * @return
     */
    @ApiOperation(value = "删除/批量删除社区主题管理")
    @PostMapping("/delete")
    public void deleteCommunityTheme(@RequestBody List<Long> ids){
        communityThemeService.delete(ids);
    }

    /**
     * @description 添加/新增帖子
     * @author anonymous
     * @date 2025/3/22 13:47
     * @param communityThemeDto,userId
     * @return
     */
    @ApiOperation(value = "添加/新增帖子")
    @PostMapping(value = "/add")
    public void addCommunityTheme(@RequestBody @Validated CommunityThemeDto communityThemeDto){
        //获取当前登录用户ID
        Long userId = ShiroKit.getUserId();
        communityThemeService.addCommunityTheme(communityThemeDto,userId);
    }
    /**
     * @description 修改帖子
     * @author anonymous
     * @date 2025/3/29 10:41
     * @param
     * @return
    */
    @ApiOperation(value = "修改帖子")
    @PostMapping(value = "/edit")
    public void editCommunityTheme(@RequestBody @Validated CommunityThemeDto communityThemeDto){
        communityThemeService.editCommunityTheme(communityThemeDto);
    }
    /**
     * @description 根据id查询帖子信息
     * @author anonymous
     * @date 2025/3/29 14:18
     * @param id
     * @return CommunityThemeDto
    */
    @ApiOperation(value = "根据id查询帖子信息")
    @GetMapping(value = "/getThemeById")
    public CommunityThemeDto getThemeById(@RequestParam("id") Long id){
        return communityThemeService.getThemeById(id);
    }
    /**
     * @description 查看增加浏览量++
     * @author anonymous
     * @date 2025/3/22 17:44
     * @param  communityThemeId
     * @return
     */
    @ApiOperation(value = "查看增加浏览量")
    @GetMapping(value = "/addClick")
    public void addClick(@RequestParam("communityThemeId") Long communityThemeId){
        communityThemeService.addClick(communityThemeId);
    }

   /**
    * @description 社区管理帖子验证
    * @author anonymous
    * @date 2025/4/19 17:43
    * @param id,col,value
    * @return Boolean
   */
    @ApiOperation(value = "验证")
    @PostMapping(value = "validate")
    public  Boolean validRepeat(Long id,@RequestParam("col") String col,@RequestParam("value") String value) {
        if (id == null) {
            //如果编号id不存在
            Integer communityTheme = communityThemeService.getCommunityTheme(col, value);
            if (communityTheme > 0) {
                return false;
            }
        } else {
            //如果编号id存在
            Wrapper<CommunityTheme> communityThemeWrapper=new EntityWrapper<>();
             communityThemeWrapper.eq(col,value).eq("id",id);
            int i = communityThemeService.selectCount(communityThemeWrapper);
            if (i > 0) {
                return false;
            }
        }
        return true;
    }
    @ApiOperation(value = "删除主题评论")
    @GetMapping(value = "/deleteThemecomment")
    public void deleteThemecomment(@RequestParam("id") Long themeID){
        communityThemeService.deleteThemeComment(themeID);
    }
}





