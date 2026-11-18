package com.cube001.cubeboot.service.interact.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.service.interact.Dto.CommentDto;
import com.cube001.cubeboot.service.interact.Dto.CommentLikeDto;
import com.cube001.cubeboot.interact.entity.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: CommentMapper.java
 * 路径评论Mapper 接口
 * </p>
 *
 * @author qw
 * @since 2019-03-12
 */
public interface CommentMapper extends BaseMapper<Comment> {
    /**
     * @description 学生端的 分页查询课程评价
     * @author 宋泽山
     * @date   2019年3月19日 08:43:28
     * @param
     * @return
     */
    List<Comment> selStuCourseCommentPage(Page<Comment> plusPage,@Param("ew")  Wrapper<Comment> formToEntityWrapper);
    /**
     * @description 分页查询课程评价
     * @author 张浩杰
     * @date   2019年3月19日 08:43:28
     * @param
     * @return
     */
    List<Comment> searchPage(Page<Comment> plusPage, @Param("ew") Wrapper<Comment> eq);
    /**
     * @description  分页查询帖子回复
     * @author 张浩杰
     * @date 2025/3/28 18:21
     * @param
     * @return
     */
    List<Comment> searchPageComment(Page<Comment> plusPage, @Param("ew") Wrapper<Comment> eq);
    /**
     * @description likeNumber值+1(点赞)
     * @author qw
     * @date 2025/3/21 9:25
     * @param id
    */
    void updateLikeNumber(Long id);
    /**
      * @description
      * @author 张人杰
      * @date 2025/4/1 16:38
      * @Param
      * @return
      */

    List<Comment>  getCommentListmapper(@Param("userId") Long userId,Page<Comment> page, @Param("ew") Wrapper<Comment> wrapper);

    /**
     * 查询评论分页并关联点赞
     * @param page
     * @param userId
     * @param wrapper
     * @return
     */
    List<CommentDto> selectPageAndLike(Page<CommentDto> page,@Param("userId") Long userId, @Param("ew") Wrapper<CommentDto> wrapper);

}
