package com.fjh.commity.service;

import com.fjh.commity.dao.UserMapper;
import com.fjh.commity.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;
    public User findUserById(String userId) {
        return userMapper.selectById(Long.parseLong(userId));
    }
}
