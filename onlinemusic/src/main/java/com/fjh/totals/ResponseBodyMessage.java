package com.fjh.totals;

import lombok.Data;

@Data
public class ResponseBodyMessage<V> {
    private int status;//响应的状态码
    private String message;//响应的提示信息
    private V data;

    public ResponseBodyMessage(int status, String message, V data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

}
