package com.fjh.commity.controller;

import com.fjh.commity.entity.User;
import com.fjh.commity.service.LikeService;
import com.fjh.commity.util.CommunityUtil;
import com.fjh.commity.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController {
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private LikeService likeService;
    @PostMapping("/like")
    @ResponseBody
    public String like(int entityType,int entityId,int likeUserId){
        //1.得到当前用户
        User user = hostHolder.getUser();
        if(user == null){
            return CommunityUtil.getJsonString(-1,"没有登录");
        }
        //当前用户点赞
        likeService.like(user.getId(),entityType,entityId,likeUserId);
        //获取当前的点赞状态
        int likeStatus = likeService.findLikeStatus(user.getId(),entityType,entityId);
        long likeCount = likeService.findLikeCount(entityType,entityId);
        Map<String,Object> hashMap = new HashMap<>();
        hashMap.put("likeStatus",likeStatus);
        hashMap.put("likeCount",likeCount);
        return CommunityUtil.getJsonString(0,"点赞成功",hashMap);
    }
}
