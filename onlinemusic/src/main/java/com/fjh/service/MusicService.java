package com.fjh.service;

import com.fjh.mapper.MusicMapper;
import com.fjh.pojo.Music;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
}
