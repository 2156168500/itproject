package com.fjh.commity.util;

/**
 * 用来实现生成redis的可以
 */
public class RedisKeyUtils {
    private static final String CONNECT = ":";
    private static final String LIKE_ENTITY = "like:entity";
    private static final String LIKE_USER="like:user";
    private static final String FOLLOWEE = "followee";
    private static final String FOLLOWER = "follower";
    public static String getLikeKey (int entityType,int entityId){
        return LIKE_ENTITY + CONNECT + entityType + CONNECT +entityId;
    }

    public static String getLikeUser(int userId){
        return LIKE_USER+CONNECT+userId;
    }

    /**
     * 获取当前用户关注的实体的key
     */

    public static String getFolloweeKey(int userId,int entityType){
        return FOLLOWEE +CONNECT +userId +CONNECT +entityType;
    }

    /**
     * 获取当前实体的粉丝的Key
     */
    public static String getFollowerKey(int entityType,int entityId){
        return FOLLOWER + CONNECT + entityType + CONNECT + entityId;
    }

}
