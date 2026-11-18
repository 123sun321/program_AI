package com.cube001.cubeboot;

import com.cube001.cubeboot.intialize.WebSocketConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;


@SpringBootApplication(scanBasePackages = {"com.cube001.cubeboot"})
@EnableRetry
@EnableScheduling
public class JcubebootConsoleApplication {
	private final static Logger logger= LoggerFactory.getLogger(JcubebootConsoleApplication.class);

	public static void main(String[] args) {

		SpringApplication.run(JcubebootConsoleApplication.class, args);
		logger.info("jcubebootApplication started");
	}

}
