$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	console.log("进入异步请求")
	$("#publishModal").modal("hide");
	var title = $("#recipient-name").val();
	var content = $("#message-text").val();
	$.post (
		CONTEXT_PATH + "/discuss/add",
		{"title":title,"content":content},
		function (data) {
			console.log(data);
			data = $.parseJSON(data);
			$("#hintModal").text(data.msg);
			$("#hintModal").modal("show");
			setTimeout(function(){
				$("#hintModal").modal("hide");
			}, 2000);
			if(data.code === 0){
				window.location.reload();
			}
		}
	);

}