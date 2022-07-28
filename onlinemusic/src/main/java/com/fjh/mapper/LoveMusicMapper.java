package com.fjh.mapper;

import com.fjh.pojo.Lovemusic;
import com.fjh.pojo.Music;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoveMusicMapper {
    /**
     * 查询用户是否收藏了该音乐
     * @return
     */
    Music findLoveMusicByMusicIdAndUserId(Integer userId,Integer musicId);

    /**
     * 收藏音乐
     * @param lovemusic
     * @return
     */
    boolean insertLoveMusic(Lovemusic lovemusic);
}
