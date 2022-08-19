package com.fjh.util;

import lombok.Data;

@Data
public class GameResponse {
    private String Message;
    private int userId;
    private int row;
    private int col;
    private int winner;

}
