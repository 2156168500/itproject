package com.fjh.service;

import com.fjh.mapper.UserMapper;
import com.fjh.pojo.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper ;
    public void insert(User user){
        userMapper.insert(user);
    }

    // 根据用户名, 来查询用户的详细信息. 用于登录功能
    public User selectByName(String username){
       return userMapper.selectByName(username);
    }

    // 总比赛场数 + 1, 获胜场数 + 1, 天梯分数 + 30
    public void userWin(int userId){
        userMapper.userWin(userId);
    }

    // 总比赛场数 + 1, 获胜场数 不变, 天梯分数 - 30
    public void userLose(int userId){
        userMapper.userLose(userId);
    }
}
