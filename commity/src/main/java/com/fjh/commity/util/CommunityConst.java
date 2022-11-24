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
}
