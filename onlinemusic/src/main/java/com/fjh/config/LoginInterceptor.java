package com.fjh.config;

import com.fjh.totals.Constant;
import com.fjh.totals.ResponseBodyMessage;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //检查是否登录
        HttpSession session = request.getSession();
        if(session == null || session.getAttribute(Constant.USERINFO_SESSION_KEY) == null){
            System.out.println("没有登录");
            return false;
        }
        return true;
    }
}
