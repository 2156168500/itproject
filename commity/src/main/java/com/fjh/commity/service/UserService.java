package com.fjh.commity.service;

import com.fjh.commity.dao.LoginTicketMapper;
import com.fjh.commity.dao.UserMapper;
import com.fjh.commity.entity.LoginTicket;
import com.fjh.commity.entity.User;
import com.fjh.commity.util.CommunityConst;
import com.fjh.commity.util.CommunityUtil;
import com.fjh.commity.util.CookieUtil;
import com.fjh.commity.util.MailConnect;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService implements CommunityConst {
    @Resource
    private UserMapper userMapper;
    @Resource
    private LoginTicketMapper loginTicketMapper;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private MailConnect mailConnect;
    @Value("${community.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    private CookieUtil cookieUtil = new CookieUtil();
    public User findUserById(String userId) {
        return userMapper.selectById(Long.parseLong(userId));
    }

    public Map<String,Object> register(User user){
        Map<String,Object> map = new HashMap<>();
        if(StringUtils.isBlank(user.getUsername())){
            map.put("usernameMsg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg","密码不能为空");
            return map;
        }
        if(StringUtils.isBlank(user.getEmail())){
            map.put("emailMsg","邮箱不能为空");
            return map;
        }

        //输入的信息都不问空之后，判断是否是重复的用户
        User u = userMapper.selectByUsername(user.getUsername());
        if(u!= null){
            map.put("usernameMsg","用户已经注册过了");
            return map;
        }
        u = userMapper.selectByEmail(user.getEmail());
        if(u != null){
            map.put("emailMsg","邮箱已经被注册过了");
            return map;
        }
        //代码来到这说明，可以进行注册了，
        //注册之前，要对密码进行加密处理
        user.setSalt(CommunityUtil.getUUID().substring(0,5));
        user.setStatus(0);
        user.setType(0);
        user.setActivationCode(CommunityUtil.getUUID());
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);
        //发送激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        // http://localhost:8080/community/activation/101/code
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailConnect.sendMail(user.getEmail(), "激活账号", content);
        return map;
    }

    /**
     *用于对用户的注册的激活操作
     */
    public int activation(int userId,String code){
        User user = userMapper.selectById(userId);
        if(user == null){
            return CommunityConst.ACTIVATION_FILE;
        }
        if(user.getStatus() == 1){
            return CommunityConst.ACTIVATION_REPEAT;
        }
        userMapper.updateStatus(userId,1);
        return CommunityConst.ACTIVATION_SUCCESS;
    }

    /**
     * 用于对于登录逻辑的处理
     */
    public Map<String,Object> login(String username,String password,int expiredSeconds){
        Map<String,Object> map = new HashMap<>();
        if(StringUtils.isBlank(username)){
            map.put("usernameMsg","用户名不能为空");
            return  map;
        }
        if(StringUtils.isBlank(password)){
            map.put("passwordMsg","密码不能为空");
            return map;
        }
        //代码来到这里说明，用户名和密码都是有效的
        User user = userMapper.selectByUsername(username);
        if(user == null){
            map.put("usernameMsg","用户不存在");
            return map;
        }
        if(user.getStatus() == 0){//说明账号还没有被激活
            map.put("usernameMsg","用户没有被激活");
            return map;
        }
        //比较密码
        password = CommunityUtil.md5(password + user.getSalt());
        if(!password.equals(user.getPassword())){
            map.put("passwordMsg","密码错误");
            return map;
        }
        //代码来到这里说明，登录是成功的。设置ticket
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setStatus(0);
        loginTicket.setTicket(CommunityUtil.getUUID());
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * expiredSeconds));
        loginTicketMapper.insertLoginTicket(loginTicket);
        //将登录凭证存储在数据库中之后，要把这个凭证存在map中，给Controller层去判断
        map.put("ticket",loginTicket.getTicket());
        return map;
    }

    public void logout(String taicket){
        loginTicketMapper.updateStatus(taicket,1);
    }

    public User getUser(HttpServletRequest request){
        String ticket = CookieUtil.getValue(request, "ticket");
        if(ticket != null){
            LoginTicket loginTicket = loginTicketMapper.selectLoginTicket(ticket);
            if(loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())){
                return userMapper.selectById(loginTicket.getUserId());
            }
        }
        return  null;
    }

    /**
     *修改密码
     */
    public int  updatePassword(int userId,String newPassword){
        return userMapper.updatePassword(userId,newPassword);
    }

    public int updateSalt(int userId,String salt){
        return userMapper.updateSalt(userId,salt);
    }

    /**
     * 上传头像
     */
    public int uploadHeader(int id,String headerPath){
       return userMapper.updateHeader(id,headerPath);
    }

}
