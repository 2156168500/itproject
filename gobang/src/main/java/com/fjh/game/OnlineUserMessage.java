package com.fjh.game;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OnlineUserMessage {
    private ConcurrentHashMap<Integer, WebSocketSession> gameHall = new ConcurrentHashMap<>();
    public void interGameHall(int userId,WebSocketSession session){
        gameHall.put(userId,session);

    }
    public void enterGameHall(int userId){
        gameHall.remove(userId);
    }
    public WebSocketSession getFromGameHall(int userId){
        return gameHall.get(userId);
    }
}
