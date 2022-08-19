package com.fjh.config;

import com.fjh.pojo.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        if(session == null){
            System.out.println("没有登录");
            return  false;
        }
        User user =(User) session.getAttribute("userInfo");
        if(user == null){
            System.out.println("没有登录");
            return  false;
        }
        return true;
    }
}
