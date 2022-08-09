package com.fjh.controller;

import com.fjh.pojo.User;
import com.fjh.service.UserService;
import com.fjh.util.ResponseBodyMessage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @RequestMapping("/login")
    public ResponseBodyMessage<User> login(@RequestParam String username, @RequestParam String password, HttpServletRequest request){
        User login = userService.selectByUsername(username);
        if(login == null){
            return  new ResponseBodyMessage<User>(-1,"用户不存在",login);
        }
        boolean flag = bCryptPasswordEncoder.matches(password,login.getPassword());
        if(flag){//登录成功
            HttpSession session = request.getSession(true);
            session.setAttribute("userInfo",login);
            login.setPassword(null);
            return new ResponseBodyMessage<>(1,"登录成功",login);
        }
        return new ResponseBodyMessage<>(-1,"用户名或密码错误",login);
    }
    @RequestMapping("/register")
    public ResponseBodyMessage<User> register(@RequestParam String username, @RequestParam String password){
        User newUser = new User();
        try {
           String encryptionPassword = bCryptPasswordEncoder.encode(password);
            newUser.setUsername(username);
            newUser.setPassword(encryptionPassword);
            userService.insert(newUser);
        }catch (DuplicateKeyException e){
            return new ResponseBodyMessage<>(-1,"用户已经存在",null);
        }

        return new ResponseBodyMessage<>(1,"注册成功",newUser);
    }

    @RequestMapping("/userInfo")
    public ResponseBodyMessage<User> get(HttpServletRequest request){
        HttpSession session = request.getSession();
        if(session == null){
            return new ResponseBodyMessage<User>(-1,"没有登录",null);
        }
        User userInfo = (User)session.getAttribute("userInfo");
        if(userInfo == null){
            return new ResponseBodyMessage<User>(-1,"没有登录",null);
        }
        return  new ResponseBodyMessage<>(1,"获取成功",userInfo);
    }

}
