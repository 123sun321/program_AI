package com.cube001.cubeboot.approve.entity.enums;

import com.baomidou.mybatisplus.enums.IEnum;

import java.io.Serializable;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: ApproveEnum.java
 * 通用点赞的枚举类,用于枚举不同的点赞类型
 *
 * @author anonymous
 * @version 1.0
 * @date 2025/4/2 15:33
 */
public enum ApproveEnum implements IEnum {

    TrainingApprove(0,"练习记录点赞"),

    ExcellentWorkApprove(1,"优秀作品点赞");

    private int value;
    private String desc;

    private ApproveEnum(Integer value, String desc){
        this.value=value;
        this.desc=desc;
    }
    @Override
    public Serializable getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public String getDesc() {
        return desc;
    }

    public static ApproveEnum create(int value){
        for(ApproveEnum approveEnum:ApproveEnum.values()){
            Integer tmp= (Integer) approveEnum.getValue();
            if(tmp.equals(value)){
                return  approveEnum;
            }
        }
        throw new NullPointerException("未匹配到");
    }

}
