$(function(){
	$("#sendBtn").click(send_letter);
	$(".close").click(delete_msg);
});

function send_letter() {
	$("#sendModal").modal("hide");
	$("#hintModal").modal("show");
	var toName = $("#recipient-name").val();
	var content = $("#message-text").val();
	$.post(
		CONTEXT_PATH + "/letter/save",
		{"toName":toName,"content":content},
		function (data) {
			data = $.parseJSON(data);
			if(data.code === 0){
				$("#hintBody").text("发送成功");
			}else {
				$("#hintBody").text(data.msg);
			}
			setTimeout(function(){
				$("#hintModal").modal("hide");
			}, 2000);
			location.reload();
		}
	)

}

function delete_msg() {
	// TODO 删除数据
	$(this).parents(".media").remove();
}