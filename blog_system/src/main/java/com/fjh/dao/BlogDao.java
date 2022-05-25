package com.fjh.dao;

import com.fjh.base.DBUtil;
import com.fjh.pojo.Blog;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class BlogDao {
    /**
     * 1.插入一篇文章
     */
    public void insert(Blog blog){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            //1.与数据库建立链接
            connection = DBUtil.getConnection();
         //2.构造SQL
            String sql = "insert into blog values (null,?,?,?,now())";
             preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,blog.getTitle());
            preparedStatement.setString(2,blog.getContent());
            preparedStatement.setInt(3,blog.getUserId());
            //执行SQL
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(connection,preparedStatement,null);
        }
    }
    // 2. 能够获取到博客表中的所有博客的信息 (用于在博客列表页, 此处每篇博客不一定会获取到完整的正文)
    public List<Blog> selectAll(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Blog> blogs = new ArrayList<>();
        try {
            connection = DBUtil.getConnection();
            String sql = "select* from blog";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int blogId = resultSet.getInt("blogid");
                String title = resultSet.getString("title");
                String content = resultSet.getString("content");
                int userId = resultSet.getInt("userid");
                Timestamp post_time = resultSet.getTimestamp("post_time");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                simpleDateFormat.format(post_time);
                blogs.add(new Blog(blogId,title,content,userId,post_time));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(connection,preparedStatement,resultSet);
        }
        return blogs;
    }

    public Blog selectOne(int blogId){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Blog blog = null;
        try {
            connection = DBUtil.getConnection();
            String sql = "select* from blog where blogid = ? ";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,blogId);
            resultSet = preparedStatement.executeQuery();
             if (resultSet.next()){
                String title = resultSet.getString("title");
                String content = resultSet.getString("content");
                int userId = resultSet.getInt("userid");
                Timestamp post_time = resultSet.getTimestamp("post_time");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                simpleDateFormat.format(post_time);
                blog =  new Blog(blogId,title,content,userId,post_time);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(connection,preparedStatement,resultSet);
        }
        return blog;
    }


}
