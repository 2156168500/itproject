package com.fjh.commity.controller;

import com.fjh.commity.entity.Comment;
import com.fjh.commity.entity.DiscussPost;
import com.fjh.commity.entity.Event;
import com.fjh.commity.entity.User;
import com.fjh.commity.event.EventProducer;
import com.fjh.commity.service.CommentService;
import com.fjh.commity.service.DiscussPostService;
import com.fjh.commity.util.CommunityConst;
import com.fjh.commity.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityConst {
    @Autowired
    private CommentService commentService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private EventProducer eventProducer;
    @Autowired
    private DiscussPostService discussPostService;
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

        //进行系统消息的发送
        Event event = new Event()
                .setTopic(TOPIC_COMMENT)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(comment.getEntityType())
                .setEntityId(comment.getEntityId())
                .setData("postId",discussPostId);
        if(comment.getEntityType() == ENTITY_TYPE_COMMENT){
            DiscussPost discussPost = discussPostService.selectDiscussPostById(event.getEntityId());
            event.setEntityUserId(discussPost.getUserId());
        }else if(comment.getEntityType() == ENTITY_TYPE_REPLAY){
            Comment target = commentService.selectCommentById(event.getEntityId());
            event.setEntityUserId(target.getUserId());
        }
        eventProducer.producer(event);
        return "redirect:/discuss/detail/" + discussPostId;
    }
}
