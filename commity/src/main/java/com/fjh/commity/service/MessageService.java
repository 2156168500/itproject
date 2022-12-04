package com.fjh.commity.service;

import com.fjh.commity.dao.MessageMapper;
import com.fjh.commity.entity.Message;
import com.fjh.commity.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MessageService {
    @Resource
    private MessageMapper messageMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;
    public List<Message> findConversations(int userId, int offset, int limit) {
        return messageMapper.selectConversations(userId, offset, limit);
    }

    public int findConversationCount(int userId) {
        return messageMapper.selectConversationCount(userId);
    }

    public List<Message> findLetters(String conversationId, int offset, int limit) {
        return messageMapper.selectLetters(conversationId, offset, limit);
    }

    public int findLetterCount(String conversationId) {
        return messageMapper.selectLetterCount(conversationId);
    }

    public int findLetterUnreadCount(int userId, String conversationId) {
        return messageMapper.selectLetterUnreadCount(userId, conversationId);
    }

    //增加一跳私信
    public int addMessage(Message message){
        //首先对私信中的内容进行格式化处理和敏感词过滤
        String content = message.getContent();
        content = HtmlUtils.htmlEscape(content);
        content = sensitiveFilter.filter(content);
        message.setContent(content);
        //执行SQL
        return messageMapper.insertMessage(message);
    }

    //读私信
    public int readMessage(List<Integer> ids){
        return messageMapper.updateStatus(ids,1);
    }

}
