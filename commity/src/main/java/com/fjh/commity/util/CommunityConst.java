package com.fjh.commity.util;

public interface CommunityConst {
    int ACTIVATION_SUCCESS = 0;
    int ACTIVATION_REPEAT = 1;
    int ACTIVATION_FILE = 2;
    /**
     * 默认的登录凭证的存储时间
     */
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;
    /**
     * 选中记住我的时候登录凭证的存储时间
     */
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;
    /**
     * 回复实体的类型：评论
     */
    int ENTITY_TYPE_COMMENT = 1;
    /**
     * 实体类型：回复
     */
    int ENTITY_TYPE_REPLAY = 2;
    /**
     * 实体类型：用户
     */
    int ENTITY_TYPE_USER = 3;
    /**
     * 主题：评论
     */
    String TOPIC_COMMENT = "comment";
    /**
     * 主题：赞
     */
    String TOPIC_LIKE = "like";
    /**
     * 主题：关注
     */
    String TOPIC_FOLLOW = "follow";
    /**
     * 系统用户id
     */
    int SYSTEM_USER = 1;
}
