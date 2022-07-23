package com.fjh.controller;

import com.fjh.totals.Constant;
import com.fjh.totals.ResponseBodyMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/music")
public class MusicController {
    @Value("${SAVE_PATH}")
    private String save_path ;
    @RequestMapping("/upload")
    public ResponseBodyMessage<Boolean> upload(@RequestParam String singer,
                                               @RequestParam("filename")MultipartFile file,
                                               HttpServletRequest request
                                               )  {
        //检查是否登录
        HttpSession session = request.getSession();
        if(session == null || session.getAttribute(Constant.USERINFO_SESSION_KEY) == null){
            System.out.println("没有登录");
            return new ResponseBodyMessage<>(-1,"没有登录" ,false);
        }
        //得到文件的文件名和类型
        String originalFilename = file.getOriginalFilename();
        String path = save_path + originalFilename;
        System.out.println("path :" + path);
        File dest = new File(path);
        //如果文件不存在,就创建文件
        if(! dest.exists()){
            dest.mkdirs();
        }
        try {
            file.transferTo(dest);
            return new ResponseBodyMessage<>(1,"上传成功",true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseBodyMessage<>(-1,"上传失败",false);

    }

}
