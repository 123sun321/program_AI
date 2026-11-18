package com.cube001.cubeboot.access.controller.dto;

import com.cube001.cubeboot.core.authorization.entity.Role;
import lombok.Data;

import java.util.List;

/**
 * @Author:张向宇
 * @Description:
 * @Date:Create in 14:08 2018/07/21
 * @Modified By:
 */
@Data
public class RoleWithPermissionDto {
    private Role role;

    private List<PermissionSimpleDto> permissions;
}
