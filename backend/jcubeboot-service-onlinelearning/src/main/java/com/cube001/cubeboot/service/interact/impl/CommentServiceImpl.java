package com.cube001.cubeboot.service.interact.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import com.cube001.cubeboot.community.entity.CommunityTheme;
import com.cube001.cubeboot.core.authorization.entity.User;
import com.cube001.cubeboot.core.authorization.mapper.UserMapper;
import com.cube001.cubeboot.core.authorization.service.UserService;
import com.cube001.cubeboot.core.authorization.shiro.ShiroKit;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.core.util.ToolUtil;
import com.cube001.cubeboot.interact.entity.Comment;
import com.cube001.cubeboot.interact.entity.CommentLike;
import com.cube001.cubeboot.interact.entity.enums.CategoryEnum;
import com.cube001.cubeboot.service.community.mapper.CommunityThemeMapper;
import com.cube001.cubeboot.service.course.mapper.CourseMapper;
import com.cube001.cubeboot.service.course.mapper.CoursePathMapper;
import com.cube001.cubeboot.service.course.mapper.ExerciseJoinReportMapper;
import com.cube001.cubeboot.service.interact.CommentService;
import com.cube001.cubeboot.service.interact.Dto.CommentConverter;
import com.cube001.cubeboot.service.interact.Dto.CommentDto;
import com.cube001.cubeboot.service.interact.Dto.CommentLikeDto;
import com.cube001.cubeboot.service.interact.mapper.CommentLikeMapper;
import com.cube001.cubeboot.service.interact.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 *  评价实现类
 *  CommentServiceimpl.java
 * </p>
 *
 * @author qw
 * @since 2019-03-12
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentLikeMapper commentLikeMapper;
    @Autowired
    private CommentConverter commentConverter;
    /**报告管理*/
    @Autowired
    private ExerciseJoinReportMapper exerciseJoinReportMapper;
    /** 社区主题*/
    @Autowired
    private CommunityThemeMapper  communityThemeMapper;
    /**课程管理*/
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private UserService userService;

    /**
     * @description 学生端的 分页查询课程评价
     * @author 宋泽山
     * @date   2019年3月19日 08:43:28
     * @param
     * @return
     */
    @Override
    public Page<Comment> selStuCourseCommentPage(Page<Comment> plusPage, Wrapper<Comment> wrapper) {
        ArrayList<Comment> commentList =new ArrayList<>(baseMapper.selStuCourseCommentPage(plusPage,wrapper));
        //增加user实体
        for (int i=0;i<commentList.size();i++){
            Comment comment = commentList.get(i);
            //设置每个评论的用户信息
            Long userId = comment.getUserId();
            User user = userService.selectById(userId);
            User commentUser = new User();
            //只暴露两个字段
            commentUser.setUserName(user.getUserName()).setUserPic(user.getUserPic());
            comment.setUser(commentUser );
            commentList.set(i, comment);
        }

        plusPage.setRecords(commentList);
        return plusPage;
    }

    /**
     * @description 分页查询课程评价
     * @author 张浩杰
     * @date   2019年3月19日 08:43:28
     * @param
     * @return
     */
    @Override
    public Page<Comment> searchPage(Page<Comment> plusPage, Wrapper<Comment> formToEntityWrapper) {
        List<Comment> commentList = baseMapper.searchPage(plusPage,formToEntityWrapper);
        plusPage.setRecords(commentList);
        return plusPage;
    }
    /**
     * @description  分页查询帖子回复
     * @author 张浩杰
     * @date 2025/3/28 18:21
     * @param
     * @return
     */
    @Override
    public Page<Comment> searchPageComment(Page<Comment> plusPage, Wrapper<Comment> formToEntityWrapper) {
        formToEntityWrapper.eq("category", CategoryEnum.CommunityReply);
        List<Comment> commentList = this.baseMapper.searchPageComment(plusPage,formToEntityWrapper);
        plusPage.setRecords(commentList);
        return plusPage;
    }

    /**
     * 发表和回复评价
     * @author Juci
     * @param commentDto 发表内容
     * @param userId 用户编号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(CommentDto commentDto, Long userId){
        User user = userMapper.selectById(userId);
        Comment comment = commentConverter.dtoToEntity(commentDto);
        comment.setUserId(userId);
        comment.setLikeNumber(0);
        comment.setUserName(user.getName());
        // 社区评价
        if(commentDto.getCategory()==CategoryEnum.CommunityReply){
            // 社区评价完善
            if(commentDto.getLinkId() != null){
               CommunityTheme communityTheme = communityThemeMapper.getThemeById(commentDto.getLinkId());
//                comment.setReplyUserId(communityTheme.getUserId());
//                comment.setReplyUserName(communityTheme.getUser().getName());
                // 评论数加一
                communityThemeMapper.addComment(communityTheme.getId(),1);
            }
        }
        //课程评价
        else if(commentDto.getCategory()==CategoryEnum.CourseEvaluation){
            // 课程评价完善
            if(commentDto.getLinkId() != null){
                comment.setReplyUserId(commentDto.getUserId());
                comment.setReplyUserName(commentDto.getUserName());
            }
        }
        // 路径评价
        else if(commentDto.getCategory()==CategoryEnum.PathEvaluation){
            // 路径评价完善
            if(commentDto.getLinkId() != null){
                Comment replyComment = baseMapper.selectById(commentDto.getLinkId());
                comment.setReplyUserId(replyComment.getUserId());
                comment.setReplyUserName(replyComment.getUserName());
            }
        }
        // 报告评价
        else if(commentDto.getCategory()==CategoryEnum.Report){
            if(this.exerciseJoinReportMapper.updateComment(commentDto.getLinkId(),1)==0){
                throw new FriendException("发布评价失败！");
            }
        }
        if(baseMapper.insert(comment)==0){
            throw new FriendException("发布评价失败！");
        }
    }

    /**
     * @description 点赞
     * @author qw
     * @date 2025/3/21 9:29
     * @param id,userId
     * @return void
    */
    @Override
    public void recommend(Long id, Long userId) {
        CommentLike commentLike = new CommentLike();
        Wrapper<CommentLike> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id",userId).eq("comment_id",id);
        if (this.commentLikeMapper.selectCount(wrapper)==0){
            baseMapper.updateLikeNumber(id);
            commentLike.setUserId(userId);
            commentLike.setCommentId(id);
            commentLikeMapper.insert(commentLike);
        }else {
            throw new FriendException("请不要重复点赞");
        }
    }

    /**
     * 分页获取评价列表
     * @author Juci
     * @param page
     * @param userId
     * @param wrapper
     * @return
     */
    @Override
    public Page<CommentDto> selectPageAndLike(Page<CommentDto> page,Long userId,Wrapper<CommentDto> wrapper)
    {
        wrapper.eq("a.is_deleted","0");
        List<CommentDto> commentDtos = baseMapper.selectPageAndLike(page,userId,wrapper);
        page.setRecords(commentDtos);
        return page;
    }
	@Override
    public Page<Comment> getCommentList(Long userId,Page<Comment> page, Wrapper<Comment> formToEntityWrapperWithSearch) {
        List<Comment> stuRewardPunishList = this.baseMapper.getCommentListmapper(userId,page, formToEntityWrapperWithSearch.eq("tc.is_deleted",0).orderBy("tc.creation_time",true));
        page.setRecords(stuRewardPunishList);
        return page;
    }

    /**
     * 删除评论
     * @param id
     * @param userId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Long id,Long userId) {
        // 处理相关评价数量
        Comment comment = this.baseMapper.selectById(id);
//        if(!userId.equals(comment.getUserId())){
//            throw new FriendException("非法删除用户评论！");
//        }
        if(comment.getCategory()==CategoryEnum.CommunityReply){
            this.communityThemeMapper.addComment(comment.getLinkId(),-1);
        }
        else if(comment.getCategory()==CategoryEnum.Report){
            this.exerciseJoinReportMapper.updateComment(comment.getLinkId(),-1);
        }
        this.baseMapper.deleteById(id);
    }
}
