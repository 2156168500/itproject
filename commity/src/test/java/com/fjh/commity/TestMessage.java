package com.fjh.commity;

import com.fjh.commity.dao.MessageMapper;
import com.fjh.commity.entity.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest(classes = CommityApplication.class)
@RunWith(SpringRunner.class)
public class TestMessage {
    @Resource
    private MessageMapper messageMapper;
    @Test
    public void testSelectAll(){
        List<Message> messages = messageMapper.selectConversations(111, 0, 20);
        for (Message message : messages){
            System.out.println(message);
        }

        int count = messageMapper.selectConversationCount(111);
        System.out.println(count);
    }
    @Test
    public void testLetters(){
        List<Message> messages = messageMapper.selectLetters("111_112", 0, 20);
        for (Message message : messages){
            System.out.println(message);
        }
        int count = messageMapper.selectLetterCount("111_112");
        System.out.println(count);

        int count1 = messageMapper.selectLetterUnreadCount(111, null);
        System.out.println(count1);
    }
}
