package com.fjh.service;

import com.fjh.dao.BlogDao;
import com.fjh.pojo.Blog;

import java.util.List;

public class BlogService {
    private BlogDao blogDao = new BlogDao() ;
    public void insert(Blog blog){
        blogDao.insert(blog);
    }
    public List<Blog> selectAll(){
        return blogDao.selectAll();
    }
    public Blog selectOne(int blogId){
        return blogDao.selectOne(blogId);
    }
}
