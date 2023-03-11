package com.fjh.commity.util;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public final class CookieUtil {
    public static String getValue(HttpServletRequest request,String name){
        Cookie[] cookies = request.getCookies();
        if(cookies == null ) return null;
        for(Cookie cookie : cookies){
            if(cookie.getName().equals(name)){
                return cookie.getValue();
            }
        }
        return null;
    }
}
