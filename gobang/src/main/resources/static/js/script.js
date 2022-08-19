let gameInfo = {
    roomId: null,
    thisUserId: null,
    thatUserId: null,
    isWhite: true,
}

//////////////////////////////////////////////////
// 设定界面显示相关操作
//////////////////////////////////////////////////

function setScreenText(me) {
    let screen = document.querySelector('#screen');
    if (me) {
        screen.innerHTML = "轮到你落子了!";
    } else {
        screen.innerHTML = "轮到对方落子了!";
    }
}

//////////////////////////////////////////////////
// 初始化 websocket
//////////////////////////////////////////////////
let  websocket = new WebSocket("ws:127.0.0.1:8080/game");
websocket.onclose = function () {
    alert("链接断开")
}
websocket.onerror = function () {
    alert("网络中断,链接断开")
  //  location.assign("./game_hall.html");
    location.replace("./game_hall.html")
}

websocket.onbeforeunload = function(){
    websocket.close();
}

websocket.onmessage = function (e) {
    let ret = JSON.parse(e.data);
    console.log(ret);
    if(ret.message != "gameReady"){
        console.log("响应错误")
        return;
    }
    if (! ret.ok ){
        console.log("响应失败")
        return;
    }
    if(ret.message == "gameReady"){
        console.log("gameReady")
        gameInfo.roomId = ret.roomId;
        gameInfo.thatUserId = ret.thatUserId;
        gameInfo.thisUserId =  ret.thisUserId
        gameInfo.isWhite = (gameInfo.thisUserId == ret.whiteUser);
        console.log(gameInfo)
        initGame();
        setScreenText(gameInfo.isWhite);
    }else if(ret.message == "repeatConnection"){
        alert("禁止多开")
       // location.assign("./login.html")
        location.replace("./login.html");
    }
}



// 处理服务器返回的响应数据

//////////////////////////////////////////////////
// 初始化一局游戏
//////////////////////////////////////////////////
function initGame() {
    // 是我下还是对方下. 根据服务器分配的先后手情况决定
    let me = gameInfo.isWhite;
    // 游戏是否结束
    let over = false;
    let chessBoard = [];
    //初始化chessBord数组(表示棋盘的数组)
    for (let i = 0; i < 15; i++) {
        chessBoard[i] = [];
        for (let j = 0; j < 15; j++) {
            chessBoard[i][j] = 0;
        }
    }
    let chess = document.querySelector('#chess');
    let context = chess.getContext('2d');
    context.strokeStyle = "#BFBFBF";
    // 背景图片
    let logo = new Image();
    logo.src = "image/sky.jpeg";
    logo.onload = function () {
        context.drawImage(logo, 0, 0, 450, 450);
        initChessBoard();
    }

    // 绘制棋盘网格
    function initChessBoard() {
        for (let i = 0; i < 15; i++) {
            context.moveTo(15 + i * 30, 15);
            context.lineTo(15 + i * 30, 430);
            context.stroke();
            context.moveTo(15, 15 + i * 30);
            context.lineTo(435, 15 + i * 30);
            context.stroke();
        }
    }

    // 绘制一个棋子, me 为 true
    function oneStep(i, j, isWhite) {
        context.beginPath();
        context.arc(15 + i * 30, 15 + j * 30, 13, 0, 2 * Math.PI);
        context.closePath();
        var gradient = context.createRadialGradient(15 + i * 30 + 2, 15 + j * 30 - 2, 13, 15 + i * 30 + 2, 15 + j * 30 - 2, 0);
        if (!isWhite) {
            gradient.addColorStop(0, "#0A0A0A");
            gradient.addColorStop(1, "#636766");
        } else {
            gradient.addColorStop(0, "#D1D1D1");
            gradient.addColorStop(1, "#F9F9F9");
        }
        context.fillStyle = gradient;
        context.fill();
    }

    chess.onclick = function (e) {
        if (over) {
            return;
        }
        if (!me) {
            return;
        }
        let x = e.offsetX;
        let y = e.offsetY;
        // 注意, 横坐标是列, 纵坐标是行
        let col = Math.floor(x / 30);
        let row = Math.floor(y / 30);
        if (chessBoard[row][col] == 0) {
            // 发送坐标给服务器, 服务器要返回结果
            send(row, col);

            // 留到浏览器收到落子响应的时候再处理(收到响应再来画棋子)
            //  oneStep(col, row, gameInfo.isWhite);
            //  chessBoard[row][col] = 1;
        }
    }

    function send(row, col) {
        let req = {
            message: 'putChess',
            userId: gameInfo.thisUserId,
            row: row,
            col: col
        };

        websocket.send(JSON.stringify(req));
    }
    //
    // initGame();

    // 之前 websocket.onmessage 主要是用来处理了游戏就绪响应. 在游戏就绪之后, 初始化完毕之后, 也就不再有这个游戏就绪响应了. 
    // 就在这个 initGame 内部, 修改 websocket.onmessage 方法~~, 让这个方法里面针对落子响应进行处理!
    websocket.onmessage= function (e) {
        console.log(e.data);
        let rep = JSON.parse(e.data);
        if(rep.message != "putChess"){
            console.log("响应错误")
            return ;
        }
        if(rep.userId == gameInfo.thisUserId){
            oneStep(rep.col,rep.row,gameInfo.isWhite);
        }
        if(rep.userId == gameInfo.thatUserId){
            oneStep(rep.col,rep.row,!gameInfo.isWhite);
        }
        chessBoard[rep.row][rep.col] = 1;
        me = !me;
        let WinText = document.querySelector("#screen");
        if (rep.winner != 0) {
            // 胜负已分
            if (rep.winner == gameInfo.thisUserId) {
               // alert("你赢了!");
                WinText.innerHTML= '你赢了'

            } else {
                //alert("你输了");
                 WinText.innerHTML = '你输了'
            }
            // 如果游戏结束, 则关闭房间, 回到游戏大厅.
            let backButton = document.createElement("button");
            backButton.id = "backButton";
            backButton.innerHTML = '返回大厅'
            let div = document.querySelector(".container>div");
            div.append(backButton);
            backButton.onclick = function(){
                location.replace("/game_hall.html")
            }

        }else{
            // 3. 更新界面显示
            setScreenText(me);
        }


    }


}


