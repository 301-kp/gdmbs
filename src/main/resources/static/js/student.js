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

var totalPage;
var currentPage;

function page(currentPage){

    $(".bg").fadeIn(200);
    $(".loadingImage").fadeIn(200);
    interceptor();
    $.ajax({
        url:"/student/listTeacher",
        type:"post",
        dataType:"json",
        data:{
            "currentPage":currentPage
        },
        success:function (data) {
            $("#getAllTeacher").empty();
            totalPage=data["totalPage"];
            currentPage=data["currentPage"];
            if(currentPage==1){
                if(totalPage==1){
                    $('.first-page').attr("disabled",true);
                    $(".next-page").attr("disabled",true);
                    $(".last-page").attr("disabled",true);
                    $(".trailer-page").attr("disabled",true);
                }else {
                    $('.first-page').attr("disabled", true);
                    $(".next-page").attr("disabled", false);
                    $(".last-page").attr("disabled", true);
                    $(".trailer-page").attr("disabled", false);
                }
            }
            else if(currentPage==totalPage){
                $('.first-page').attr("disabled",false);
                $(".next-page").attr("disabled",true);
                $(".last-page").attr("disabled",false);
                $(".trailer-page").attr("disabled",true);
            }else {
                $('.first-page').attr("disabled",false);
                $(".next-page").attr("disabled",false);
                $(".last-page").attr("disabled",false);
                $(".trailer-page").attr("disabled",false);
            }

            $.each(data["list"],function(index,obj){
                var tr=$("<tr>")
                    .append('<p style="display:none">'+obj.teacherId+'</p>')
                    .append($("<td>").html('<a href="javascript:void(0)" onclick="viewInfo(this)" title="查看教师简介">'+obj.teacherName+'</a>'))
                    .append($("<td>").text(obj.departmentName))
                    .append($("<td>").text(obj.title))
                    .append($("<td>").text(obj.content))
                    .append($("<td>").html('<p>'+obj.total+'/'+obj.studentNum+'</p>'));
                //if(obj.confirmNum==obj.studentNum){
                if(obj.status=="true"){
                    var td=$("<td>").text('已确认');
                }else{
                    var td=$("<td>").html('<a href="javascript:void(0)" onclick="select(this)">选择</a>');
                }
                tr.append(td);
                $("#getAllTeacher").append(tr);
            });
            $(".bg").fadeOut(200);
            $(".loadingImage").fadeOut(200);
        }
    })

}

$(function(){
    $(".first-page").click(function () {
        currentPage=1;
        page(currentPage);
    });
    $(".next-page").click(function () {
        currentPage+=1;
        page(currentPage);
    });
    $(".last-page").click(function () {
        currentPage-=1;
        page(currentPage);
    });
    $(".trailer-page").click(function () {
        currentPage=totalPage;
        page(currentPage);
    })
});

