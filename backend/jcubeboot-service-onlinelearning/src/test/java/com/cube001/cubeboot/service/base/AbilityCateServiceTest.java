package com.cube001.cubeboot.service.base;

import com.cube001.cubeboot.service.AbstractShiroTest;
import com.cube001.cubeboot.service.ServiceTestApplication;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.easymock.EasyMock.createNiceMock;

/**
 * Copyright (C), 2018-2019, ZhengZhouChuangZhi. Co., Ltd.
 * FileName: AbilityCateServiceTest.java
 * 能力要项的单元测试
 *
 * @author 张向宇
 * @version 1.0
 * @date 2025/12/18 13:55
 */
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {"dev"})
@SpringBootTest(classes= ServiceTestApplication.class)
public class AbilityCateServiceTest extends AbstractShiroTest {

//    @Autowired
//    private AbilityCateService abilityCateService;

    /**
     * 测试获取所有能力要项
     */
    @Test
    @Transactional
    @Rollback(true)// 事务自动回滚，默认是true。可以不写
    public void getAll()
    {
//        //1. 创建模拟的认证Subject实例
//        Subject subjectUnderTest = createNiceMock(Subject.class);
//        expect(subjectUnderTest.isAuthenticated()).andReturn(true).anyTimes();
//        expect(subjectUnderTest.getPrincipal()).andReturn("{id:1,account:\"admin\",name:\"管理员\",tenantid:123}").anyTimes();
//        EasyMock.replay(subjectUnderTest);
//        //2. 将认证实例加入当前线程
//        setSubject(subjectUnderTest);
//        //取所有
//        List<AbilityCate> list= abilityCateService.selectList( new EntityWrapper<>());
//        Assert.assertTrue(list.size()>0);
//        //加租户过滤
//        TenantFliter tenantFliter=new TenantFliter(true,"123"); //设置租户id为123
//        List<AbilityCate> listWidthTenantId= abilityCateService.selectList( new EntityWrapper<>());
//        Assert.assertTrue(listWidthTenantId.size()>0);
    }

    @Test
    @Transactional
    @Rollback(true)// 事务自动回滚，默认是true。可以不写
    public void insertAndDelete()
    {
//        //1. 创建模拟的认证Subject实例
//        Subject subjectUnderTest = createNiceMock(Subject.class);
//        expect(subjectUnderTest.isAuthenticated()).andReturn(true).anyTimes();
//        expect(subjectUnderTest.getPrincipal()).andReturn("{id:1,account:\"admin\",name:\"管理员\",tenantid:123}").anyTimes();
//        EasyMock.replay(subjectUnderTest);
//        //2. 将认证实例加入当前线程
//        setSubject(subjectUnderTest);
//
//
//        AbilityCate abilityCate=new AbilityCate();
//        abilityCate.setName("测试数据111");
//        abilityCate.setSort(10);
//        abilityCate.setSummary("5");
//        abilityCate.setUpScore(3);
//        abilityCate.setSystem(true);
//        abilityCate.setPeriod(1);
//        abilityCate.setFormula("test");
//        abilityCateService.insert(abilityCate);
//
//        //查找
//        AbilityCate abilityCate_query= abilityCateService.selectOne( new EntityWrapper<AbilityCate>().eq("name","测试数据111"));
//        Assert.assertEquals(abilityCate,abilityCate_query);

    }

    @After
    public void tearDownSubject2() {
        //3. Unbind the subject from the current thread:
        clearSubject();
    }

}