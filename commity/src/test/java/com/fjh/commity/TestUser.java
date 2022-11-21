package com.fjh.commity;

import com.fjh.commity.dao.UserMapper;
import com.fjh.commity.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest(classes = CommityApplication.class)
@RunWith(SpringRunner.class)
public class TestUser {
    @Resource
    private UserMapper userMapper;
    @Test
    public void testSelect(){
        User user = userMapper.selectById(101);
        System.out.println(user);
        user = userMapper.selectByUsername("liubei");
        System.out.println(user);
        user = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user);
    }
}
