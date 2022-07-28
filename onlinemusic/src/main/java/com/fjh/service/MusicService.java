package com.fjh.service;

import com.fjh.mapper.MusicMapper;
import com.fjh.pojo.Music;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MusicService {
    @Resource
    private MusicMapper musicMapper;
    public int insert(Music music){
        return musicMapper.insert(music);
    }
    public Music selectOne(String title,String singer){
        return musicMapper.selectOne(title,singer);
    }
    public Music selectById(int id){
        return musicMapper.selectById(id);
    }
    public int deleteOne(int id){
        return musicMapper.deleteOne(id);
    }
    public List<Music>findMusic(String name){
        if(name != null){
            return musicMapper.findMusicByName(name);
        }else {
            return musicMapper.findMusic();
        }
    }
}
