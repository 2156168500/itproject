package com.fjh.commity.controller;

import com.fjh.commity.entity.Message;
import com.fjh.commity.entity.Page;
import com.fjh.commity.service.MessageService;
import com.fjh.commity.service.UserService;
import com.fjh.commity.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;
    @GetMapping("/letter/list")
    public String getLetterList(Model model, Page page){
        //设置分页信息
        page.setLimit(5);
        page.setRows(messageService.findConversationCount(hostHolder.getUser().getId()));
        page.setPath("/letter/list");

        //查询所有的会话信息
        List<Message> conversationList = messageService.findConversations(hostHolder.getUser().getId(),
                page.getOffset(),page.getLimit());
        //将所有的会话视图需要的信息存储
        List<Map<String,Object>> conversations = new ArrayList<>();
        //遍历所有的会话信息，找到对应的视图用到的数据
        for(Message conversation : conversationList){
            Map<String,Object> map = new HashMap<>();
            //首先存储会话的内容
            map.put("conversation",conversation);
            //存储当前会话共有多少个记录
            map.put("letterCount", messageService.findLetterCount(conversation.getConversationId()));
            //存储当前会话有多少个未读私信
            map.put("unreadCount",messageService.findLetterUnreadCount(hostHolder.getUser().getId(),conversation.getConversationId()));
            //存储目标用户的信息
            Integer targetId = hostHolder.getUser().getId().equals(conversation.getFromId()) ? conversation.getToId() : conversation.getFromId();
            map.put("target",userService.findUserById(targetId.toString()));
            conversations.add(map);
        }
        model.addAttribute("conversations",conversations);
        //查询所有的未读消息
        int letterUnreadCount = messageService.findLetterUnreadCount(hostHolder.getUser().getId(),null);
        model.addAttribute("letterUnreadCount",letterUnreadCount);
        return "/site/letter";
    }
}
