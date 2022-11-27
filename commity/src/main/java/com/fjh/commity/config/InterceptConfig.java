package com.fjh.commity.config;
import com.fjh.commity.controller.intercept.LoginIntercept;
import com.fjh.commity.controller.intercept.LoginRequiredInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class InterceptConfig implements WebMvcConfigurer {
    @Autowired
    private LoginIntercept loginIntercept;
    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginIntercept).excludePathPatterns("/**/css.*")
                .excludePathPatterns("/**/js.*")
                .excludePathPatterns("/**/img.*");

        registry.addInterceptor(loginRequiredInterceptor).excludePathPatterns("/**/css.*")
                .excludePathPatterns("/**/js.*")
                .excludePathPatterns("/**/img.*");
    }
}
