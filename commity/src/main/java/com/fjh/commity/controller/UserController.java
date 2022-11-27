package com.fjh.commity.controller;

import com.fjh.commity.annotation.LoginRequired;
import com.fjh.commity.entity.User;
import com.fjh.commity.service.UserService;
import com.fjh.commity.util.CommunityUtil;
import com.fjh.commity.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
@RequestMapping("/user")
public class UserController  {
    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;
    @Value("${community.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Value("${community.path.upload}")
    private String uploadPath;
    @LoginRequired
    @GetMapping("/update")
    public String toUpdate(){
        return "/site/setting";
    }
    @LoginRequired
    @PostMapping("/update")
    public String update(Model model, String oldPassword, String newPassword){
        if (StringUtils.isBlank(oldPassword)){
            model.addAttribute("msg","密码不能为空" );
            return "/site/setting";
        }
        if(StringUtils.isBlank(newPassword)) {
            model.addAttribute("msg", "新密码不能为空");
            return "/site/setting";
        }
        User user = hostHolder.getUser();
        oldPassword = CommunityUtil.md5(oldPassword + user.getSalt());
        String salt = CommunityUtil.getUUID().substring(0,5);
        newPassword = CommunityUtil.md5(newPassword + salt );
        if(!user.getPassword().equals(oldPassword)){
            model.addAttribute("meg","原密码不正确");
            return "/site/setting";
        }
        userService.updatePassword(user.getId(),newPassword);
        userService.updateSalt(user.getId(),salt);
        return "redirect:/logout";
    }

    @PostMapping("/upload")
    @LoginRequired
    public String upload(MultipartFile headerImage,Model model){
        if(headerImage == null){
            model.addAttribute("msg","文件不能为空");
            return "/site/setting";
        }
        //首先获取文件的文件名
        String fileName = headerImage.getOriginalFilename();
        if(fileName == null){
            model.addAttribute("msg","文件格式错误");
            return "/site/setting";
        }
        //获取后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if(StringUtils.isBlank(suffix)){
            model.addAttribute("msg","文件格式错误");
            return "/site/setting";
        }
        //生成随机的文件名
        fileName = CommunityUtil.getUUID() + suffix;
        //生成文件存放的位置
        String path = uploadPath + "/" + fileName;
        //生成文件
        File dest = new File(path);
        //存放文件
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //生成web访问路径
        //http://localhost:8080/community/user/header/xxx.xxx;
        String webPath = domain + contextPath + "/user/header/" + fileName ;
        userService.uploadHeader(hostHolder.getUser().getId(),webPath);
        return "redirect:/home";
    }
    @LoginRequired
    @GetMapping("/header/{fileName}")
    //http://localhost:8080/community/user/header/xxx.xxx;
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response){
        //获取服务器上头像的存储位置
        fileName = uploadPath + "/" + fileName;
        //获取后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        FileInputStream fis = null;
        response.setContentType("image/" + suffix);
        try {
            ServletOutputStream os = response.getOutputStream();
            fis = new FileInputStream(fileName);
            byte[]buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1){
                os.write(buffer,0,b);
            }
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
