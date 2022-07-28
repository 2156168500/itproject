package com.fjh.service;

import com.fjh.mapper.LoveMusicMapper;
import com.fjh.pojo.Lovemusic;
import com.fjh.pojo.Music;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LoveMusicService {
    @Resource
    private LoveMusicMapper loveMusicMapper;
    public boolean insertLoveMusic(Integer userId,Integer musicId){
        Music music = loveMusicMapper.findLoveMusicByMusicIdAndUserId(userId, musicId);
        if(music != null){
            return false;
        }
        return loveMusicMapper.insertLoveMusic(new Lovemusic(null,userId,musicId));
    }

    public List<Music> findLoveMusic(String musicName,Integer userId){
        if(musicName == null){
            return loveMusicMapper.findLoveMusicByUserId(userId);
        }else{
            return loveMusicMapper.findLoveMusicBykeyAndUID(musicName,userId);
        }
    }

    public boolean deleteLoveMusic(Integer userId,Integer musicId){
        return loveMusicMapper.deleteLoveMusic(userId,musicId);
    }
    public int deleteMusicById(Integer musicId){
        return loveMusicMapper.deleteLoveMusicById(musicId);
    }
}
