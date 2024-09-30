package com.julia.config;

import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.julia.tool.AdminToken;
import com.julia.tool.PlayerToken;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: julia
 * @description: SaTokenConfigure
 * @Auther: brody
 * @create: 2021-11-09
 **/
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {
    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册Sa-Token的路由拦截器
        registry.addInterceptor(new SaRouteInterceptor((req, res, handler)->{
            SaRouter.match("/julia/profess/**", AdminToken::checkLogin);
            SaRouter.match("/julia/v2/**", PlayerToken::checkLogin);
        })).addPathPatterns("/**").excludePathPatterns("/swagger-resources/**","/swagger-**","/swagger-ui.**",
                "/julia/api/**");
    }
}