function showInform(){
    interceptor();
    $.ajax({
        url:"/student/showInform",
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
        url:"/student/showName",
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
    $("#logout").click(function(){
        interceptor();
        $.ajax({
            url:"/student/stuLogout",
            type:"post",
            dataType:"text",
            success:function (data) {
                window.location.href="login.html";
            }
        })
    });

    $(".index-button").click(function(){
        $(".list").hide();
        $(".index-page").show();
    });

    $(".list-teacher-button").click(function(){
        $(".list").hide();
        $(".list-teacher").show();
        $("#getAllTeacher").empty();
        currentPage=1;
        page(currentPage);
    });

    $(".selectT").click(function(){
        var selectName=$(".teaName").val();
        $("#getAllTeacher").empty();
        interceptor();
        $.ajax({
            url:"/student/getTeacherByName",
            type:"post",
            dataType:"json",
            data:{
                "selectName":selectName
            },
            success:function (data) {
                $.each(data,function(index,obj) {
                        var tr = $("<tr>")
                            .append('<p style="display:none">' + obj.teacherId + '</p>')
                            .append($("<td>").html('<a href="javascript:void(0)" onclick="viewInfo(this)" title="查看教师简介">'+obj.teacherName+'</a>'))
                            .append($("<td>").text(obj.departmentName))
                            .append($("<td>").text(obj.title))
                            .append($("<td>").text(obj.content))
                            .append($("<td>").html('<p>' + obj.total + '/' + obj.studentNum + '</p>'));
                    if(obj.status=="true"){
                        var td=$("<td>").text('已确认');
                    }else{
                        var td=$("<td>").html('<a href="javascript:void(0)" onclick="select(this)">选择</a>');
                    }
                        tr.append(td);
                        $("#getAllTeacher").append(tr);
                    //}
                })
            }
        })
    });
    $(".selectAllT").click(function(){
        $("#getAllTeacher").empty();
        currentPage=1;
        page(currentPage);
    });

    $(".list-studentInfo-button").click(function(){
        $(".list").hide();
        $(".list-studentInfo").show();
        interceptor();
        $.ajax({
            url:"/student/viewStudentInfo",
            type:"post",
            dataType:"json",
            async:false,
            success:function (data) {
                $("#viewInfo").empty();
                var tr=$("<tr>")
                    .append($("<td>").text(data.studentId))
                    .append($("<td>").text(data.studentName))
                    .append($("<td>").text(data.departmentName))
                    .append($("<td>").text(data.phone))
                    .append($("<td>").text(data.status));
                $("#viewInfo").append(tr);
            }
        })
    });

    $(".teacher-choose-button").click(function(){
        $(".list").hide();
        $(".list-student").show();
        interceptor();
        $.ajax({
            url:"/student/viewChoose",
            type:"post",
            dataType:"json",
            async:false,
            success:function (data) {
                $("#viewChoose").empty();
                $.each(data["teacherList"],function(index,obj){
                    $("#viewChoose").empty();
                    var tr=$("<tr>")
                        .append($("<td>").text(obj.teacherName))
                        .append($("<td>").text(obj.departmentName))
                    if(data["status"]=="已选上"){
                        tr.append($("<td>").append(obj.phone))
                    }else{
                        tr.append($("<td>").append($("<td>").text("")))
                    }
                    tr .append($("<td>").html('<p>'+data["totalSelect"]+'/'+obj.studentNum+'</p>'))
                            .append($("<td>").text(data["status"]));
                    if(data["status"]=="已选上"){
                        tr.append($("<td>").append("已锁定"))
                    }else if(data["note"]=="true"){
                        tr.append($("<td>").append("不可操作"))
                    }else{
                        var td=$("<td>").html('<a href="javascript:void(0)" onclick="cancleSelect()">取消选择</a>');
                    }
                    //var td=$("<td>").html('<a href="javascript:void(0)" onclick="cancleSelect()">取消选择</a>');
                    tr.append(td);
                    $("#viewChoose").append(tr);
                })
            }
        })
    });

    $(".change-info-button").click(function(){
        $(".list").hide();
        $(".change-info").show();
    });

    $("#changeInfo").click(function(){
        var phone=$(".phone").val();
        var reg=/^1[3456789]\d{9}$/;
        if(!reg.test(phone)){
            newhint("请输入正确的手机号");
        }else {
            interceptor();
            $.ajax({
                url:"/student/changeInfo",
                type:"post",
                dataType:"text",
                data:{
                    "phone":phone
                },
                success:function (data) {
                    newhint(data);
                    $(".list-studentInfo-button").click();
                }
            })
        }

    })

    $(".change-password").click(function(){
        $(".list").hide();
        $(".change-passwd").show();
    });

    $("#change").click(function(){
        var oldPassword=$(".oldPassword").val();
        var newPassword=$(".newPassword").val();
        var confirmPassword=$(".confirmPassword").val();
        if(newPassword!=confirmPassword){
            newhint("密码不一致");
        }
        else{
            interceptor();
            $.ajax({
                url:"/student/changepasswd",
                type:"post",
                dataType:"text",
                data:{
                    "oldPassword":oldPassword,
                    "newPassword":newPassword
                },
                success:function (data) {
                    if(data=="true") {
                        window.location.href="login.html";
                    }
                    else {
                        newhint("旧密码错误");
                    }
                }
            })
        }
    });

    $("#reset").click(function(){
        $(".oldPassword").val("");
        $(".newPassword").val("");
        $(".confirmPassword").val("");
    });

    $(".bg1").click(function () {
        $(".bg1").hide(150);
        $("#teacher_info").hide(150);
    })
});

function viewInfo(obj){
    var v_teacherId=$(obj).parent().parent().find("p").eq(0).text();
    interceptor();
    $.ajax({
        url:"/student/viewInfo",
        type:"post",
        dataType:"text",
        async:true,
        data:{
            "teacherId":v_teacherId
        },
        success:function (data) {
            if(data.length==0){
                newhint("该教师没有简介");
            }
            else{
                hintTeacherInfo("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+data);
            }
        }
    })
}

function select(obj){
    var teacherId=$(obj).parent().parent().find("p").eq(0).text();
    interceptor();
    $.ajax({
        url:"/student/select",
        type:"post",
        dataType:"text",
        async:true,
        data:{
            "teacherId":teacherId
        },
        success:function (data) {
            if(data=="true"){
                newhint("选择成功")
                setTimeout(function(){
                    $(".teacher-choose-button").click();
                },1800)
            }else if(data=="false"){
                newhint("只可以选择一位指导老师")
            }else{
                newhint(data)
            }
        }
    })
}

function cancleSelect(){
    interceptor()
    $.ajax({
        url:"/student/delete",
        type:"post",
        dataType:"text",
        success:function () {
            $(".list-teacher-button").trigger("click");
        }
    })
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

function hintTeacherInfo(a) {
    $(".bg1").show(150);
    $("#teacher_info_body").html(a);
    $("#teacher_info").show(150);
}

function newhint(message){
    var layer = layui.layer;
    layer.msg(message);
}