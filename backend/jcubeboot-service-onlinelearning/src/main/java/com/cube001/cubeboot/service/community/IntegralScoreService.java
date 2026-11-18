package com.cube001.cubeboot.service.community;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cube001.cubeboot.community.entity.IntegralScore;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>   查询个人积分
 *
 * @author anonymous
 * @since 2019-03-18
 */
public interface IntegralScoreService extends IService<IntegralScore> {

    /**
     *功能描述      返回 本周，本月，全部 个人积分
     * @author     anonymous
     * @date       2019/3/23
     * @param
     * @return
    */
    Map<String, Integer> selectPersonalScore(long studentId);
    /**
     *
     * @description 对某个学生添加一条积分记录,然后同步添加积分到student表
     * @author anonymous
     * @date 2025/4/17 17:18
     * @param userId 用户编号
     * @param name 积分类型
     * @param num 积分数
     * @return boolean
     */
    boolean add(Long userId,String name,Integer num,Long studentId);
}
