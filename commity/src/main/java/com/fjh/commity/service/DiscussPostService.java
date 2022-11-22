package com.fjh.commity.service;

import com.fjh.commity.dao.DiscussPostMapper;
import com.fjh.commity.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class DiscussPostService {
    @Resource
    private DiscussPostMapper discussPostMapper;
    public List<DiscussPost> selectAllDiscussPost(int userId,int offset,int limit){
        return discussPostMapper.selectAllDiscussPost(userId,offset,limit);
    }
    public int selectCount(int userId){
        return discussPostMapper.selectCount(userId);
    }
}
