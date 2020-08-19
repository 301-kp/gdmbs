(function(doc, win) {
    var docEl = doc.documentElement,
    resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
    recalc = function() {
    var clientWidth = docEl.clientWidth;
    if (!clientWidth) return;
    if(clientWidth >= 2560){
    docEl.style.fontSize = '200px';
    }else if(clientWidth <= 600){
    docEl.style.fontSize = '32px';
    }else{
    docEl.style.fontSize = 100 * (clientWidth / 1920) + 'px';
    }
    };
    if (!doc.addEventListener) return;
    win.addEventListener(resizeEvt, recalc, false);
    doc.addEventListener('DOMContentLoaded', recalc, false);
    })(document, window);


function showInform(){
    interceptor();
    $.ajax({
        url:"/teacher/showInform",
        type:"post",
        dataType:"text",
        success:function (data) {
            if(data.length==0){
            }else{
                $("#indexContent").text(data);
            }
        }
    })
}

function showName(){
    interceptor();
    $.ajax({
        url:"/teacher/showName",
        type:"get",
        dataType:"text",
        success:function (data) {
            $(".name").text('您好，'+data);
        }
    })
}

$(function(){
    showInform();
    showName();
});

$(function(){
    $(".index-button").click(function(){
        $(".list").hide();
        $(".index-page").show();
    });
    $(".complete-info-button").click(function(){
        $(".list").hide();
        $(".complete-info").show();
        interceptor();
        $.ajax({
            url:"/teacher/getInfo",
            type:"get",
            dataTyper:"json",
            success:function (data) {
                $(".phoneNumber").val(data.phone);
                $(".teaInfo").val(data.info);
            }
        })
    });
    $(".commitInfo").click(function(){
        interceptor();
        $.ajax({
            url:"/teacher/updateInfo",
            type:"get",
            dataTyper:"text",
            data:{
                "teaInfo":$(".teaInfo").val()
            },
            success:function (data) {
                newhint(data);
            }
        })
    });

    $(".list-student-button").click(function(){
        $(".list").hide();
        $(".list-student").show();
        interceptor();
        $.ajax({
            url:"/teacher/listStudent",
            type:"post",
            dataType:"json",
            success:function (data) {
                $("#list-student-tbody").empty();
                var list=data.studentList;
                var sta=data.teachersta;
                var num=data.num;
                var num1=data.num1;
                var num2=data.num2;

                $(".student-num").text('可选人数：'+num+'人');
                $(".select-num").text('待审核人数：'+num1+'人');
                $(".sure-num").text('已确认人数：'+num2+'人');

                if(sta=='false'){
                    $(".list-button").val('提交');
                }else if(sta=='true'){
                    $(".list-button").val('已提交');
                    $(".list-button").attr("disabled",true);
                }

                $.each(list,function(index,value){
                    var status=value.status;
                    var tr=$("<tr>")
                        .append($("<td>").text(value.studentid))
                        .append($("<td>").text(value.studentname))
                        .append($("<td>").text(value.departmentName))
                        .append($("<td>").text(value.phone));
                    if(status=="已选上"){
                        tr.append($("<td>").append("已选"));
                    }else{
                        tr.append($("<td>").append($('<a onclick="selectStudent(this)" style="margin-right:10px;cursor:pointer">选择</a><a onclick="deleteStudent(this)" style="cursor:pointer">删除</a>')));
                    }
                    $("#list-student-tbody").append(tr);
                })
            }
        })
    });



    $(".change-password").click(function(){
        $(".list").hide();
        $(".change-passwd").show();
    });


    $(".lockStu").click(function () {
        layer.confirm('确认后后续学生将不能再选您的毕设，是否确认？', {
            btn: ['确定','取消'] //按钮
        }, function(){
            interceptor();
            $.ajax({
                url:"/teacher/lock",
                type:"get",
                dataTyper:"text",
                success:function (data) {
                    newhint(data);
                    $(".list-button").val('已提交');
                    $(".list-button").attr("disabled",true);
                    $(".list-student-button").trigger("click");
                }
            })
        }, function(){
        });


    });

    $(".cancel").click(function () {
        $(".win-content").fadeOut(200);
        $(".win-mask").fadeOut(200);
    });

    $(".clear-info").click(function () {
        $(".old-passwd").val("");
        $(".new-passwd").val("");
        $(".new-passwd-sure").val("");
    });

    $(".change-passwd-sure").click(function () {
        if($(".new-passwd").val()==$(".new-passwd-sure").val()){
            interceptor();
            $.ajax({
                url:"/teacher/changepasswd",
                type:"post",
                dataTyper:"text",
                data:{
                    "oldPassword":$(".old-passwd").val(),
                    "newPassword":$(".new-passwd").val()
                },
                success:function (data) {
                    if(data=="true"){
                        window.location.href="login.html";
                    }else{
                        newhint("旧密码错误");
                    }
                }
            })
        }else{
            newhint("密码不一致");
        }
    });


    $(".logout").click(function () {
        interceptor();
        $.ajax({
            url:"/teacher/logout",
            type:"get",
            dataTyper:"text",
            success:function (data) {
                if(data=="true"){
                    window.location.href="login.html";
                }
            }
        })
    })

    $(".commitPhoneNumber").click(function () {
        var phoneReg=/^[1][3,4,5,7,8][0-9]{9}$/;
        var phone=$(".phoneNumber").val();
        if(!phoneReg.test(phone)){
            layer.tips('电话格式错误', '.phoneNumber', {
                tips: 3
            });
        }else{
            interceptor();
            $.ajax({
                url:"/teacher/updatePhoneNumber",
                type:"post",
                dataType:"text",
                data:{
                    "phoneNumber":phone
                },
                success:function (data) {
                    newhint(data);
                }
            })
        }
    })
});

