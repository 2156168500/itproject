package com.fjh.controller;

import com.fjh.pojo.Music;
import com.fjh.pojo.User;
import com.fjh.service.LoveMusicService;
import com.fjh.service.MusicService;
import com.fjh.totals.Constant;
import com.fjh.totals.ResponseBodyMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/music")
public class MusicController {
    @Autowired
    private MusicService musicService;
    @Autowired
    private LoveMusicService loveMusicService;
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
    @RequestMapping("/get")
    public ResponseEntity<byte[]> get(String path){
        if(path == null || path.length() == 0){
            return ResponseEntity.badRequest().build();
        }
        String fileName = path + ".mp3";
        File file = new File(save_path + fileName);
        byte[] ret = null;
        try {
            ret=  Files.readAllBytes(file.toPath());
            if(ret == null){
                return  ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(ret);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping("/delete")
    public ResponseBodyMessage<Boolean> deleteMusicById(@RequestParam String id){
        int deleteId = Integer.parseInt(id);
        //1.查询是否有这个id的音乐
        Music music = musicService.selectById(deleteId);
        if(music == null){//没有这个音乐
            return new ResponseBodyMessage<>(-1,"没有该音乐删除失败" ,false);
        }
        //如果找到了这个音乐
        //1.在服务器中删除这个音乐
       String title =  music.getTitle();
       File file = new File(save_path + title + ".mp3");
        System.out.println(file.getPath());
         boolean flag = file.delete();
         if(flag){
             //在数据库中删除
           int ret =  musicService.deleteOne(deleteId);
           if(ret == 1){
               loveMusicService.deleteMusicById(deleteId);
               return new ResponseBodyMessage<>(1,"删除成功",true);
           }else {
               return  new ResponseBodyMessage<>(-1,"删除失败",false);
           }

         }else {
             return new ResponseBodyMessage<>(-1,"删除失败",false);
         }

    }

    @RequestMapping("/deleteSel")
    public ResponseBodyMessage<Boolean> deleteAll(@RequestParam("id[]") List<Integer> id){
        Music music = null;
        int i = 0;
        for( i = 0 ; i < id.size() ; i++){
            int deleteId = id.get(i);
            //1.查询是否有这个id的音乐
             music = musicService.selectById(deleteId);
            if(music == null){//没有这个音乐
                return new ResponseBodyMessage<>(-1,"没有该音乐删除失败" ,false);
            }
            //如果找到了这个音乐
            //1.在服务器中删除这个音乐
            String title =  music.getTitle();
            File file = new File(save_path + title + ".mp3");
            System.out.println(file.getPath());
            boolean flag = file.delete();
            if(flag){
                //在数据库中删除
                int ret =  musicService.deleteOne(deleteId);
                if(ret != 1){
                    return  new ResponseBodyMessage<>(-1,"删除失败",false);
                }else {
                   loveMusicService.deleteMusicById(deleteId);
                }
            }else {
                return new ResponseBodyMessage<>(-1,"删除失败",false);
            }
        }
        if(i == id.size()){
            return new ResponseBodyMessage<>(1,"批量删除成功",true);
        }else{
            return new ResponseBodyMessage<>(1,"批量删除失败",true);
        }
    }
//required = false 可以不传参
@RequestMapping("/findmusic")
    public ResponseBodyMessage<List<Music>>findMusic(@RequestParam(required = false) String musicName){
        List<Music> musicList = null;
        System.out.println(musicName);
       musicList =  musicService.findMusic(musicName);
       return new ResponseBodyMessage<>(1,"查询成功",musicList);
    }
}
