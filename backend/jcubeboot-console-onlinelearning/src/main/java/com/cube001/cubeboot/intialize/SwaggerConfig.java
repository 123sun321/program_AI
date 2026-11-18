package com.cube001.cubeboot.intialize;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:张向宇
 * @Description:
 * @Date:Create in 10:08 2018/03/17
 * @Modified By:
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(prefix = "cubeboot", name = "swagger-open", havingValue = "true")
public class SwaggerConfig{

    private Predicate<RequestHandler> package1 = RequestHandlerSelectors.basePackage("com.cube001.cubeboot.base.controller");
    private Predicate<RequestHandler> package2 = RequestHandlerSelectors.basePackage("com.cube001.cubeboot.gradeCourse.controller");

    @Bean
    public Docket createRestApi() {
        //添加head参数start
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("Authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        tokenPar.name("app_id").description("租户").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
//               .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))                         //这里采用包含注解的方式来确定要显示的接口
                .apis(RequestHandlerSelectors.basePackage("com.cube001.cubeboot"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }



    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("JCubeBoot Doc")
                .description("JCubeBoot Api文档")
                .termsOfServiceUrl("http://www.cube001.com")
                .contact("zhangxiangyu")
                .version("2.0")
                .build();
    }



}
