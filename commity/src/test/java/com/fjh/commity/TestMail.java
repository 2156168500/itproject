package com.fjh.commity;

import com.fjh.commity.entity.DiscussPost;
import com.fjh.commity.service.DiscussPostService;
import com.fjh.commity.util.MailConnect;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@SpringBootTest(classes = CommityApplication.class)
@RunWith(SpringRunner.class)
public class TestMail {
    @Autowired
    private MailConnect mailConnect;
    @Autowired
    private TemplateEngine templateEngine;
    @Test
    public void  testMail(){
       String to = "2156168500@qq.com";
       String subject = "TEST";
       String text = "Wellcome there";
       mailConnect.sendMail(to,subject,text);
    }
    @Test
    public void  testHtml(){
        String to = "2156168500@qq.com";
        String subject = "TEST";
        Context context = new Context();
        context.setVariable("username","fjh");
        String text = templateEngine.process("/mail/dome", context);
        System.out.println(text);
        mailConnect.sendMail(to,subject,text);
    }
}
