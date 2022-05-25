package com.fjh.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Blog implements Serializable {
    private int blogId;
    private String title;
    private String content;
    private int userId;
    private Timestamp post_time;
    public String getPostTime() {
        // 使用 SimpleDateFormat 来完成时间戳到格式化日期时间的转换.
        // 这个转换过程, 需要在构造方法中指定要转换的格式, 然后调用 format 来进行转换
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(post_time);
    }

}
