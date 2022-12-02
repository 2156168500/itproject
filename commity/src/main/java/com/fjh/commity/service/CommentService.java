package com.fjh.commity.service;

import com.fjh.commity.dao.CommentMapper;
import com.fjh.commity.entity.Comment;
import com.fjh.commity.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CommentService {
    @Resource
    private CommentMapper commentMapper;
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<Comment> selectCommentsByEntity(int entityType,int entityId,int offset,int limit){
        return commentMapper.selectCommentsByEntity(entityType,entityId,offset,limit);
    }

    public int selectCount(int entityType, int entityId){
        return commentMapper.selectCountByEntity(entityType,entityId);
    }

    /**
     *因为这一个业务中包含两次对数据层的操作，
     * 为了保证业务的完整性
     * 使用事务的管理
     * 如果两次对数据层的访问都是成功的就执行成功
     * 如果有一个是失败的，就回滚数据
     */
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public int insertComment(Comment comment){
        //对comment中的内容进行html的格式处理和敏感词的过滤
        String content = comment.getContent();
        content = HtmlUtils.htmlEscape(content);
        content = sensitiveFilter.filter(content);
        comment.setContent(content);
        //第一步插入评论到数据库中
        int rows = commentMapper.insertComment(comment);
        //第二步在discussPost中修改评论的数量
        if(comment.getEntityType() == 1){//证明是一个帖子的评论
            int count = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId());
            discussPostService.updateCommentCount(comment.getEntityId(),count + 1);
        }
        return rows;
    }
}
