package com.fjh.commity.controller;

import com.fjh.commity.entity.Message;
import com.fjh.commity.entity.Page;
import com.fjh.commity.entity.User;
import com.fjh.commity.service.MessageService;
import com.fjh.commity.service.UserService;
import com.fjh.commity.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    @GetMapping("/letter/detail/{conversationId}")
    public String getLetterDetail(@PathVariable("conversationId") String conversationId, Page page, Model model) {
        // 分页信息
        page.setLimit(5);
        page.setPath("/letter/detail/" + conversationId);
        page.setRows(messageService.findLetterCount(conversationId));

        // 私信列表
        List<Message> letterList = messageService.findLetters(conversationId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> letters = new ArrayList<>();
        if (letterList != null) {
            for (Message message : letterList) {
                Map<String, Object> map = new HashMap<>();
                map.put("letter", message);
                map.put("fromUser", userService.findUserById(message.getFromId().toString()));
                letters.add(map);
            }
        }
        model.addAttribute("letters", letters);

        // 私信目标
        model.addAttribute("target", getLetterTarget(conversationId));

        return "/site/letter-detail";
    }

    private User getLetterTarget(String conversationId) {
        String[] ids = conversationId.split("_");
        Integer id0 = Integer.parseInt(ids[0]);
        Integer id1 = Integer.parseInt(ids[1]);

        if (hostHolder.getUser().getId() == id0) {
            return userService.findUserById(id1.toString());
        } else {
            return userService.findUserById(id0.toString());
        }
    }
}
