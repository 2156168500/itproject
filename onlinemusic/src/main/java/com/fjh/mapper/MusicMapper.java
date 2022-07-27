package com.fjh.mapper;

import com.fjh.pojo.Music;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MusicMapper {
    int insert(Music music);
    Music selectOne(String title,String singer);

    /**
     * 通过id查找音乐
     * @param id 要查找的id
     * @return 查到的music
     */
    Music selectById(int id);

    /**
     * 删除单个音乐
     * @param id 要删除的音乐的id
     * @return
     */
    int deleteOne(int id);
}
