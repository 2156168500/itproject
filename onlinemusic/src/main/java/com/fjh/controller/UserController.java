package com.fjh.controller;

import com.fjh.pojo.User;
import com.fjh.service.UserService;
import com.fjh.totals.Constant;
import com.fjh.totals.ResponseBodyMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 登录功能
     * @param username 用户名
     * @param password 密码
     * @param request 请求对象
     */
    @RequestMapping("/login")
    public ResponseBodyMessage<User> login(@RequestParam String username, @RequestParam String password
                                           , HttpServletRequest request
                                           ){
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(password);
//        User login = userService.login(user);
        User login = userService.selectByName(username);
        if(login == null){
            System.out.println("登录失败");
            return new ResponseBodyMessage<>(-1,"用户名或密码错误",null);
        }else {
           boolean flag = bCryptPasswordEncoder.matches(password,login.getPassword());
           if(!flag){
               System.out.println("登录失败");
               return new ResponseBodyMessage<>(-1,"用户名或密码错误",null);
           }
            request.getSession().setAttribute(Constant.USERINFO_SESSION_KEY,login);
            return new ResponseBodyMessage<>(1,"登录成功",login);
        }
    }

    /**
     * 注册功能
     * @param username 用户名
     * @param password 密码

     */
    @RequestMapping("/register")
    public ResponseBodyMessage<User> register(@RequestParam String username,@RequestParam String password){
        User user = userService.selectByName(username);
        if(user != null){
            System.out.println("用户已经存在");
            return new ResponseBodyMessage<>(-1,"用户已经存在",null);
        }else{
            //对用户输入的密码进行加密
           String newPassWord =  bCryptPasswordEncoder.encode(password);
           User insetUser = new User();
           insetUser.setPassword(newPassWord);
           insetUser.setUsername(username);
           //插入数据到数据库中
           userService.insert(insetUser);

           return new ResponseBodyMessage<>(1,"注册成功",insetUser);
        }

    }



}
