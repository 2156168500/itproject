package com.fjh.commity.controller;

import com.fjh.commity.entity.Event;
import com.fjh.commity.entity.User;
import com.fjh.commity.event.EventProducer;
import com.fjh.commity.service.FollowService;
import com.fjh.commity.util.CommunityConst;
import com.fjh.commity.util.CommunityUtil;
import com.fjh.commity.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FollowController implements CommunityConst {

    @Autowired
    private FollowService followService;
    @Autowired
    private EventProducer eventProducer;
    @Autowired
    private HostHolder hostHolder;
    @PostMapping("/follow")
    @ResponseBody
    public String follow(int entityType,int entityId ){
        User user = hostHolder.getUser();//获取当前登录的用户
        if(user != null){
            followService.follow(user.getId(),entityType,entityId);
        }else{
            return CommunityUtil.getJsonString(-1,"没有登录，无法关注");
        }
        Event event = new Event()
                .setUserId(user.getId())
                .setEntityType(entityType)
                .setEntityId(entityId)
                .setTopic(TOPIC_FOLLOW)
                .setEntityUserId(entityId);
        eventProducer.producer(event);
        return CommunityUtil.getJsonString(0,"关注成功");


    }

    @PostMapping("/unFollow")
    @ResponseBody
    public String unFollow(int entityType, int entityId){
        User user = hostHolder.getUser();//获取当前登录的用户
        if(user != null){
            followService.unFollow(user.getId(),entityType,entityId);
        }else{
            return CommunityUtil.getJsonString(-1,"没有登录，无法取关");
        }
        return CommunityUtil.getJsonString(0,"取关成功");
    }
}
