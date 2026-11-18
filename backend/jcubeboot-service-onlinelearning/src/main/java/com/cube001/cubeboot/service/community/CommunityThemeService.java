package com.cube001.cubeboot.service.community;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.cube001.cubeboot.community.entity.CommunityTheme;
import com.cube001.cubeboot.service.community.dto.CommunityThemeDto;

import java.util.List;

/***
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: CommunityThemeService
 * 社区主题管理service
 *
 * @author 张浩杰
 * @date   2019/3/21 15:04
 * @version 1.0
 */
public interface CommunityThemeService extends IService<CommunityTheme> {
    /**
     * @description 删除/批量删除社区主题管理
     * @author 张浩杰
     * @date 2025/3/21 16:26
     * @param  ids
     * @return
     */
    void delete(List<Long> ids);
    /**
     * @description 添加/新增帖子
     * @author 张浩杰
     * @date 2025/3/22 13:47
     * @param communityThemeDto,userId
     * @return
     */
    void addCommunityTheme(CommunityThemeDto communityThemeDto, Long userId);
    /**
     * @description 修改帖子
     * @author 张浩杰
     * @date 2025/3/29 10:43
     * @param communityThemeDto
     * @return
    */
    void editCommunityTheme(CommunityThemeDto communityThemeDto);
    /**
     * @description 根据id查询帖子信息
     * @author 张浩杰
     * @date 2025/3/29 14:22
     * @param id
     * @return CommunityThemeDto
    */
    CommunityThemeDto getThemeById(Long id);
    /**
     * @description 查看增加浏览量
     * @author 张浩杰
     * @date 2025/3/22 17:44
     * @param  communityThemeId
     * @return
     */
    void addClick(Long communityThemeId);
    /**
     * @description 验证
     * @author 张浩杰
     * @date 2025/3/29 15:28
     * @param col,value
     * @return Boolean
     */
    Integer getCommunityTheme(String col,String value);

    /**
      * @description删除社区帖子评论
      * @author 张人杰
      * @date 2025/4/19 17:25
      * @Param Long theme
      * @return
      */
    void  deleteThemeComment(Long theme);

    /**
     * @description  用户获取自己发的帖子
     * @author 马勇超
     * @date 2025/4/24 10:58
     * @param   page,wrapper
     * @return
     */
    Page<CommunityTheme> selectPageByStudent(Page page, Wrapper<CommunityTheme> wrapper);

    /**
    * @description  获取所有帖子
    * @author 马勇超
    * @date 2025/4/24 17:40
    * @param   page,wrapper
    */
    Page<CommunityTheme> selectAllThemePage(Page<CommunityTheme> page, Wrapper<CommunityTheme> wrapper);

    /**
     * @description  获取用户个人的回复帖子主题和回复内容
     * @author 马勇超
     * @date 2025/4/24 15:00
     * @param   page,wrapper
     * @return
     */
    Page<CommunityTheme> selectBySelfReply(Page page, Wrapper<CommunityTheme> wrapper);
}