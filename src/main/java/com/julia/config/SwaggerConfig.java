package com.julia.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;


/**
 * @program: julia
 * @description: SwaggerConfig
 * @Auther: chowel.master
 * @create: 2021-11-09
 **/
@EnableSwagger2
@Configuration
public class SwaggerConfig implements WebMvcConfigurer {
    @Value(value = "${swagger.enabled}")
    Boolean swaggerEnabled;

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/", "classpath:/resources/",
            "classpath:/static/", "classpath:/public/"};

    private static final String[] SWAGGER_URL = {"/swagger-ui.html", "/webjars/**", "/swagger-resources/**", "/error", "/csrf"};

    @Bean
    public Docket createRestApi() {
        ParameterBuilder platformToken = new ParameterBuilder();
        ParameterBuilder customerToken = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        platformToken.name("julia-admin").description("管理后台").modelRef(new ModelRef("string")).parameterType("header")
                .required(false).build();
        customerToken.name("julia-play").description("玩家").modelRef(new ModelRef("string")).parameterType("header")
                .required(false).build();
        pars.add(platformToken.build());
        ResponseMessage message500 = new ResponseMessageBuilder()
                .code(500)
                .message("服务器内部异常")
                .responseModel(new ModelRef("Error"))
                .build();
        ResponseMessage message510 = new ResponseMessageBuilder()
                .code(510)
                .message("业务逻辑异常(可直接提示)")
                .responseModel(new ModelRef("Error"))
                .build();
        List<ResponseMessage> responseMessageList = new ArrayList<>();
        responseMessageList.add(message500);
        responseMessageList.add(message510);
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                // 是否开启
                .enable(swaggerEnabled).select()
                // 扫描的路径包
                .apis(RequestHandlerSelectors.basePackage("com.julia.controller"))
                // 指定路径处理PathSelectors.any()代表所有的路径
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars)
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, responseMessageList);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("瑶瑶领先")
                .description("瑶瑶领先，瑶瑶3000")
                // 作者信息
                .contact(new Contact("大师", "https://github.com/chowel", "lipbet@gmail.com"))
                .version("1.0.0")
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!registry.hasMappingForPattern("/webjars/**")) {
            registry.addResourceHandler("/webjars/**").addResourceLocations(
                    "classpath:/META-INF/resources/webjars/");
        }
        if (!registry.hasMappingForPattern("/**")) {
            registry.addResourceHandler("/**").addResourceLocations(
                    CLASSPATH_RESOURCE_LOCATIONS);
        }
    }

}
