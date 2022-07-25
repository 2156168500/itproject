package com.fjh.mapper;

import com.fjh.pojo.Music;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MusicMapper {
    int insert(Music music);
    Music selectOne(String title,String singer);
}
