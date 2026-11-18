package com.cube001.cubeboot.service.community.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.community.entity.CommunityTheme;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/***
 * Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: CommunityThemeMapper
 * 社区主题管理mapper
 *
 * @author anonymous
 * @date   2019/3/21 15:08
 * @version 1.0
 */
public interface CommunityThemeMapper extends BaseMapper<CommunityTheme> {

    /**
    * @description  增加帖子浏览量
    * @author anonymous
    * @date 2025/4/26 8:34
    * @param   communityThemeId
    * @return
    */
    void addCount(@Param("id") Long communityThemeId);
    void  reduceCount(@Param("id") Long commentNumber);

    /**
     * @description  用户获取自己发的帖子
     * @author anonymous
     * @date 2025/4/24 10:58
     * @param   page,wrapper
     * @return
     */
    List<CommunityTheme> selectPageByStudent(Page page,@Param("ew") Wrapper<CommunityTheme> wrapper);
    /**
     * @description  获取所有帖子
     * @author anonymous
     * @date 2025/4/24 17:40
     * @param   page,wrapper
     */
    List<CommunityTheme> selectAllThemePage(Page<CommunityTheme> page,@Param("ew") Wrapper<CommunityTheme> wrapper);

    /**
    * @description  根据帖子id查询信息
    * @author anonymous
    * @date 2025/4/25 10:41
    * @param   id
    * @return
    */
    CommunityTheme getThemeById(@Param("id") Long id);

    /**
    * @description  增加帖子评论数
    * @author anonymous
    * @date 2025/4/26 8:39
    * @param
    */
    void addComment(@Param("id") Long id,@Param("count") Integer count);
    /**
     * @description  获取用户个人的回复帖子主题和回复内容
     * @author anonymous
     * @date 2025/4/24 15:00
     * @param   page,wrapper
     * @return
     */
    List<CommunityTheme> selectBySelfReply(Page page,@Param("ew") Wrapper<CommunityTheme> wrapper);
}
