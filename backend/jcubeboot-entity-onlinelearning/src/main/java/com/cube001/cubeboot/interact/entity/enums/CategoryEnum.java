package com.cube001.cubeboot.interact.entity.enums;


import com.baomidou.mybatisplus.enums.IEnum;

import java.io.Serializable;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: com.cube001.cubeboot.base.entity.enums
 * 能力要项分类枚举
 *
 * @author 陈乐乐
 * @version 1.0
 * @date 2025/11/29 15:13
 */
public enum CategoryEnum implements IEnum {
    CommunityReply(1,"社区回帖"),
    CourseEvaluation(2,"课程评价"),
    PathEvaluation(3,"路径评价"),
    Report(4,"报告评价");

    private int value;
    private String desc;

    private CategoryEnum(Integer value, String desc){
        this.value=value;
        this.desc=desc;
    }

    @Override
    public String toString() {
        return this.value+"";
    }

    @Override
    public Serializable  getValue(){
        return this.value;
    }
    public String getDesc(){
        return this.desc;
    }

    public static CategoryEnum create(int value){
        for(CategoryEnum categoryEnum:CategoryEnum.values()){
            Integer tmp= (Integer) categoryEnum.getValue();
            if(tmp.equals(value)){
                return  categoryEnum;
            }
        }
        throw new NullPointerException("未匹配到");
    }
}
