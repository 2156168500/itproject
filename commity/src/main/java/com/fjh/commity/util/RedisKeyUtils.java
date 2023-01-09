package com.fjh.commity.util;

/**
 * 用来实现生成redis的可以
 */
public class RedisKeyUtils {
    private static final String CONNECT = ":";
    private static final String LIKE_ENTITY = "like:entity";
    public static String getLikeKey (int entityType,int entityId){
        return LIKE_ENTITY + CONNECT + entityType + CONNECT +entityId;

    }


}
