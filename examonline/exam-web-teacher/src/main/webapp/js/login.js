var login = {};
login.toLogin = function () {
    $("#msg").html('');
    var param = {
        tname: $("#tname").val(),
        pass: $("#pass").val()
    }
    $.post('comm/login', param, function (t) {
        if (t == true) {
            // alert("登录成功")
            location.href='comm/main.html'
        } else {
            $("#msg").html('用户名或密码错误')
            $("#pass").val("")
        }
    })
}
