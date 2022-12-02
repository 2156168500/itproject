package com.fjh.commity.service;

import com.fjh.commity.dao.CommentMapper;
import com.fjh.commity.entity.Comment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CommentService {
    @Resource
    private CommentMapper commentMapper;
    public List<Comment> selectCommentsByEntity(int entityType,int entityId,int offset,int limit){
        return commentMapper.selectCommentsByEntity(entityType,entityId,offset,limit);
    }

    public int selectCount(int entityType, int entityId){
        return commentMapper.selectCountByEntity(entityType,entityId);
    }
}
