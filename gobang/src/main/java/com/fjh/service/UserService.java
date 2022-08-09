package com.fjh.service;

import com.fjh.mapper.UserMapper;
import com.fjh.pojo.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service
public class UserService {
    @Resource
    private UserMapper userMapper;
    public int insert(User user){
        return userMapper.insert(user);
    }
    public User selectByUsername(String username){
        return userMapper.selectByUsername(username);
    }
}
