package com.fjh.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fjh.pojo.Blog;
import com.fjh.service.BlogService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/blog")
public class BlogController extends HttpServlet {
    private BlogService blogService = new BlogService();
    ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Blog> blogList = blogService.selectAll();
        String json = objectMapper.writeValueAsString(blogList);
        resp.setCharacterEncoding("utf8");
        resp.setContentType("application/json;charset=utf8");
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
