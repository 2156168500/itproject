package com.fjh.util;

import lombok.Data;

@Data
public class GameReadyResponse {
    private  String message;
    private  boolean ok;
    private String reason;
    private String roomId;
    private int thisUserId;
    private int thatUserId;
    private int whiteUser;

}
