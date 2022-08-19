package com.fjh.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fjh.game.OnlineUserMessage;
import com.fjh.game.Room;
import com.fjh.game.RoomManger;
import com.fjh.pojo.User;
import com.fjh.service.UserService;
import com.fjh.util.GameReadyResponse;
import com.fjh.util.GameResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import sun.nio.cs.US_ASCII;

import java.io.IOException;
import java.util.Random;

@Controller
public class GameController extends TextWebSocketHandler {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RoomManger roomManger;
    @Autowired
    private OnlineUserMessage onlineUserMessage;
    @Autowired
    private UserService userService;
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //处理链接成功
        //1.检测用户的登录状态
        User user = (User) session.getAttributes().get("userInfo");
        if(user == null){//没有登录
            System.out.println("没有登录");
            GameReadyResponse response = new GameReadyResponse();
            response.setOk(false);
            response.setReason("尚未登录");
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
            return ;
        }
        //2.判断当前用户是否在房间中
        Room room = roomManger.getRoomByUserId(user.getUserId());
        if(room == null){
            System.out.println("用户不在房间中");
            GameReadyResponse response = new GameReadyResponse();
            response.setOk(false);
            response.setReason("用户尚未匹配不能进行游戏");
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
            return;
        }
        //3.判断多开的情况
        //当前用户如果在游戏大厅或者游戏房间中则当前用户已经在登录状态了
        if(onlineUserMessage.getFromGameHall(user.getUserId()) != null ||
        onlineUserMessage.getFromGameRoom(user.getUserId()) != null        ){
            System.out.println("检测到多开");
            GameReadyResponse response = new GameReadyResponse();
            response.setOk(true);
            response.setMessage("repeatConnection");
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
            return ;
        }
        //将当前用户和回话进行管理
        onlineUserMessage.enterGameRoom(user.getUserId(),session);

        synchronized (room){
            if(room.getUser1() == null){//第一个用户还没有进入设置
                //将当前用户作为user1
                room.setUser1(user);
                return;
            }
            if(room.getUser2() == null){//第二个用户还没有设置
                room.setUser2(user);
                int whiteUserId = 0;
                int ret  = new Random().nextInt();
                whiteUserId = ret % 2 == 0 ? room.getUser1().getUserId(): room.getUser2().getUserId();

                new Random();
                //玩家1准备就绪
                noticeGameReady(room,room.getUser1().getUserId(),room.getUser2().getUserId(), whiteUserId);
                //玩家二准备就绪
                noticeGameReady(room,room.getUser2().getUserId(),room.getUser1().getUserId(),whiteUserId);
                return ;
            }
        }

        System.out.println("房间满了");
        GameReadyResponse response = new GameReadyResponse();
        response.setOk(false);
        response.setReason("房间已满");
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));

    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
       User user = (User) session.getAttributes().get("userInfo");
       if(user == null){
           System.out.println("尚未登录 落子模块不能访问");
           return ;
       }
       //获取用户所在的游戏房间
        Room room = roomManger.getRoomByUserId(user.getUserId());
       if(room == null){
           System.out.println("房间为空");
        }
       room.putChess(message.getPayload());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
      User user = (User) session.getAttributes().get("userInfo");
      if(user == null){
          return;
      }
      WebSocketSession temp = onlineUserMessage.getFromGameRoom(user.getUserId());
      if(temp == session){
          System.out.println("下线");
          onlineUserMessage.exitGameRoom(user.getUserId());
      }
      noticeThatUserWin(user);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        User user = (User) session.getAttributes().get("userInfo");
        if(user == null){
            return;
        }
        WebSocketSession temp = onlineUserMessage.getFromGameRoom(user.getUserId());
        if(temp == session){
            System.out.println("下线");
            onlineUserMessage.exitGameRoom(user.getUserId());
        }
        noticeThatUserWin(user);
    }

    private void noticeGameReady(Room room, Integer thisUserId, Integer thatUserId,int whiteUserId) {
        System.out.println("先手" + whiteUserId);
        GameReadyResponse response = new GameReadyResponse();
        response.setOk(true);
        response.setMessage("gameReady");
        response.setThisUserId(thisUserId);
        response.setThatUserId(thatUserId);
        response.setWhiteUser(whiteUserId);
        WebSocketSession session = onlineUserMessage.getFromGameRoom(thisUserId);
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //处理对手下线
    private void noticeThatUserWin(User user) throws IOException {
        // 1. 根据当前玩家, 找到玩家所在的房间
        Room room = roomManger.getRoomByUserId(user.getUserId());
        if (room == null) {
            // 这个情况意味着房间已经被释放了, 也就没有 "对手" 了
            System.out.println("当前房间已经释放, 无需通知对手!");
            return;
        }

        // 2. 根据房间找到对手
        User thatUser = (user == room.getUser1()) ? room.getUser2() : room.getUser1();
        // 3. 找到对手的在线状态
        WebSocketSession webSocketSession = onlineUserMessage.getFromGameRoom(thatUser.getUserId());
        if (webSocketSession == null) {
            // 这就意味着对手也掉线了!
            System.out.println("对手也已经掉线了, 无需通知!");
            return;
        }
        // 4. 构造一个响应, 来通知对手, 你是获胜方
        GameResponse resp = new GameResponse();
        resp.setMessage("putChess");
        resp.setUserId(thatUser.getUserId());
        resp.setWinner(thatUser.getUserId());
        webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(resp)));

        // 5. 更新玩家的分数信息
        int winUserId = thatUser.getUserId();
        int loseUserId = user.getUserId();
        userService.userWin(winUserId);
        userService.userLose(loseUserId);

        // 6. 释放房间对象
        roomManger.remove(room, room.getUser1().getUserId(), room.getUser2().getUserId());
    }

}
