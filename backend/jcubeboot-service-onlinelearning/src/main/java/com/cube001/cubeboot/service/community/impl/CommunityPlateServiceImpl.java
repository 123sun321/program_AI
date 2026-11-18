package com.cube001.cubeboot.service.community.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.cube001.cubeboot.community.entity.CommunityPlate;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cube001.cubeboot.core.authorization.entity.UserRole;
import com.cube001.cubeboot.core.authorization.mapper.UserroleMapper;
import com.cube001.cubeboot.core.authorization.service.UserroleService;
import com.cube001.cubeboot.core.exception.FriendException;
import com.cube001.cubeboot.service.community.CommunityPlateService;
import com.cube001.cubeboot.service.community.dto.CommunityPlateConverter;
import com.cube001.cubeboot.service.community.dto.CommunityPlateDto;
import com.cube001.cubeboot.service.community.mapper.CommunityPlateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
* Copyright (C), 2018 - 2019, ZhengZhouChuangZhi. Co., Ltd.
* FileName: CommunityPlateServiceImpl
* 社区板块service实现类
*
* @author anonymous
* @date   2019/3/19 16:18
* @version 1.0
*/
@Service
public class CommunityPlateServiceImpl extends ServiceImpl<CommunityPlateMapper, CommunityPlate> implements CommunityPlateService {
    @Autowired
    private CommunityPlateConverter communityPlateConverter;

    @Autowired
    private UserroleMapper userroleMapper;

    /**
     * @description 删除/批量删除社区板块
     * @author anonymous
     * @date  2019年3月19日 17:36:08
     * @param  ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> ids) {
        if(ids.size() > 0){
            for (Long id : ids) {
                this.deleteById(id);
            }
        }else {
            throw new FriendException("删除社区板块失败！");
        }
    }
    /**
     * @description 编辑修改社区板块
     * @author anonymous
     * @date  2019年3月19日 17:50:41
     * @param  communityPlateDto
     * @return
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(CommunityPlateDto communityPlateDto) {
        Boolean result = null;
        //把dto转换为实体类
        CommunityPlate communityPlateNew = communityPlateConverter.dtoToCommunityPlate(communityPlateDto);
        //获取传入的排序编号
        Integer sortNew = communityPlateNew.getSort();
        //旧模板数据
        CommunityPlate communityPlateOld = this.selectById(communityPlateNew.getId());
        //通过传入的排序编号查询数据库是否有相同的编号数据
        CommunityPlate communityPlate = baseMapper.getCommunityPlateBySort(sortNew);
        if(communityPlate != null){
            //有相同排序编号数据,把旧数据排序编号保存
            communityPlate.setSort(communityPlateOld.getSort());
            baseMapper.updateById(communityPlate);
            this.updateById(communityPlateNew);
        }else if(communityPlate == null){
            this.updateById(communityPlateNew);
        }else{
            throw new FriendException("保存社区板块失败！");
        }
    }
    /**
     * @description 添加社区模块
     * @author anonymous
     * @date  2019年3月20日 15:17:52
     * @param  communityPlateDto
     * @return
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(CommunityPlateDto communityPlateDto) {
        //把dto转为实体类
        CommunityPlate communityPlate = communityPlateConverter.dtoToCommunityPlate(communityPlateDto);
        List<CommunityPlate> communityPlateList = this.baseMapper.selectList(new EntityWrapper<CommunityPlate>());
        for (CommunityPlate plate : communityPlateList) {
            if(communityPlate.getSort().equals(plate.getSort())){
                throw new FriendException("排序编号已存在！");
            }
        }
        this.baseMapper.insert(communityPlate);
    }
    /**
     * @description 通过角色获取权限社区板块
     * @author anonymous
     * @date 2025年3月22日 13:45:31
     * @param
     * @return communityPlateList
     */
    @Override
    public Map<Object,String> getPlateByRole(Long userId) {
        UserRole userRole=new UserRole();
        userRole.setUserId(userId);
        Map<Object,String> listPlateIdAndName = new HashMap<>();
        List<CommunityPlate> communityPlateList = this.baseMapper.selectList(new EntityWrapper<CommunityPlate>());
        for (CommunityPlate communityPlate : communityPlateList) {
            if(communityPlate.getRoles().contains(",")){
                String[] splits = communityPlate.getRoles().split(",");
                for (String split : splits) {
                    if(split.equals(String.valueOf(userroleMapper.selectOne(userRole).getRoleId()))){
                        listPlateIdAndName.put(communityPlate.getId(),communityPlate.getName());
                    }
                }
            }else if(communityPlate.getRoles().equals(String.valueOf(userroleMapper.selectOne(userRole).getRoleId()))){
                listPlateIdAndName.put(communityPlate.getId(),communityPlate.getName());
            }
        }
        System.out.println(listPlateIdAndName);
        return listPlateIdAndName;
    }
    /**
     * @description 验证排序是否重复
     * @author anonymous
     * @date 2025/3/22 16:21
     * @param  col,value
     * @return
     */
    @Override
    public Integer getCommunityPlateSort(String col, String value) {
        EntityWrapper<CommunityPlate> wrapper= new EntityWrapper<>();
        wrapper.eq(col,value);
        return baseMapper.selectCount(wrapper);
    }


}
