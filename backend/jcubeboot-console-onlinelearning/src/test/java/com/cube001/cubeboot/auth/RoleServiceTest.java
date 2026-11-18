package com.cube001.cubeboot.auth;

import com.cube001.cubeboot.core.authorization.entity.Permission;
import com.cube001.cubeboot.core.authorization.entity.PermissionRelation;
import com.cube001.cubeboot.core.authorization.entity.Role;
import com.cube001.cubeboot.core.authorization.service.PermissionService;
import com.cube001.cubeboot.core.authorization.service.RoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:张向宇
 * @Description:
 * @Date:Create in 14:39 2018/06/27
 * @Modified By:
 */
@RunWith(SpringRunner.class)
@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RoleServiceTest {
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    @Test
    public void GetPermissionByRoleName() {
        PermissionRelation permissionRelation=new PermissionRelation();
        //permissionRelation.setTenantId()
        //permissionRelation.setRoleId()
       // List<Permission> permissions=permissionService.findPermissionsByRoleId(2);
        //permissions=permissionService.findPermissionsByRoleId(2);

        //permissions=permissionService.findPermissionsByRoleName("frontUser");
    }

    @Test
    public void UpdatePermissionTest()
    {
//        List<Permission> permissions=new ArrayList<Permission>();
//        Permission permission1=new Permission();
//        permission1.setName("user");
//        permission1.setPermissionRelation(new PermissionRelation().setIsGranted(true));
//
//        permissions.add(permission1);
////        Permission permission2=new Permission();
////        permission2.setName("user.add");
////        permission2.setPermissionRelation(new PermissionRelation().setIsGranted(false));
////
////        permissions.add(permission2);
//
//        permissionService.SetRolePermissions(2,permissions);

    }

    @Test
    public void UserRoleTest()
    {
        //List<Role> roles=roleService.getRolesByUserId(1L);
    }
}
