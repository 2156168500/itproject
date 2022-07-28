package com.fjh.mapper;

import com.fjh.pojo.Lovemusic;
import com.fjh.pojo.Music;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
    /**
     * 如果没有传入具体的歌曲名，显示当前用户收藏的所有音乐
     * @param userId
     * @return
     */
    List<Music> findLoveMusicByUserId(int userId);
    /**
     * 根据某个用户的ID和歌曲名称查询，某个用户收藏的音乐
     * @param musicName
     * @param userId
     * @return
     */
    List<Music> findLoveMusicBykeyAndUID(String musicName, int userId);

    /**
     * 移除收藏的音乐
     * @param userId
     * @param musicId
     * @return
     */
    boolean deleteLoveMusic(Integer userId,Integer musicId);
    /**
     * 当删除库中的音乐的时候，同步删除lovemusic中的数据
     * @param musicId
     * @return
     */
    int deleteLoveMusicById(int musicId);
}
