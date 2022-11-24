package com.fjh.commity;

import com.fjh.commity.dao.LoginTicketMapper;
import com.fjh.commity.entity.LoginTicket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

@SpringBootTest(classes = CommityApplication.class)
@RunWith(SpringRunner.class)
public class TestLoginTicket {
    @Resource
    private LoginTicketMapper loginTicketMapper;
    @Test
    public void testInsert(){
        LoginTicket loginTicket = new LoginTicket() ;
        loginTicket.setUserId(101);
        loginTicket.setTicket("abcde");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 3600 * 12));
        loginTicketMapper.insertLoginTicket(loginTicket);

    }
    @Test
    public void testSelect(){
        LoginTicket loginTicket = loginTicketMapper.selectLoginTicket("abcde");
        System.out.println(loginTicket);
        loginTicketMapper.updateStatus("abcde",1);
       loginTicket =loginTicketMapper.selectLoginTicket("abcde");
        System.out.println(loginTicket);
    }
}
