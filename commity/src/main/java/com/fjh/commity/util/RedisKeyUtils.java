package com.fjh.commity.util;

/**
 * 用来实现生成redis的可以
 */
public class RedisKeyUtils {
    private static final String CONNECT = ":";
    private static final String LIKE_ENTITY = "like:entity";
    private static final String LIKE_USER="like:user";
    public static String getLikeKey (int entityType,int entityId){
        return LIKE_ENTITY + CONNECT + entityType + CONNECT +entityId;
    }

    public static String getLikeUser(int userId){
        return LIKE_USER+CONNECT+userId;
    }


}
