package com.cube001.cubeboot.service.interact;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.cube001.cubeboot.service.interact.Dto.CommentDto;

import com.cube001.cubeboot.interact.entity.Comment;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qw
 * @since 2019-03-12
 */
public interface CommentService extends IService<Comment> {
    /**
     * @description 学生端的 分页查询课程评价
     * @author 宋泽山
     * @date   2019年3月19日 08:43:28
     * @param
     * @return
     */
    Page<Comment> selStuCourseCommentPage(Page<Comment> plusPage, Wrapper<Comment> formToEntityWrapper);
    /**
     * @description 分页查询课程评价
     * @author 张浩杰
     * @date   2019年3月19日 08:43:28
     * @param
     * @return
     */
    Page<Comment> searchPage(Page<Comment> plusPage, Wrapper<Comment> formToEntityWrapper);
    /**
     * @description  分页查询帖子回复
     * @author 张浩杰
     * @date 2025/3/28 18:21
     * @param
     * @return
    */
    Page<Comment> searchPageComment(Page<Comment> plusPage, Wrapper<Comment> formToEntityWrapper);

    /**
     * 评价点赞
     * @author Juci
     * @param id
     * @param userId
     */
    void recommend(Long id, Long userId);
    /**
     * 发表和回复评价
     * @author Juci
     * @param commentDto 发表内容
     * @param userId 用户编号
     */
    void add(CommentDto commentDto, Long userId);
    /**
     * 获取评论分页并关联评价表
     * @author Juci
     * @param page
     * @param userId
     * @param wrapper
     * @return
     */
    Page<CommentDto> selectPageAndLike(Page<CommentDto> page,Long userId,Wrapper<CommentDto> wrapper);
    /**
      * @description社区帖子对应评论回复分页
      * @author 张人杰
      * @date 2025/4/2 17:33
      * @Param
      * @return
      */
	Page<Comment> getCommentList(Long userId,Page<Comment> page, Wrapper<Comment> formToEntityWrapperWithSearch);

    /**
     * 删除评论
     * @author Juci
     * @param id
     * @param userId
     */
	void deleteComment(Long id,Long userId);

}
