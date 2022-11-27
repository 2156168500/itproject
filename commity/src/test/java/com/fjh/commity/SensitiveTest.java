package com.fjh.commity;

import com.fjh.commity.util.SensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = CommityApplication.class)
@RunWith(SpringRunner.class)
public class SensitiveTest {
    @Autowired
    private SensitiveFilter sensitiveFilter;
    @Test
    public void test(){
        String text = "这里有☆赌☆博☆，☆嫖☆娼☆，☆吸☆毒☆，☆开☆票☆，哈哈哈";
        String dest = sensitiveFilter.filter(text);
        System.out.println(dest);
    }
}
