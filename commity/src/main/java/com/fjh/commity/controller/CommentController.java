package com.fjh.commity.controller;

import com.fjh.commity.entity.Comment;
import com.fjh.commity.entity.User;
import com.fjh.commity.service.CommentService;
import com.fjh.commity.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private HostHolder hostHolder;
    @PostMapping("/add/{discussPostId}")
    public String addComment(@PathVariable("discussPostId")int discussPostId, Comment comment){
        //对评论中的内容进行处理
        if(comment.getStatus() == null){
            comment.setStatus(0);
        }
        if(comment.getTargetId() == null){
            comment.setTargetId(0);
        }
        comment.setCreateTime(new Date());
        User user = hostHolder.getUser();
        if(user == null){
            return "/login";
        }
        comment.setUserId(user.getId());
        //添加评论
        commentService.insertComment(comment);

        return "redirect:/discuss/detail/" + discussPostId;
    }
}
