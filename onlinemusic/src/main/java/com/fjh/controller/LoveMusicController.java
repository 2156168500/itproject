package com.fjh.controller;

import com.fjh.pojo.User;
import com.fjh.service.LoveMusicService;
import com.fjh.totals.Constant;
import com.fjh.totals.ResponseBodyMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/lovemusic")
public class LoveMusicController {
    @Autowired
    private LoveMusicService loveMusicService;
    @RequestMapping("/likeMusic")
    public ResponseBodyMessage<Boolean> likeMusic( @RequestParam String id , HttpServletRequest request){
        //1.判断用户是否登录
        int loveMusicId =  Integer.parseInt(id);
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute(Constant.USERINFO_SESSION_KEY) == null){
            return new ResponseBodyMessage<>(-1,"没有登录",false);
        }
        User user = (User)session.getAttribute(Constant.USERINFO_SESSION_KEY);
        boolean flag = loveMusicService.insertLoveMusic(user.getId(),loveMusicId);
        if(flag){
            return new ResponseBodyMessage<>(1,"收藏成功",true);
        }else{
            return new ResponseBodyMessage<>(-1,"收藏失败您可能已经收藏过了",false);
        }
    }
}
