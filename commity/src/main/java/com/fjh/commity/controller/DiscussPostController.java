package com.fjh.commity.controller;

import com.fjh.commity.entity.DiscussPost;
import com.fjh.commity.entity.User;
import com.fjh.commity.service.DiscussPostService;
import com.fjh.commity.service.UserService;
import com.fjh.commity.util.CommunityUtil;
import com.fjh.commity.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@Controller
@RequestMapping("/discuss")
public class DiscussPostController {
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;
    private static final Logger logger = LoggerFactory.getLogger(DiscussPostController.class);
    @PostMapping("/add")
    @ResponseBody
    public String postDiscussPost(String title,String content){
        if(StringUtils.isBlank(title)){
            return CommunityUtil.getJsonString(-1,"标题不能为空");
        }
        if (StringUtils.isBlank(content)) {
            return CommunityUtil.getJsonString(-1,"内容不能为空");
        }
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJsonString(-1,"您还没有登录");
        }
        DiscussPost discussPost = new DiscussPost();
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setUserId(user.getId().toString());
        discussPost.setCreateTime(new Date());
        discussPost.setType(0);
        discussPost.setStatus(0);
        discussPost.setCommentCount(0);
        discussPostService.addDiscussPost(discussPost);
        return CommunityUtil.getJsonString(0,"发布成功");
    }
    @GetMapping("/detail/{discussPostId}")
    public String getDiscussPost(@PathVariable("discussPostId") int id, Model model){
        //首先查询帖子
        DiscussPost discussPost = discussPostService.selectDiscussPostById(id);
        model.addAttribute("post",discussPost);
        //根据userId查询用户的信息
        User user = userService.findUserById(discussPost.getUserId());
        model.addAttribute("user",user);
        return "/site/discuss-detail";
    }
}
