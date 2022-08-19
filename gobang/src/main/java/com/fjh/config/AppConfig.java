package com.fjh.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {
    @Bean
 public    BCryptPasswordEncoder getBCryptPasswordEncoder(){
     return new BCryptPasswordEncoder();
 }
 @Bean
    public ObjectMapper getObjectMapper(){
        return new ObjectMapper();
 }

    public void addInterceptors(InterceptorRegistry registry) {
        LoginInterceptor loginInterceptor = new LoginInterceptor();
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                //排除所有的JS
                .excludePathPatterns("/js/**.js")
                //排除images下所有的元素
                .excludePathPatterns("/image/**")
                .excludePathPatterns("/css/**.css")
                .excludePathPatterns("/login.html") //排除登录接口
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/register");

    }

}
