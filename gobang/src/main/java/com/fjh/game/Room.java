package com.fjh.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fjh.GobangApplication;
import com.fjh.pojo.User;
import com.fjh.service.UserService;
import com.fjh.util.GameRequest;
import com.fjh.util.GameResponse;
import lombok.Data;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.UUID;
@Data
public class Room {
    private String RoomID;
    private User user1;
    private User user2;
    private int [][]board = new int[MAX_ROW][MAX_COL];
    private ObjectMapper objectMapper ;
    private OnlineUserMessage onlineUserMessage ;
    private RoomManger roomManger;
    private UserService userService;
    private static int MAX_ROW = 15;
    private static int MAX_COL = 15;
    public Room(){
        RoomID = UUID.randomUUID().toString();
        objectMapper = GobangApplication.context.getBean(ObjectMapper.class);
        onlineUserMessage = GobangApplication.context.getBean(OnlineUserMessage.class);
        roomManger = GobangApplication.context.getBean(RoomManger.class);
        userService = GobangApplication.context.getBean(UserService.class);
    }

    public void putChess(String payload) throws JsonProcessingException {
        GameRequest request = objectMapper.readValue(payload,GameRequest.class);
        GameResponse response = new GameResponse();

        //1.在棋盘上落子,0 表示当前位置没有落子,1表示玩家一的子,2表示玩家2的子
        int chess = request.getUserId() == user1.getUserId() ? 1 : 2;
        int row = request.getRow();
        int col = request.getCol();
        if(board[row][chess] != 0){
            System.out.println("已经有子不能再落子了");
            return;
        }
        board[row][col] = chess;
        //2.判定胜负
        int winner = 0;
       winner =  checkWinner( row, col);
        printBoard(roomManger.getRoomByUserId(user1.getUserId()).getRoomID());
        //3.返回数据给客户端
        WebSocketSession session1 = onlineUserMessage.getFromGameRoom(user1.getUserId());
        WebSocketSession session2 = onlineUserMessage.getFromGameRoom(user2.getUserId());
        if(session1 == null){//说明用户1下线了,直接判定为用户二获胜
            winner = user2.getUserId();
            System.out.println("用户1下线了,直接判定为用户二获胜");
        }
        if(session2 == null){//说明用户2下线了,直接判定为用户一获胜
            winner = user1.getUserId();
            System.out.println("用户2下线了,直接判定为用户一获胜");
        }
        response.setMessage("putChess");
        response.setUserId(request.getUserId());
        response.setCol(col);
        response.setRow(row);
        response.setWinner(winner);
        try {
            if(session1 != null){//返回给客户端 1 响应
                session1.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
            }
            if(session2 != null){//返回个客户端二响应
                session2.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        if(winner != 0){
            int winUserId = winner;
            int loseUserId = winner == user1.getUserId() ? user2.getUserId() : user1.getUserId();
            userService.userWin(winUserId);
            userService.userLose(loseUserId);
          Room room = roomManger.getRoomByUserId(user1.getUserId());
          roomManger.remove(room,user1.getUserId(),user2.getUserId());
        }
    }

    /**
     * 判断棋面上是否是否出现五子连珠
     * @param row 新落子的行
     * @param col 新落子的列
     */
    private int  checkWinner(int row, int col) {
        int chess = board[row][col];
        for(int c = col - 4 ; c <= col; c++){
            try {
                if(board[row][c] == chess
                   &&board[row][c + 1] ==chess
                   &&board[row][c + 2] == chess
                   &&board[row][c + 3] == chess
                   &&board[row][c + 4] == chess
                ){
                    return chess == 1 ? user1.getUserId() : user2.getUserId();
                }

            }catch (ArrayIndexOutOfBoundsException e){
                continue;
            }
        }

        // 2. 检查所有列
        for (int r = row - 4; r <= row; r++) {
            try {
                if (board[r][col] == chess
                        && board[r + 1][col] == chess
                        && board[r + 2][col] == chess
                        && board[r + 3][col] == chess
                        && board[r + 4][col] == chess) {
                    return chess == 1 ? user1.getUserId() : user2.getUserId();
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                continue;
            }
        }

        // 3. 检查左对角线
        for (int r = row - 4, c = col - 4; r <= row && c <= col; r++, c++) {
            try {
                if (board[r][c] == chess
                        && board[r + 1][c + 1] == chess
                        && board[r + 2][c + 2] == chess
                        && board[r + 3][c + 3] == chess
                        && board[r + 4][c + 4] == chess) {
                    return chess == 1 ? user1.getUserId() : user2.getUserId();
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                continue;
            }
        }

        // 4. 检查右对角线
        for (int r = row - 4, c = col + 4; r <= row && c >= col; r++, c--) {
            try {
                if (board[r][c] == chess
                        && board[r + 1][c - 1] == chess
                        && board[r + 2][c - 2] == chess
                        && board[r + 3][c - 3] == chess
                        && board[r + 4][c - 4] == chess) {
                    return chess == 1 ? user1.getUserId() : user2.getUserId();
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                continue;
            }
        }

        // 胜负未分, 就直接返回 0 了.
        return 0;
    }


    private void printBoard(String roomId) {
        System.out.println("===========================================");
        System.out.println("打印棋盘内容" + roomId);
        for(int i = 0; i < MAX_ROW ; i++){
            for(int j = 0; j < MAX_COL ; j++){
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
        System.out.println("===========================================");

    }

}
