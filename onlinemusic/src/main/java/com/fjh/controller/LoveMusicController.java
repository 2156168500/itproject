package com.fjh.controller;

import com.fjh.pojo.Music;
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
import java.util.List;

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
    @RequestMapping("/findlovemusic")
    public ResponseBodyMessage<List<Music>> findLoveMusic(@RequestParam(required = false) String musicName ,HttpServletRequest request){
        HttpSession session = request.getSession();
        if(session == null || session.getAttribute(Constant.USERINFO_SESSION_KEY) == null){
            return new ResponseBodyMessage<>(-1,"没有登录",null);
        }
        User user = (User) session.getAttribute(Constant.USERINFO_SESSION_KEY);
       List<Music> musicList =  loveMusicService.findLoveMusic(musicName,user.getId());
       return new ResponseBodyMessage<>(1,"查询成功",musicList);
    }
    @RequestMapping("/deletelovemusic")
    public ResponseBodyMessage<Boolean> deleteLoveMusic(@RequestParam String musicId,HttpServletRequest request){
        //1.判断用户是否登录
        int loveMusicId =  Integer.parseInt(musicId);
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute(Constant.USERINFO_SESSION_KEY) == null){
            return new ResponseBodyMessage<>(-1,"没有登录",false);
        }
        User user = (User)session.getAttribute(Constant.USERINFO_SESSION_KEY);
        boolean flag = loveMusicService.deleteLoveMusic(user.getId(),loveMusicId);
        if(flag){
            return new ResponseBodyMessage<>(1,"移除成功",true);
        }
        return new ResponseBodyMessage<>(-1,"移除失败",false);
    }
}
