package com.fjh.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@Configuration
public class AppConfig {
    @Bean
 public    BCryptPasswordEncoder getBCryptPasswordEncoder(){
     return new BCryptPasswordEncoder();
 }
 @Bean
    public ObjectMapper getObjectMapper(){
        return new ObjectMapper();
 }

}
