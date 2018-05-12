package com.musixise.musixisebox.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaowei on 2018/4/6.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private ParameterBuilder getParameterBuilder() {
        ParameterBuilder aParameterBuilder = new ParameterBuilder();
        aParameterBuilder
                .parameterType("query") //参数类型支持header, cookie, body, query etc
                .name("access_token") //参数名
                .defaultValue("") //默认值
                .description("access_token ")
                .modelRef(new ModelRef("string"))//指定参数值的类型
                .required(false).build(); //非必需，这里是全局配置，然而在登陆的时候是不用验证的

        return aParameterBuilder;
    }

    @Bean
    public Docket FrontEndApi(){

        List<Parameter> aParameters = new ArrayList<Parameter>();
        aParameters.add(getParameterBuilder().build());


        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("v1")
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(true)
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.musixise.musixisebox"))
                .paths(PathSelectors.ant("/api/v1/**"))
                .build().apiInfo(apiInfo())
                .globalOperationParameters(aParameters);

    }

    @Bean
    public Docket BackEndApi(){

        List<Parameter> aParameters = new ArrayList<Parameter>();
        aParameters.add(getParameterBuilder().build());

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("admin")
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(true)
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.musixise.musixisebox"))
                .paths(PathSelectors.ant("/api/v1/admin/**")).build()
                .apiInfo(apiAdminInfo())
                .globalOperationParameters(aParameters);
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("MUSIXISE.COM RESTful APIs")
                .description("前台接口")
                .termsOfServiceUrl("github-url")
                .contact(new Contact("zhaowei, zfan40", "http://www.musixise.com", "developer@musixise.com"))
                .version("1.0.0")
                .build();
    }

    private ApiInfo apiAdminInfo(){
        return new ApiInfoBuilder()
                .title("MUSIXISE.COM ADMIN RESTful APIs")
                .description("后台接口")
                .termsOfServiceUrl("github-url")
                .contact(new Contact("zhaowei, zfan40", "http://www.musixise.com", "developer@musixise.com"))
                .version("1.0.0")
                .build();
    }
}
