package com.cube001.cubeboot.console;

import com.cube001.cubeboot.core.authorization.entity.Tenant;
import com.cube001.cubeboot.core.domain.tenant.TenantFliter;
import org.junit.Test;

import java.io.Console;


public class        TenantFilterTest {
    @Test
   public void  ThreeCirtle(){
        try (TenantFliter tenantFliter=new TenantFliter(true,"10")){

            System.out.println(tenantFliter.getTenantId());

            try (TenantFliter tenantFliter1=new TenantFliter(true,"11")){
                System.out.println(tenantFliter1.getTenantId());

                try (TenantFliter tenantFliter2=new TenantFliter(true,"12")){
                    System.out.println(tenantFliter2.getTenantId());
                }
                System.out.println(tenantFliter1.getTenantId());
            }
            System.out.println(tenantFliter.getTenantId());

        }
   }
}
