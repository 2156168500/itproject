package com.fjh.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseBodyMessage<V> {
    private int code;
    private String message;
    private V data;
}
