package com.fjh.commity.service;

import com.fjh.commity.entity.User;
import com.fjh.commity.util.HostHolder;
import com.fjh.commity.util.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
public class FollowService {
    /**
     * 关注
     */
    @Autowired
    private RedisTemplate redisTemplate;
    public void follow(int userId,int entityType,int entityId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                //因为这里涉及两个数据库的操作，所以要用事务
                //1.首先获取两个key
                String followee = RedisKeyUtils.getFolloweeKey(userId,entityType);
                String follower = RedisKeyUtils.getFollowerKey(entityType,entityId);
                operations.multi();
                operations.opsForZSet().add(followee,entityId,System.currentTimeMillis());
                operations.opsForZSet().add(follower,userId,System.currentTimeMillis());
                return operations.exec();
            }
        });
    }

    public void unFollow(int userId,int entityType,int entityId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                //因为这里涉及两个数据库的操作，所以要用事务
                //1.首先获取两个key
                String followee = RedisKeyUtils.getFolloweeKey(userId,entityType);
                String follower = RedisKeyUtils.getFollowerKey(entityType,entityId);
                operations.multi();
                operations.opsForZSet().remove(followee,entityId);
                operations.opsForZSet().remove(follower,userId);
                return operations.exec();
            }
        });
    }

    /**
     * 查询当前用户关注的人数
     */

    public long getFolloweeCount(int userId,int entityType){
        String redisKey = RedisKeyUtils.getFolloweeKey(userId,entityType);
        return redisTemplate.opsForZSet().zCard(redisKey);
    }

    /**
     * 查询当前实体的粉丝
     */

    public long getFollowerCount(int entityType,int entityId){
        String redisKey = RedisKeyUtils.getFollowerKey(entityType,entityId);
        return redisTemplate.opsForZSet().zCard(redisKey);
    }

    /**
     * 查询当前用户是否关注了某个实体
     */

    public boolean hasFollow(int userId,int entityType,int entityId){
        String redisKey = RedisKeyUtils.getFolloweeKey(userId,entityType);
        return redisTemplate.opsForZSet().score(redisKey,entityId) != null;
    }

}
