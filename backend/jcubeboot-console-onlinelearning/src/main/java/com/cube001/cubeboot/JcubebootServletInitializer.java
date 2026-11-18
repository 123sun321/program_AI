package com.cube001.cubeboot;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @Author:张向宇
 * @Description:
 * @Date:Create in 8:52 2018/04/27
 * @Modified By:
 */
public class JcubebootServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(JcubebootConsoleApplication.class);
    }
}
