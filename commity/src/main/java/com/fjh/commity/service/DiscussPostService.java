package com.fjh.commity.service;

import com.fjh.commity.dao.DiscussPostMapper;
import com.fjh.commity.entity.DiscussPost;
import com.fjh.commity.util.HostHolder;
import com.fjh.commity.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.List;
@Service
public class DiscussPostService {
    @Autowired
    private SensitiveFilter sensitiveFilter;
    @Resource
    private DiscussPostMapper discussPostMapper;
    public List<DiscussPost> selectAllDiscussPost(int userId,int offset,int limit){
        return discussPostMapper.selectAllDiscussPost(userId,offset,limit);
    }
    public int selectCount(int userId){
        return discussPostMapper.selectCount(userId);
    }

    public int addDiscussPost(DiscussPost discussPost){
        String title = discussPost.getTitle();
        String content = discussPost.getContent();
        //转义文字中的html标签
        title = HtmlUtils.htmlEscape(title);
        content = HtmlUtils.htmlEscape(content);
        //对文字中的敏感词汇进行过滤
        title = sensitiveFilter.filter(title);
        content = sensitiveFilter.filter(content);
        discussPost.setTitle(title);
        discussPost.setContent(content);
        return discussPostMapper.addDiscussPost(discussPost);
    }

    public DiscussPost selectDiscussPostById(int id){
        return discussPostMapper.selectDiscussPostById(id);
    }
}
