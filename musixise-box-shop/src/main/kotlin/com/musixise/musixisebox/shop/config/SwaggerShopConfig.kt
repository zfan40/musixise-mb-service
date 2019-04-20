package com.musixise.musixisebox.shop.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.ParameterBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.schema.ModelRef
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.service.Parameter
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.util.*

@Configuration
@EnableSwagger2
@Profile("!test")
class SwaggerShopConfig {
    private fun getParameterBuilder(): ParameterBuilder {
        val aParameterBuilder = ParameterBuilder()
        aParameterBuilder
            .parameterType("query") //参数类型支持header, cookie, body, query etc
            .name("access_token") //参数名
            .defaultValue("") //默认值
            .description("access_token ")
            .modelRef(ModelRef("string"))//指定参数值的类型
            .required(false).build() //非必需，这里是全局配置，然而在登陆的时候是不用验证的

        return aParameterBuilder
    }

    @Bean
    fun ShopFrontEndApi(): Docket {

        val aParameters = ArrayList<Parameter>()
        aParameters.add(getParameterBuilder().build())

        return Docket(DocumentationType.SWAGGER_2)
            .groupName("v1-shop")
            .useDefaultResponseMessages(true)
            .forCodeGeneration(true)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.musixise.musixisebox.shop"))
            .paths(PathSelectors.ant("/api/v1/**"))
            .build().apiInfo(apiInfo())
            .globalOperationParameters(aParameters)

    }

    @Bean
    fun ShopBackEndApi(): Docket {

        val aParameters = ArrayList<Parameter>()
        aParameters.add(getParameterBuilder().build())

        return Docket(DocumentationType.SWAGGER_2)
            .groupName("admin-shop")
            .useDefaultResponseMessages(true)
            .forCodeGeneration(true)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.musixise.musixisebox.shop"))
            .paths(PathSelectors.ant("/api/v1/admin/**")).build()
            .apiInfo(apiAdminInfo())
            .globalOperationParameters(aParameters)
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
            .title("MUSIXISE.COM RESTful APIs")
            .description("购物接口")
            .termsOfServiceUrl("github-url")
            .contact(Contact("zhaowei, zfan40", "http://www.musixise.com", "developer@musixise.com"))
            .version("1.0.0")
            .build()
    }

    private fun apiAdminInfo(): ApiInfo {
        return ApiInfoBuilder()
            .title("MUSIXISE.COM ADMIN RESTful APIs")
            .description("后台接口")
            .termsOfServiceUrl("github-url")
            .contact(Contact("zhaowei, zfan40", "http://www.musixise.com", "developer@musixise.com"))
            .version("1.0.0")
            .build()
    }
}