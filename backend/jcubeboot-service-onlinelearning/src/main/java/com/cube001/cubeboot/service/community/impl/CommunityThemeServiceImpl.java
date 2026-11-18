package com.cube001.cubeboot.service.community.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.community.entity.CommunityTheme;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.interact.entity.Comment;
import com.cube001.cubeboot.service.community.CommunityThemeService;
import com.cube001.cubeboot.service.community.dto.CommunityThemeConverter;
import com.cube001.cubeboot.service.community.dto.CommunityThemeDto;
import com.cube001.cubeboot.service.community.mapper.CommunityThemeMapper;
import com.cube001.cubeboot.service.interact.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/***
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: CommunityThemeServiceImpl
 * 社区主题管理service实现类
 *
 * @author 张浩杰
 * @date   2019/3/21 15:06
 * @version 1.0
 */
@Service
public class CommunityThemeServiceImpl extends ServiceImpl<CommunityThemeMapper, CommunityTheme> implements CommunityThemeService {
    @Autowired
    private CommunityThemeConverter communityThemeConverter;
    @Autowired
    CommentMapper  commentMapper;
    /**
     * @description 删除/批量删除社区主题管理
     * @author 张浩杰
     * @date 2025/3/21 16:26
     * @param  ids
     * @return
     */
    @Override
    public void delete(List<Long> ids) {
        if(ids.size() > 0){
            for (Long id : ids) {
                this.baseMapper.deleteById(id);
            }
        }else {
            throw new FriendException("删除社区主题失败！");
        }
    }
   /**
     * @description 新增帖子
     * @author 张人杰
     * @date 2025/4/16 17:43
     * @Param
     * @return
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCommunityTheme(CommunityThemeDto communityThemeDto, Long userId) {
        CommunityTheme communityTheme = communityThemeConverter.dtoToCommunityTheme(communityThemeDto);

        //设置当前用户userId
        communityTheme.setUserId(userId);
        communityTheme.setClick(0);
        communityTheme.setCommentNumber(0);
        Integer insert = this.baseMapper.insert(communityTheme);
        if (insert<1){

            throw new FriendException("保存帖子失败！");
        }



    }
    /**
     * @description 修改帖子
     * @author 张浩杰
     * @date 2025/3/29 10:43
     * @param  communityThemeDto
     * @return
    */
    @Override
    public void editCommunityTheme(CommunityThemeDto communityThemeDto) {
        CommunityTheme communityTheme = communityThemeConverter.dtoToCommunityTheme(communityThemeDto);
        this.baseMapper.updateById(communityTheme);
    }
    /**
     * @description 根据id查询帖子信息
     * @author 张浩杰
     * @date 2025/3/29 14:22
     * @param id
     * @return CommunityThemeDto
     */
    @Override
    public CommunityThemeDto getThemeById(Long id) {
        CommunityThemeDto communityThemeDto = new CommunityThemeDto();
        CommunityTheme communityTheme = this.baseMapper.getThemeById(id);
        if(null != communityTheme){
            communityThemeDto = communityThemeConverter.communityThemeDto(communityTheme);
        }
        return communityThemeDto;
    }

    /**
     * @description 查看增加浏览量
     * @author 张浩杰
     * @date 2025/3/22 17:44
     * @param  communityThemeId
     * @return
     */
    @Override
    public void addClick(Long communityThemeId) {
        CommunityTheme communityTheme = this.baseMapper.selectById(communityThemeId);
        if(communityTheme != null){
            this.baseMapper.addCount(communityTheme.getId());
        }else {
            throw new FriendException("帖子不存在！");
        }
    }
    /**
     * @description 验证
     * @author 张浩杰
     * @date 2025/3/29 15:28
     * @param col,value
     * @return Boolean
     */
    @Override
    public Integer getCommunityTheme(String col, String value) {
        EntityWrapper<CommunityTheme> wrapper= new EntityWrapper<>();
        wrapper.eq(col,value);
        return baseMapper.selectCount(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteThemeComment(Long commentId) {
        Wrapper<CommunityTheme> commentWrapper=new EntityWrapper<>();
        Comment comment = commentMapper.selectById(commentId);
        commentMapper.deleteById(commentId);
        baseMapper.reduceCount(comment.getLinkId());
    }

    /**
     * @description  用户获取自己发的帖子
     * @author 马勇超
     * @date 2025/4/24 10:58
     * @param   page,wrapper
     * @return
     */
    @Override
    public Page<CommunityTheme> selectPageByStudent(Page page, Wrapper<CommunityTheme> wrapper) {
        List<CommunityTheme> communityThemes = baseMapper.selectPageByStudent(page,wrapper.eq("a.is_deleted",0));
        page.setRecords(communityThemes);
        return page;
    }

    /**
     * @description  获取所有帖子
     * @author 马勇超
     * @date 2025/4/24 17:40
     * @param   page,wrapper
     */
    @Override
    public Page<CommunityTheme> selectAllThemePage(Page<CommunityTheme> page, Wrapper<CommunityTheme> wrapper) {
        List<CommunityTheme> communityThemes = baseMapper.selectAllThemePage(page,wrapper.eq("a.is_deleted",0));
        page.setRecords(communityThemes);
        return page;
    }
    /**
     * @description  获取用户个人的回复帖子主题和回复内容
     * @author 马勇超
     * @date 2025/4/24 15:00
     * @param   page,wrapper
     * @return
     */
    @Override
    public Page<CommunityTheme> selectBySelfReply(Page page, Wrapper<CommunityTheme> wrapper) {
        List<CommunityTheme> communityThemeList = baseMapper.selectBySelfReply(page,wrapper.eq("a.is_deleted",0));
        page.setRecords(communityThemeList);
        return page;
    }
}
