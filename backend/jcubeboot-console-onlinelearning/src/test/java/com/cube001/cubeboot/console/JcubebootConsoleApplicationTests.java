package com.cube001.cubeboot.console;


import com.cube001.cubeboot.JcubebootConsoleApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JcubebootConsoleApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WebAppConfiguration
public class JcubebootConsoleApplicationTests {

	@Test
	public void contextLoads() {

	}

	@Test
	public  void  TestUserLombok()
	{
		/*User u1=new User();
		u1.setName("anonymous");
		System.out.println(u1.toString());*/
	}
	//@Test
	public  void InsertArticle()
	{

	}

}
