package com.cube001.cubeboot.service.base;


/**
 * Copyright (C),2018-2019, ZhengZhouChuangZhi. Co.,Ltd.
 * FileName: [RoleExtendService]
 * 角色扩展接口
 *
 * @author Juci
 * @date 2025-01-17
 * @version 1.0
 */
public interface RoleExtendService {
    /**
     * 设置学校管理员角色
     * @param userid 用户编号
     * @return
     */
    void setSchoolManagerRole(Long userid);

    /**
     * 设置学生角色
     * @param userid 用户编号
     * @return
     */
    void setStudentRole(Long userid);

    /**
     * 设置教师角色
     * @param userid 用户编号
     * @return
     */
    void setTeacherRole(Long userid);

    /**
     * 设置企业角色
     * @param userid
     */
    void setCompanyRole(Long userid);

    /**
     * 删除学校管理员角色
     * @param userid 用户编号
     * @return
     */
    void deleteSchoolManagerRole(Long userid);

    /**
     * 删除学生角色
     * @param userid 用户编号
     * @return
     */
    void deleteStudentRole(Long userid);

    /**
     * 删除教师角色
     * @param userid 用户编号
     * @return
     */
    void deleteTeacherRole(Long userid);

    /**
     * 删除企业角色
     * @param userid
     */
    void deleteCompanyRole(Long userid);
}
