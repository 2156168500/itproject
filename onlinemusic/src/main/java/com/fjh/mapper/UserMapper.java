package com.fjh.mapper;

import com.fjh.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User login(User User);
    User selectByName(String username);
    int insert(User user);

}
