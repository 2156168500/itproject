package com.fjh.commity.controller;

import com.fjh.commity.entity.DiscussPost;
import com.fjh.commity.entity.Page;
import com.fjh.commity.entity.User;
import com.fjh.commity.service.DiscussPostService;
import com.fjh.commity.service.UserService;
import com.fjh.commity.util.CommunityConst;
import com.google.code.kaptcha.Producer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController implements CommunityConst {
    @Autowired
    private UserService userService;
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private Producer kaptchaProducer;
    private final  static Logger logger = LoggerFactory.getLogger(LoginController.class);
    @RequestMapping(path = "/home", method = RequestMethod.GET)
    public String getDiscussPost(Model model, Page page){
        // 方法调用钱,SpringMVC会自动实例化Model和Page,并将Page注入Model.
        // 所以,在thymeleaf中可以直接访问Page对象中的数据.
        page.setRows(discussPostService.selectCount(0));
        page.setPath("/home");
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
    @GetMapping("/register")
    public String toRegister(){
        System.out.println("go to register");
        return "/site/register";
    }
    @GetMapping("/login")
    public String toLogin(){
        return "/site/login";
    }
    @PostMapping("/register")
    public String Register(Model model, User user){
        Map<String, Object> map = userService.register(user);
        if(map != null && !map.isEmpty()){
            //获取map中的数据,返回给register.html
           model.addAttribute("usernameMsg",map.get("usernameMsg"));
           model.addAttribute("passwordMsg",map.get("passwordMsg"));
           model.addAttribute("emailMsg",map.get("emailMsg"));
           return "/site/register";
        }
        model.addAttribute("msg","您已经注册成功，我们给您发送了一份激活邮件，请您激活登录");
        model.addAttribute("target","/home");
        return "/site/operate-result";
    }

    //http://localhost:8080/community/activation/101/code
    @GetMapping("/activation/{userId}/{code}")
    public String activation(Model model, @PathVariable("userId") int userId,
                             @PathVariable("code") String code){
        logger.info("userId = " + userId);
        logger.info("code = " + code);
        int result = userService.activation(userId, code);
        if(result == CommunityConst.ACTIVATION_SUCCESS){
            model.addAttribute("msg","激活成功，您可以正常使用");
            model.addAttribute("target","/login");
        }else if(result == CommunityConst.ACTIVATION_REPEAT){
            model.addAttribute("msg","无效激活，您的账户已经被激活过了");
            model.addAttribute("target","/home");
        }else {
            model.addAttribute("msg","激活失败，您提供的激活码错误");
            model.addAttribute("target","/home");
        }

        return "/site/operate-result";
    }
    @GetMapping("/kaptcha")
    public void getKaptchaProducer(HttpServletResponse response, HttpSession session)  {
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);
        session.setAttribute("kaptcha",text);
        response.setContentType("image/png");
        response.setCharacterEncoding("utf-8");
        try {
            ServletOutputStream os = response.getOutputStream();
            ImageIO.write(image,"png",os);
        } catch (IOException e) {
            logger.error("验证码响应失败");
        }
    }
    @PostMapping("/login")
    public String login(String username,String password,String code,boolean rememberMe,
                        Model model,HttpServletResponse response,HttpSession session
                        ){
        if(StringUtils.isBlank(code)){
            model.addAttribute("codeMsg","验证码不能为空");
            return "/site/login";
        }
        //对验证码进行判断
        String kaptcha = (String) session.getAttribute("kaptcha");
        if(!code.equalsIgnoreCase(kaptcha)){
            model.addAttribute("codeMsg","验证码错误");
            return "/site/login";
        }
        int expiredSeconds = rememberMe ? REMEMBER_EXPIRED_SECONDS :DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        if(map.containsKey("ticket")){//说明是登录成功的
            //登录成功之后,要把登录凭证发给浏览器
            Cookie cookie = new Cookie("ticket",(String) map.get("ticket"));
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/home";
        }else {
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            return "/site/login";
        }
    }
    @GetMapping("/logout")
    public String logout(@CookieValue String ticket){
        userService.logout(ticket);
        return "redirect:/login";
    }


}