function selectStudent(aa){
    var s_studentId=$(aa).parent().parent().find("td").eq(0).text();
    /*if(num2==num){
        newhint("指导人数已满");
    }else{*/
    interceptor();
    $.ajax({
        url:"/teacher/selectStudent",
        type:"get",
        dataTyper:"text",
        data:{
            "studentId":s_studentId
        },
        success:function (data) {
            newhint(data);
            $(".list-student-button").click();
            if(data=='最终确认'){
                var s_status1=$(aa).parent().parent().find("td").eq(4).text();
                $(".bg").fadeIn(150);
                $("#distribute_info").delay(1800).fadeIn(150);
                $.ajax({
                    url:"/teacher/randomDispatcherLock",
                    type:'post',
                    dataType:'text',
                    success:function () {
                        $(".bg").fadeOut(150);
                        $("#distribute_info").fadeOut(150);
                        $(".list-student-button").click();
                    }
                })

            }
        }
    })
    //}
}


function deleteStudent(aa) {
    var d_studentId=$(aa).parent().parent().find("td").eq(0).text();

    layer.confirm('是否确定删除学生？', {
        btn: ['确定','取消'] //按钮
    }, function(){
        interceptor();
        $.ajax({
            url:"/teacher/delete",
            type:"post",
            dataTyper:"text",
            data:{
                "studentId":d_studentId
            },
            success:function(data) {
                newhint(data);
                setTimeout(function(){
                    $(".list-student-button").click();
                },1800)
                if(data=="删除成功"){
                    newhint("删除成功");
                    $.ajax({
                        url:"/teacher/randomDispatcher",
                        type:"post",
                        dataTyper:"text",
                        data:{
                            "studentId":d_studentId
                        },
                        success:function(data) {
                        }
                    })
                }
            }
        })
    }, function(){
    });


}


function newhint(message){
    var layer = layui.layer;
    layer.msg(message);
}

function interceptor(){
    $.ajaxSetup({
        complete : function(xhr, status) {
            //拦截器实现超时跳转到登录页面
            // 通过xhr取得响应头
            var REDIRECT = xhr.getResponseHeader("REDIRECT");
            //如果响应头中包含 REDIRECT 则说明是拦截器返回的
            if (REDIRECT == "REDIRECT")
            {
                var win = window;
                while (win != win.top)
                {
                    win = win.top;
                }
                //重新跳转到 login.html
                win.location.href = xhr.getResponseHeader("CONTEXTPATH");
            }
        }
    });
}

/*
function deleteData(){
    layer.confirm('是否确定重置？请谨慎操作！', {
        btn: ['确定','取消'] //按钮
    }, function(){
        var id=$(".ids").val();
        interceptor();
        $.ajax({
            url:"/admin/deleteData",
            dataType:"text",
            success:function (data) {
                newhint(data);
            }
        })
    }, function(){
    });

}*/