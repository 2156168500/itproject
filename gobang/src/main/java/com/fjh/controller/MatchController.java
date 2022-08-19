package com.fjh.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fjh.game.Matcher;
import com.fjh.game.OnlineUserMessage;
import com.fjh.pojo.User;
import com.fjh.util.MatchRequest;
import com.fjh.util.MatchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
@Controller
public class MatchController extends TextWebSocketHandler {
    @Autowired
    private ObjectMapper objectMapper ;
    @Autowired
    private OnlineUserMessage onlineUserMessage;
    @Autowired
    private Matcher matcher;
    @Override
      public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //上线处理
        try {
            User user = (User) session.getAttributes().get("userInfo");
            WebSocketSession temp = onlineUserMessage.getFromGameHall(user.getUserId());
            if(temp != null){//已经登录了,不能重复登录
                MatchResponse response = new MatchResponse();
                System.out.println("已经登录");
                response.setOk(true);
                response.setReason("已经登录,不能重复登录");
                response.setMessage("repetition");
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
                session.close();
                return;

            }
            onlineUserMessage.enterGameHall(user.getUserId(),session);
        }catch (NullPointerException e){
          // e.printStackTrace();
            MatchResponse response = new MatchResponse();
            response.setOk(false);
            response.setReason("您还没有登录");
            response.setMessage("stopMatch");
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
        }

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        MatchResponse response = new MatchResponse();
           String payload =  message.getPayload();
        MatchRequest matchRequest = objectMapper.readValue(payload, MatchRequest.class);
        User user =(User) session.getAttributes().get("userInfo");
        if(matchRequest.getMessage().equals("startMatch")){
            //1.创建一个类表示匹配队列,把当前用户加到匹配队列里去
            matcher.pushMatcher(user);
            //2.返回数据
            response.setOk(true);
            response.setMessage("startMatch");
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
        }else if(matchRequest.getMessage().equals("stopMatch")){
            //1.创建一个类表示匹配队列,把当前用户从匹配队列中移除
            matcher.removeMatcher(user);
            //2.返回数据
            response.setOk(true);
            response.setMessage("stopMatch");
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
        }else {
            response.setOk(false);
            response.setReason("非法请求");
        }

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        try {
            User user = (User) session.getAttributes().get("userInfo");
            WebSocketSession temp = onlineUserMessage.getFromGameHall(user.getUserId());

            if(temp == session){//多开情况的处理
                onlineUserMessage.exitGameHall(user.getUserId());
                matcher.removeMatcher(user);
            }

        }catch (NullPointerException e){
            e.printStackTrace();
            MatchResponse response = new MatchResponse();
            response.setOk(false);
            response.setReason("您还没有登录");
            response.setMessage("stopMatch");
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        try {
            User user = (User) session.getAttributes().get("userInfo");
            WebSocketSession temp = onlineUserMessage.getFromGameHall(user.getUserId());
            if(session == temp){
                onlineUserMessage.exitGameHall(user.getUserId());
                matcher.removeMatcher(user);
            }

        }catch (NullPointerException e){
            System.out.println("链接断开 ,您还没有登录");

        }
    }
}
