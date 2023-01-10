package com.fjh.commity.service;

import com.fjh.commity.util.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 点赞的实现
     */
    public void like(int userId,int entityType ,int entityId,int entityUserId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                //获取两个redis的key
                String entityLikeKey = RedisKeyUtils.getLikeKey(entityType,entityId);
                String entityUserKey = RedisKeyUtils.getLikeUser(entityUserId);
                //查询当前用户是否为该实体点过赞
                boolean isMember = operations.opsForSet().isMember(entityLikeKey,userId);
                //开启事务
                redisTemplate.multi();
                if(isMember){//当前用户已经为该实体点过赞了
                    redisTemplate.opsForSet().remove(entityLikeKey,userId);
                    redisTemplate.opsForValue().decrement(entityUserKey);
                }else{//当前用户尚未为该实体点过赞了
                    redisTemplate.opsForSet().add(entityLikeKey,userId);
                    redisTemplate.opsForValue().increment(entityUserKey);
                }
                return redisTemplate.exec();
            }
        });

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
    //获取某个用户被赞的次数
    public int findLikeUserCount(int userId){
        String entityUserKey = RedisKeyUtils.getLikeUser(userId);
        Integer likeUserCount = (Integer) redisTemplate.opsForValue().get(entityUserKey);
        return likeUserCount == null ? 0 : likeUserCount;
    }
}
