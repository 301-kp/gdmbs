$(function () {
    layui.use('form', function () {
        var form = layui.form; //只有执行了这一步，部分表单元素才会自动修饰成功
        form.render();
    });
});


$(function () {
    $(document).keydown(function (event) {
        if (event.keyCode == 13) {//回车键对应code值为13
            $(".login").click();//类选择器选择按钮
        }
    });


    $(".login").click(function () {
        var identity=$("#identity").val();
        var url='';
        if(identity=="学生"){
            url="/student/login";
        }else if(identity=="教师"){
            url="/teacher/login";
        }else if(identity=="管理员"){
            url="/admin/login";
        }
        $.ajax({
            url: url,
            type: "post",
            dataType:"text",
            data:{
                "id":$("#id").val(),
                "password":$("#password").val()
            },
            success:function (data) {
                if(data=="admin"){
                    window.location.href="admin.html";
                }else if(data=="teacher"){
                    window.location.href="teacher.html";
                }else if(data=="student"){
                    window.location.href="student.html";
                }else if(data=="err"){
                    newhint("已有账号登录，请先前往注销");
                }else{
                    newhint("密码错误");
                }
            }

        })
    })


});

function newhint(message){
    var layer = layui.layer;
    layer.msg(message);
}