package com.fjh.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fjh.pojo.User;
import com.fjh.util.MatchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 用来实现匹配的过程
 */
@Component
public class Matcher {
    @Autowired
    private ObjectMapper objectMapper;
    //天梯分小于2000的
    private Queue<User> normalQueue = new LinkedList<>();
    //天梯分(2000,3000)
    private Queue<User> highQueue = new LinkedList<>();
    //天梯分大于3000
    private Queue<User> veryHighQueue = new LinkedList<>();
    @Autowired
    private OnlineUserMessage onlineUserMessage ;
    @Autowired
    private RoomManger roomManger;

    public void pushMatcher(User user){
        int score = user.getScore();
        synchronized (normalQueue){
            if(score <= 2000){
                normalQueue.offer(user);
                normalQueue.notify();
            }else  if(score >200 && score <= 3000){
                highQueue.offer(user);
                highQueue.notify();
            }else{
                veryHighQueue.offer(user);
                veryHighQueue.notify();
            }
        }

    }
    public void removeMatcher(User user){
        int score = user.getScore();
        if(score <= 2000){
            synchronized(normalQueue){
                normalQueue.remove(user);
            }

        }else if(score > 2000 && score <= 3000){
            synchronized (highQueue){
                highQueue.remove(user);
            }

        }else{
            synchronized(veryHighQueue) {
                veryHighQueue.offer(user);
            }
        }
    }

    public Matcher(){
        Thread t1 = new Thread(){
            @Override
            public void run() {
                while (true){
                    handlerMatcher(normalQueue);
                }
            }
        };
        t1.start();

        Thread t2 = new Thread(){
            @Override
            public void run() {
                while (true){
                    handlerMatcher(highQueue);
                }
            }
        };
        t2.start();
        Thread t3 = new Thread(){
            @Override
            public void run() {
                while (true){
                    handlerMatcher(veryHighQueue);
                }
            }
        };
        t3.start();
    }
    public void handlerMatcher(Queue<User> queue){
        synchronized(queue){
            while (queue.size() < 2){
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            User user1 = queue.poll();
            User user2= queue.poll();
            WebSocketSession session1 = onlineUserMessage.getFromGameHall(user1.getUserId());
            WebSocketSession session2 = onlineUserMessage.getFromGameHall(user2.getUserId());
            if(session1 == null){//用户一下线了
                queue.offer(user2);
                return ;
            }
            if(session2 == null){//用户二下线了
                queue.offer(user1);
                return ;
            }
            //多开的情况,理论上在之前的建立连接的过程就避免了这种情况,但是还是要再次进行
            //判断,保证程序的可靠性
            if(session1 == session2){
                queue.offer(user1);
                return;
            }
            //下面是匹配成功之后的操作
            Room room = new Room();
            roomManger.add(room,user1.getUserId(),user2.getUserId());
            //2.给玩家1返回数据
            MatchResponse response = new MatchResponse();
            response.setOk(true);
            response.setMessage("successMatch");
            try {
                session1.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
                session2.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
        }
}
