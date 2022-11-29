package com.fjh.commity.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.Map;
import java.util.UUID;

public final class CommunityUtil {
    private CommunityUtil(){

    }

    /**
     * 生成一个速记的字符串
     * @return 生成的随机字符串
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    /**
     * 对Key加密
     */
    public static String md5(String key){
        if(StringUtils.isBlank(key)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    /**
     * 实现对JSON数据的处理
     */

    public static String getJsonString(int code, String msg, Map<String,Object> map){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",code);
        jsonObject.put("msg",msg);
        if(map != null){
            for(String key : map.keySet()){
                jsonObject.put(key,map.get(key));
            }
        }
        return jsonObject.toJSONString();
    }

    public static String getJsonString(int code, String msg){
        return getJsonString(code,msg,null);
    }
    public static String getJsonString(int code){
        return getJsonString(code,null,null);
    }
}
