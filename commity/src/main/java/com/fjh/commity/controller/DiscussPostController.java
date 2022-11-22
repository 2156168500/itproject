package com.fjh.commity.controller;

import com.fjh.commity.entity.DiscussPost;
import com.fjh.commity.entity.Page;
import com.fjh.commity.entity.User;
import com.fjh.commity.service.DiscussPostService;
import com.fjh.commity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DiscussPostController {
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private UserService userService;
    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String getDiscussPost(Model model, Page page){
        // 方法调用钱,SpringMVC会自动实例化Model和Page,并将Page注入Model.
        // 所以,在thymeleaf中可以直接访问Page对象中的数据.
        page.setRows(discussPostService.selectCount(0));
        page.setPath("/index");
        List<DiscussPost> list = discussPostService.selectAllDiscussPost(0, page.getOffset(), page.getLimit());
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (list != null) {
            for (DiscussPost post : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
                User user = userService.findUserById(post.getUserId());
                map.put("user", user);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        return "/index";
    }
}
