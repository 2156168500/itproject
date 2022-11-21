var main = {};

main.info = function () {

}
main.exit = function () {
    if(!confirm("是否退出系统")){
        return;
    }
    location.href = "comm/exit";
}