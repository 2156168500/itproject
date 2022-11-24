package com.fjh.commity.dao;

import com.fjh.commity.entity.LoginTicket;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginTicketMapper {
    int insertLoginTicket(LoginTicket loginTicket);
    LoginTicket selectLoginTicket(String ticket);
    int updateStatus(String ticket,int status);

}
