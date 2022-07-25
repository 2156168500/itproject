package com.fjh.controller;

import com.fjh.pojo.Music;
import com.fjh.pojo.User;
import com.fjh.service.MusicService;
import com.fjh.totals.Constant;
import com.fjh.totals.ResponseBodyMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/music")
public class MusicController {
    @Autowired
    private MusicService musicService;
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
        //找到.MP3的.的位置
        int indexPoint = originalFilename.lastIndexOf(".");
        String title = originalFilename.substring(0,indexPoint);
        Music music =  musicService.selectOne(title,singer);
      if(music != null){
          return new ResponseBodyMessage<>(-1,"上传失败,音乐已经存在",false);
      }
        String path = save_path + originalFilename;
        System.out.println("path :" + path);
        File dest = new File(path);
        //如果文件不存在,就创建文件
        if(! dest.exists()){
            dest.mkdirs();
        }
        try {
            file.transferTo(dest);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseBodyMessage<>(-1,"服务器上传失败",false);
        }
        //上传音乐到数据库
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
        String time =  simpleDateFormat.format(new Date());
        //播放该音乐要请求的路径
        String url = "music/get?path=" + title;
        User user = (User) session.getAttribute(Constant.USERINFO_SESSION_KEY);
        int userId = user.getId();
        Music insertMusic = new Music(null,title,singer,time,url,userId);
        int result =  musicService.insert(insertMusic);
        if(result == 1){
            //插入数据库成功
            return  new ResponseBodyMessage<>(1,"数据库上传成功",true);
        }else{
            dest.delete();
            return  new ResponseBodyMessage<>(-1,"数据库上传失败",false);
        }

    }

}
