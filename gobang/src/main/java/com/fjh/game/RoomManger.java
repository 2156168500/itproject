package com.fjh.game;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class RoomManger {
    private ConcurrentHashMap<String,Room> rooms = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer ,String> userIdToRoomId = new ConcurrentHashMap<>();
    public void  add(Room room,Integer userId1,Integer userId2){
        rooms.put(room.getRoomID(),room);
        userIdToRoomId.put(userId1,room.getRoomID());
        userIdToRoomId.put(userId2,room.getRoomID());
    }

    public void remove(Room room, Integer userId1, Integer userId2){
        rooms.remove(room.getRoomID());
        userIdToRoomId.remove(userId1);
        userIdToRoomId.remove(userId2);
    }
    public Room getRoomByRoomId(String roomID){
        return rooms.get(roomID);
    }
    public Room getRoomByUserId(Integer userId){
        String roomId = userIdToRoomId.get(userId);
        if(roomId == null){
            return null;
        }
        return rooms.get(roomId);
    }
}
