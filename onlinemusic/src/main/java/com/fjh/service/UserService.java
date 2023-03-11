package com.fjh.service;


import com.fjh.mapper.UserMapper;
import com.fjh.pojo.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;
    public User login(User user){
        return userMapper.login(user);
    }
    public User selectByName(String userName){
        return userMapper.selectByName(userName);
    }
    public int insert(User user){

        return   userMapper.insert(user);
    }

}
