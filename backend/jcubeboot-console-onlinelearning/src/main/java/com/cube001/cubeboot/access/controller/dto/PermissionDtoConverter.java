package com.cube001.cubeboot.access.controller.dto;

import com.cube001.cubeboot.core.authorization.entity.Permission;
import org.mapstruct.Mapper;

/**
 * @Author:张向宇
 * @Description:
 * @Date:Create in 9:46 2018/07/13
 * @Modified By:
 */
@Mapper(componentModel = "spring")
public interface PermissionDtoConverter {
    Permission editDtoToEntity(PermissionEditDto permissionEditDto);
}
