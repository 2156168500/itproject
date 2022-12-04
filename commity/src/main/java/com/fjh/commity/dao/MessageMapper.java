package com.fjh.commity.dao;

import com.fjh.commity.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {
    //查询当前用户所有的会话，只显示每个会话最近收到的私信
     List<Message> selectConversations(int userId,int offset,int limit);
    //查询当前用户所有的会话的个数
     int selectConversationCount(int userId);
     //查询每个会话中所有私信
    List<Message> selectLetters(String conversationId,int offset,int limit);
    //查询每个会话中的所有的私信个数
    int selectLetterCount(String conversationId);
    //查询用户对应的未读私信的数量，这个方法要实现为可变SQL，因为需要查询用户所有的未读私信
    //和用户的每个会话的未读消息
    int selectLetterUnreadCount(int userId, String conversationId);
    //添加一条私信
    int insertMessage(Message message);
    //修改私信的住状态
    int updateStatus(List<Integer> ids,int status);
}
