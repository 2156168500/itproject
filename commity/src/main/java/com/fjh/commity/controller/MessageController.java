package com.fjh.commity.controller;

import com.fjh.commity.entity.Message;
import com.fjh.commity.entity.Page;
import com.fjh.commity.entity.User;
import com.fjh.commity.service.MessageService;
import com.fjh.commity.service.UserService;
import com.fjh.commity.util.CommunityUtil;
import com.fjh.commity.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

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
        //如果有未读的消息，改为已读
        List<Integer> ids = getIds(letterList);
        if(!ids.isEmpty()){
            messageService.readMessage(ids);
        }
        return "/site/letter-detail";
    }

    private List<Integer> getIds(List<Message>messages){
        List<Integer> ids = new ArrayList<>();
        if (messages != null){
            for(Message message : messages){
                if(message.getToId().equals(hostHolder.getUser().getId()) && message.getStatus() == 0){
                    ids.add(message.getId());
                }
            }
        }
        return ids;

    }

    private User getLetterTarget(String conversationId) {
        String[] ids = conversationId.split("_");
        Integer id0 = Integer.parseInt(ids[0]);
        Integer id1 = Integer.parseInt(ids[1]);

        if (hostHolder.getUser().getId().equals(id0)) {
            return userService.findUserById(id1.toString());
        } else {
            return userService.findUserById(id0.toString());
        }
    }
    @PostMapping("/letter/save")
    @ResponseBody
    public String addLetter(String toName,String content){
        //首先去数据库中利用用户名，查询用户
        User target = userService.findUserByUsername(toName);
        if(target == null){
            return CommunityUtil.getJsonString(1,"用户不存在");
        }
        //设置私信的属性
        Message message = new Message();
        message.setFromId(hostHolder.getUser().getId());
        message.setToId(target.getId());
        message.setContent(content);
        String conversationId = "";
       conversationId = message.getFromId() < message.getToId()
               ? message.getFromId() + "_" + message.getToId()
               : message.getToId() + "_" + message.getFromId();
       message.setConversationId(conversationId);
       message.setStatus(0);
       message.setCreateTime(new Date());
       messageService.addMessage(message);
        return CommunityUtil.getJsonString(1);
    }
}
