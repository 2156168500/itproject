package com.fjh.mapper;

import com.fjh.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    //插入数据,用于注册
    int insert(User user);
    //查询,用于登录
    User selectByUsername(String username);
    void userWin(int winUserId);
    void userLose(int loseUserId);
}
