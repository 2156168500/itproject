package com.fjh.commity.service;

import com.fjh.commity.util.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 点赞的实现
     */
    public void like(int userId,int entityType ,int entityId){
        //1.获取redisKey
        String redisKey = RedisKeyUtils.getLikeKey(entityType,entityId);
        //2.查看当前用户是否已经赞过该实体
        Boolean isMember = redisTemplate.opsForSet().isMember(redisKey, userId);
        if(isMember){//如果已经赞过了,就取消赞
            redisTemplate.opsForSet().remove(redisKey,userId);
        }else{//没有赞过就填加进去
            redisTemplate.opsForSet().add(redisKey,userId);
        }
    }

    /**
     * 获取点赞的数量
     */

    public long findLikeCount(int entityType,int entityId){
        String redisKey = RedisKeyUtils.getLikeKey(entityType,entityId);
        return redisTemplate.opsForSet().size(redisKey);
    }

    public int findLikeStatus(int userId,int entityType,int entityId){
        String redisKey = RedisKeyUtils.getLikeKey(entityType,entityId);
        return redisTemplate.opsForSet().isMember(redisKey,userId)?1:0;
    }
}
