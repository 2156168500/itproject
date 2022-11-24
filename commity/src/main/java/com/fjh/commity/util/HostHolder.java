package com.fjh.commity.util;

import com.fjh.commity.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 为了存储登录进来的用户的信息的类
 */
@Component
public class HostHolder {
    private static final Logger logger = LoggerFactory.getLogger(HostHolder.class);
    private ThreadLocal<User> threadLocal = new InheritableThreadLocal<>();
    public void setUser(User user){
        if(user != null){
            threadLocal.set(user);
        }else {
            logger.error("user is null");
        }
    }

    public User getUser(){
        return threadLocal.get();
    }

    public void clear(){
        threadLocal.remove();
    }
}
