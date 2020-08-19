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

var phoneReg=/^[1][3,4,5,7,8][0-9]{9}$/;
var number=/^[0-9]*$/;

var upTeacherId;
var teacherId;
var teacherName;


var totalPage;
var currentPage;

var totalPage1;
var currentPage1;

var totalPage2;
var currentPage2;

var totalPage3;
var currentPage3;

var old_studentId;


function page(currentPage){
    $('.first-page').show();
    $(".next-page").show();
    $(".last-page").show();
    $(".trailer-page").show();

    $('.first-page2').hide();
    $(".next-page2").hide();
    $(".last-page2").hide();
    $(".trailer-page2").hide();

    $("#pageNum").show();
    $("#pageNum1").hide();
    interceptor();
    $.ajax({
        url:"/admin/listTeacher",
        type:"post",
        dataType:"json",
        data:{
            "currentPage":currentPage
        },
        success:function (data) {
            $("#listTeacher").empty();
            totalPage=data["totalPage"];
            currentPage=data["currentPage"];
            if(currentPage==1){
                if(totalPage==1){
                    $('.first-page').attr("disabled",true);
                    $(".next-page").attr("disabled",true);
                    $(".last-page").attr("disabled",true);
                    $(".trailer-page").attr("disabled",true);
                }
                else{
                    $('.first-page').attr("disabled",true);
                    $(".next-page").attr("disabled",false);
                    $(".last-page").attr("disabled",true);
                    $(".trailer-page").attr("disabled",false);
                }
            }
            else if(currentPage==totalPage){
                $('.first-page').attr("disabled",false);
                $(".next-page").attr("disabled",true);
                $(".last-page").attr("disabled",false);
                $(".trailer-page").attr("disabled",true);
            } else if(currentPage!=1 && currentPage!=totalPage){
                $('.first-page').attr("disabled",false);
                $(".next-page").attr("disabled",false);
                $(".last-page").attr("disabled",false);
                $(".trailer-page").attr("disabled",false);
            }
            $.each(data["list"],function(index,obj){
                var tr=$("<tr>")
                    .append($("<td>").text(obj.teacherId))
                    .append($("<td>").text(obj.teacherName))
                    .append($("<td>").text(obj.departmentName))
                    .append($("<td>").text(obj.title))
                    .append($("<td>").text(obj.content))
                    .append($("<td>").text(obj.total+'/'+obj.studentNum));
                if(obj.status=="true")
                    tr.append($("<td>").text("已确认"));
                else if(obj.status=="false")
                    tr.append($("<td>").text("未确认"));
                var td=$("<td>").html('<a href="javascript:void(0)" style="margin-right: 15px;" onclick="updateTeacher(this)">修改</a><a href="javascript:void(0)" style="margin-right: 15px;" onclick="deleteTeacher(this)">删除</a><a href="javascript:void(0)" style="margin-right: 15px;" onclick="cancleConfirm(this)">取消确认</a>');
                tr.append(td);
                $("#listTeacher").append(tr);
            });
            $("#pageNum").text('第 '+currentPage+'/'+totalPage+' 页');
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

$(function(){
    $(".first-page1").click(function () {
        currentPage1=1;
        page1(currentPage1);
    });
    $(".next-page1").click(function () {
        currentPage1+=1;
        page1(currentPage1);
    });
    $(".last-page1").click(function () {
        currentPage1-=1;
        page1(currentPage1);
    });
    $(".trailer-page1").click(function () {
        currentPage1=totalPage1;
        page1(currentPage1);
    })

});

function page1(currentPage1){
    $('.first-page1').show();
    $(".next-page1").show();
    $(".last-page1").show();
    $(".trailer-page1").show();

    $('.first-page3').hide();
    $(".next-page3").hide();
    $(".last-page3").hide();
    $(".trailer-page3").hide();
    interceptor();
    $.ajax({
        url:"/admin/listStudentStatus",
        type:"post",
        dataType:"json",
        data:{
            "currentPage":currentPage1,
            "departmentId":queryDepartmentid($("#selectMajor").val())
        },
        success:function (data) {
            $("#listStudent").empty();
            totalPage1=data["totalPage"];
            currentPage1=data["currentPage"];
            if(currentPage1==1){
                if(totalPage1==1){
                    $('.first-page1').attr("disabled",true);
                    $(".next-page1").attr("disabled",true);
                    $(".last-page1").attr("disabled",true);
                    $(".trailer-page1").attr("disabled",true);
                }
                else {
                    $('.first-page1').attr("disabled",true);
                    $(".next-page1").attr("disabled",false);
                    $(".last-page1").attr("disabled",true);
                    $(".trailer-page1").attr("disabled",false);
                }
            }
            else if(currentPage1==totalPage1){
                $('.first-page1').attr("disabled",false);
                $(".next-page1").attr("disabled",true);
                $(".last-page1").attr("disabled",false);
                $(".trailer-page1").attr("disabled",true);
            }
            else if(currentPage1!=1 && currentPage1!=totalPage1){
                $('.first-page1').attr("disabled",false);
                $(".next-page1").attr("disabled",false);
                $(".last-page1").attr("disabled",false);
                $(".trailer-page1").attr("disabled",false);
            }
            $.each(data["list"],function(index,obj){
                var tr=$("<tr>")
                    .append($("<td>").text(obj.studentid))
                    .append($("<td>").text(obj.studentname))
                    .append($("<td>").text(obj.departmentName))
                    .append($("<td>").text(obj.phone));
                $.each(obj,function(key,value){
                    if(key=="teacher"){
                        tr.append($("<td>").text(value["teacherName"]));
                    }
                });
                tr.append($("<td>").text(obj.status));
                tr.append($("<td>").html('<a href="javascript:void(0)" style="margin-right: 15px;" onclick="updateStudent(this)">修改</a><a href="javascript:void(0)" style="margin-right: 15px;" onclick="cancleAffirm(this)">取消选课</a><a href="javascript:void(0)" onclick="deleteStudent(this)">删除</a>'));
                $("#listStudent").append(tr);
            });
            $("#pageNum2").text('第 '+currentPage1+'/'+totalPage1+' 页');
        }
    })
}
$(function(){
    $(".first-page3").click(function () {
        currentPage3=1;
        page3(currentPage3);
    });
    $(".next-page3").click(function () {
        currentPage3+=1;
        page3(currentPage3);
    });
    $(".last-page3").click(function () {
        currentPage3-=1;
        page3(currentPage3);
    });
    $(".trailer-page3").click(function () {
        currentPage3=totalPage3;
        page3(currentPage3);
    })

});

function page3(currentPage3){
    $('.first-page1').hide();
    $(".next-page1").hide();
    $(".last-page1").hide();
    $(".trailer-page1").hide();

    $('.first-page3').show();
    $(".next-page3").show();
    $(".last-page3").show();
    $(".trailer-page3").show();

    interceptor();
    $.ajax({
        url:"/admin/listStudentByStatus",
        type:"post",
        dataType:"json",
        data:{
            "currentPage":currentPage3,
            "status":$("#selectStuStatus").val()
        },
        success:function (data) {
            $("#listStudent").empty();
            totalPage3=data["totalPage"];
            currentPage3=data["currentPage"];
            if(currentPage3==1){
                if(totalPage3==1){
                    $('.first-page3').attr("disabled",true);
                    $(".next-page3").attr("disabled",true);
                    $(".last-page3").attr("disabled",true);
                    $(".trailer-page3").attr("disabled",true);
                }
                else {
                    $('.first-page3').attr("disabled",true);
                    $(".next-page3").attr("disabled",false);
                    $(".last-page3").attr("disabled",true);
                    $(".trailer-page3").attr("disabled",false);
                }
            }
            else if(currentPage3==totalPage3){
                $('.first-page3').attr("disabled",false);
                $(".next-page3").attr("disabled",true);
                $(".last-page3").attr("disabled",false);
                $(".trailer-page3").attr("disabled",true);
            }
            else if(currentPage3!=1 && currentPage3!=totalPage3){
                $('.first-page3').attr("disabled",false);
                $(".next-page3").attr("disabled",false);
                $(".last-page3").attr("disabled",false);
                $(".trailer-page3").attr("disabled",false);
            }
            $.each(data["list"],function(index,obj){
                var tr=$("<tr>")
                    .append($("<td>").text(obj.studentid))
                    .append($("<td>").text(obj.studentname))
                    .append($("<td>").text(obj.departmentName))
                    .append($("<td>").text(obj.phone));
                $.each(obj,function(key,value){
                    if(key=="teacher"){
                        tr.append($("<td>").text(value["teacherName"]));
                    }
                });
                tr.append($("<td>").text(obj.status));
                tr.append($("<td>").html('<a href="javascript:void(0)" style="margin-right: 15px;" onclick="updateStudent(this)">修改</a><a href="javascript:void(0)" style="margin-right: 15px;" onclick="cancleAffirm(this)">取消选课</a><a href="javascript:void(0)" onclick="deleteStudent(this)">删除</a>'));
                $("#listStudent").append(tr);
            });
            $("#pageNum2").text('第 '+currentPage3+'/'+totalPage3+' 页');
        }
    })
}
$(function(){
    $(".first-page2").click(function () {
        currentPage2=1;
        page2(currentPage2);
    });
    $(".next-page2").click(function () {
        currentPage2+=1;
        page2(currentPage2);
    });
    $(".last-page2").click(function () {
        currentPage2-=1;
        page2(currentPage2);
    });
    $(".trailer-page2").click(function () {
        currentPage2=totalPage2;
        page2(currentPage2);
    })

});

function page2(currentPage2){
    $('.first-page').hide();
    $(".next-page").hide();
    $(".last-page").hide();
    $(".trailer-page").hide();

    $('.first-page2').show();
    $(".next-page2").show();
    $(".last-page2").show();
    $(".trailer-page2").show();

    $("#pageNum").hide();
    $("#pageNum1").show();

    interceptor();
    $.ajax({
        url:"/admin/listTeaByStatus",
        type:"post",
        dataType:"json",
        data:{
            "currentPage":currentPage2,
            "status":queryStatus($("#selectStatus").val())
        },
        success:function (data) {
            $("#listTeacher").empty();
            totalPage2=data["totalPage"];
            currentPage2=data["currentPage"];
            if(currentPage2==1){
                if(totalPage2==1||totalPage2==0){
                    $('.first-page2').attr("disabled",true);
                    $(".next-page2").attr("disabled",true);
                    $(".last-page2").attr("disabled",true);
                    $(".trailer-page2").attr("disabled",true);
                }
                else {
                    $('.first-page2').attr("disabled",true);
                    $(".next-page2").attr("disabled",false);
                    $(".last-page2").attr("disabled",true);
                    $(".trailer-page2").attr("disabled",false);
                }
            }
            else if(currentPage2==totalPage2){
                $('.first-page2').attr("disabled",false);
                $(".next-page2").attr("disabled",true);
                $(".last-page2").attr("disabled",false);
                $(".trailer-page2").attr("disabled",true);
            }
            else if(currentPage2!=1 && currentPage2!=totalPage2){
                $('.first-page2').attr("disabled",false);
                $(".next-page2").attr("disabled",false);
                $(".last-page2").attr("disabled",false);
                $(".trailer-page2").attr("disabled",false);
            }
            $.each(data["list"],function(index,obj){
                var tr=$("<tr>")
                    .append($("<td>").text(obj.teacherId))
                    .append($("<td>").text(obj.teacherName))
                    .append($("<td>").text(obj.departmentName))
                    .append($("<td>").text(obj.title))
                    .append($("<td>").text(obj.content))
                    .append($("<td>").text(obj.total+'/'+obj.studentNum));
                if(obj.status=="true")
                    tr.append($("<td>").text("已确认"));
                else if(obj.status=="false")
                    tr.append($("<td>").text("未确认"));
                var td=$("<td>").html('<a href="javascript:void(0)" style="margin-right: 15px;" onclick="updateTeacher(this)">修改</a><a href="javascript:void(0)" style="margin-right: 15px;" onclick="deleteTeacher(this)">删除</a><a href="javascript:void(0)" style="margin-right: 15px;" onclick="cancleConfirm(this)">取消确认</a>');
                tr.append(td);
                $("#listTeacher").append(tr);
            });
            $("#pageNum1").text('第 '+currentPage2+'/'+totalPage2+' 页');
        }
    })
}


$(function(){
    $(".cancel").click(function () {
        $("#rz").hide();
    });

    $(".index-b").click(function(){
        $(".list").hide();
        $(".index-page").show();
    });

    $(".addTeacher-b").click(function(){
        $(".list").hide();
        $(".addTeacher").show();
    });



    $("#addTea").click(function(){
        var departmentid=queryDepartmentid($("#departmentid").val());

        var studentNumber=$(".studentNum").val();
        var phone=$(".teacherPhone").val();
        if(!phoneReg.test(phone)){
            layer.tips('电话格式错误', '.teacherPhone', {
                tips: 3
            });
        }else if(!number.test(studentNumber)){
            layer.tips('人数必须为纯数字', '.studentNum', {
                tips: 3
            });
        }else {
            interceptor();
            $.ajax({
                url:"/admin/addTeacher",
                type:"post",
                dataType:"text",
                data:{
                    "teacherId":$(".teacherId").val(),
                    "teacherName":$(".teacherName").val(),
                    "studentNum":studentNumber,
                    "phone":phone,
                    "title":$(".title").val(),
                    "content":$(".content").val(),
                    "departmentid":departmentid
                },
                success:function (data) {
                    newhint(data);
                    $("#resetTea").click();
                }
            })
        }

    });

    $("#addStudent").click(function(){
        var studepartmentid=queryDepartmentid($("#student-departmentid").val());
        var phone=$(".stu-phone").val();
        if(!phoneReg.test(phone)){
            layer.tips('电话格式错误', '.stu-phone', {
                tips: 3
            });
        }else {
            interceptor();
            $.ajax({
                url:"/admin/addStudent",
                type:"post",
                dataType:"text",
                data:{
                    "studentid":$(".studentId").val(),
                    "studentname":$(".studentName").val(),
                    "phone":$(".stu-phone").val(),
                    "departmentid":studepartmentid
                },
                success:function (data) {
                    newhint(data);
                    if(data=="保存成功") {
                        $("#resetStudent").click();
                    }
                }
            })
        }

    });


    $(".cencel").click(function () {
        $("#az").hide();
    });

    $(".viewTeacher-b").click(function(){
        $(".list").hide();
        $(".viewTeacher").show();
        currentPage=1;
        page(currentPage);
    });

    $(".viewStudent-b").click(function(){
        $(".list").hide();
        $(".viewStudent").show();
    });

    $("#updateT").click(function(){
        interceptor();
        $.ajax({
            url:"/admin/changeinfo",
            type:"post",
            dataType:"text",
            data:{
                "teacherId":upTeacherId,
                "teacherName":$(".upTeacherName").val(),
                "studentNum":$(".upStudentNum").val(),
                "title":$(".upTitle").val(),
                "departmentName":$(".upDepartment").val(),
                "content":$(".upContent").val()
            },
            success:function (data) {
                $("#az").hide();
                $(".viewTeacher-b").click();
                newhint(data);
            }
        })
    });


    $('.but-cz').click(function(){
        var id=$(".ids").val();
        interceptor();
        $.ajax({
            url:"/admin/checkId",
            type:"post",
            dataType:"text",
            data:{
                "role":$("#role").find("option:selected").text(),
                "id":id
            },
            success:function (data) {
                if(data=="true"){
                    resetPassword();
                }else {
                    newhint(data);
                }
            }
        })
    });



    $(".selectStu").click(function(){
        currentPage1=1;
        page1(currentPage1);
    });

    $(".selectStuByStatus").click(function(){
        currentPage3=1;
        page3(currentPage3);
    });

    $(".selectTeaByStatus").click(function(){
        currentPage2=1;
        page2(currentPage2);
    });

    $(".changePassword-b").click(function(){
        $(".list").hide();
        $(".changePassword").show();
    });

    $(".changePassword-admin").click(function(){
        $(".list").hide();
        $(".changePassword-admin").show();
    });

    $(".add-more-info").click(function(){
        $(".list").hide();
        $(".addMoreInfo").show();
    });

    $(".addStudent-b").click(function(){
        $(".list").hide();
        $(".addStudent").show();
    });

    $(".logout").click(function () {
        interceptor();
        $.ajax({
            url:"/admin/logout",
            type:"get",
            dataTyper:"text",
            success:function (data) {
                if(data=="true"){
                    window.location.href="login.html";
                }
            }
        })
    });

    $(".cencelStudent").click(function () {
        $("#studentInfoUpdate").hide();
    });

    $(".selectOneStu").click(function () {
        interceptor();
        $.ajax({
            url:"/admin/selectOneStudent",
            type:"post",
            dataTyper:"text",
            data:{
                studentName:$(".select-one-student").val()
            },
            success:function (data) {
                $("#listStudent").empty();
                $('.first-page2').attr("disabled",true);
                $(".next-page2").attr("disabled",true);
                $(".last-page2").attr("disabled",true);
                $(".trailer-page2").attr("disabled",true);
                $('.first-page1').attr("disabled",true);
                $(".next-page1").attr("disabled",true);
                $(".last-page1").attr("disabled",true);
                $(".trailer-page1").attr("disabled",true);
                $("#pageNum2").text("第1/1页");
                $.each(data,function(index,obj){
                    var tr=$("<tr>")
                        .append($("<td>").text(obj.studentid))
                        .append($("<td>").text(obj.studentname))
                        .append($("<td>").text(obj.departmentName))
                        .append($("<td>").text(obj.phone))
                        .append($("<td>").text(obj.teacherName))
                        .append($("<td>").text(obj.status))
                         .append($("<td>").html('<a href="javascript:void(0)" style="margin-right: 15px;" onclick="updateStudent(this)">修改</a><a href="javascript:void(0)" style="margin-right: 15px;" onclick="cancleAffirm(this)">取消选课</a><a href="javascript:void(0)" onclick="deleteStudent(this)">删除</a>'));
                    $("#listStudent").append(tr);
                });
            }
        })
    });

    $(".selectByTea").click(function () {
        interceptor();
        $.ajax({
            url:"/admin/selectByTea",
            type:"post",
            dataTyper:"text",
            data:{
                teacherName:$(".select-by-teacher").val()
            },
            success:function (data) {
                $("#listStudent").empty();
                $('.first-page2').attr("disabled",true);
                $(".next-page2").attr("disabled",true);
                $(".last-page2").attr("disabled",true);
                $(".trailer-page2").attr("disabled",true);
                $('.first-page1').attr("disabled",true);
                $(".next-page1").attr("disabled",true);
                $(".last-page1").attr("disabled",true);
                $(".trailer-page1").attr("disabled",true);
                $("#pageNum2").text("第1/1页");
                $.each(data,function(index,obj){
                    var tr=$("<tr>")
                        .append($("<td>").text(obj.studentid))
                        .append($("<td>").text(obj.studentname))
                        .append($("<td>").text(obj.departmentName))
                        .append($("<td>").text(obj.phone))
                        .append($("<td>").text(obj.teacherName))
                        .append($("<td>").text(obj.status))
                        .append($("<td>").html('<a href="javascript:void(0)" style="margin-right: 15px;" onclick="updateStudent(this)">修改</a><a href="javascript:void(0)" style="margin-right: 15px;" onclick="cancleAffirm(this)">取消选课</a><a href="javascript:void(0)" onclick="deleteStudent(this)">删除</a>\''));
                    $("#listStudent").append(tr);
                });
            }
        })
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
                url:"/admin/changepasswd",
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
});

function updateTeacher(obj){
    $("#az").show();
    var StudentNum=($(obj).parent().parent().find("td").eq(5).text());
    var n=StudentNum.split("/")[1];
    upTeacherId=$(obj).parent().parent().find("td").eq(0).text();
    $(".upTeacherName").val($(obj).parent().parent().find("td").eq(1).text());
    $(".upStudentNum").val(n);
    $(".upTitle").val($(obj).parent().parent().find("td").eq(3).text());
    $(".upDepartment").val($(obj).parent().parent().find("td").eq(2).text());
    $(".upContent").val($(obj).parent().parent().find("td").eq(4).text());
}

function deleteTeacher(obj){
    teacherId=$(obj).parent().parent().find("td").eq(0).text();
    layer.confirm('是否确定删除？', {
        btn: ['确定','取消'] //按钮
    }, function(){
        interceptor();
        $.ajax({
            url:"/admin/deleteTeacher",
            type:"post",
            dataType:"text",
            data:{
                "teacherId":teacherId
            },
            success:function (data) {
                newhint(data);
                setTimeout(function(){
                    $(".viewTeacher-b").trigger("click");
                },500)
            }
        })
    }, function(){
    });
}
function cancleConfirm(obj){
    var cancleIdId=$(obj).parent().parent().find("td").eq(0).text();

    layer.confirm('是否确认取消？', {
        btn: ['确定','取消'] //按钮
    }, function(){
        interceptor();
        $.ajax({
            url:"/admin/cancleConfirm",
            type:"post",
            dataType:"text",
            data:{
                "teacherId":cancleIdId
            },
            success:function (data) {
                newhint(data);
                setTimeout(function(){
                    $(".viewTeacher-b").trigger("click");
                },800)

            }
        })
    }, function(){
    });


}

function downloadExcelFile() {
    window.location.href = "/admin/download";
}

function downloadTeacherExcelFile(){
    window.location.href = "/admin/downloadTeacher";
}

function downloadStudentTemplate(){
    window.location.href = "/admin/downloadStudentTemplate";
}

function downloadTeacherTemplate(){
    window.location.href = "/admin/downloadTeacherTemplate";
}

$(function () {
    $(".submit-file").click(function () {
        $(".bg").fadeIn(200);
        $(".loadingImage").fadeIn(200);
    })
});


function cancleAffirm(obj){
    layer.confirm('是否确认取消该学生的选课情况？', {
        btn: ['确定','取消'] //按钮
    }, function(){
        interceptor();
        $.ajax({
            url:"/admin/cancleAffirmStu",
            type:"post",
            dataType:"text",
            data:{
                "studentId":$(obj).parent().parent().find("td").eq(0).text()
            },
            success:function (data) {
                $(".selectStu").click();
                newhint(data);
            }
        })
    }, function(){
    });
}

function updateStudent(obj) {
    $("#studentInfoUpdate").show();
    old_studentId=$(obj).parent().parent().find("td").eq(0).text();
    $(".upStudentId").val(old_studentId);
    $(".upStudentName").val($(obj).parent().parent().find("td").eq(1).text());
}


$(function(){
    $("#updateStudent").click(function(){
        $("#studentInfoUpdate").hide();
        interceptor();
        $.ajax({
            url:"/admin/updateStudent",
            type:"post",
            dataType:"text",
            data:{
                "old_studentId":old_studentId,
                "new_studentId":$(".upStudentId").val(),
                "new_studentName":$(".upStudentName").val()
            },
            success:function (data) {
                $(".selectStu").click();
                newhint(data);
            }
        })
    });

    $(".list-button-stu").click(function(){
        interceptor();
        $.ajax({
            url:"/admin/addStudentInform",
            type:"post",
            dataType:"text",
            data:{
                "content":$(".student-inform").val()
            },
            success:function (data) {
                newhint(data);
            }
        })
    });

    $(".list-button-tea").click(function(){
        interceptor();
        $.ajax({
            url:"/admin/addTeacherInform",
            type:"post",
            dataType:"text",
            data:{
                "content":$(".teacher-inform").val()
            },
            success:function (data) {
                newhint(data);
            }
        })
    });

});


function interceptor() {
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


function queryDepartmentid(departmentName){
    if(departmentName=="信息与计算科学"){
        return "07001";
    }
    else if(departmentName=="应用统计学"){
        return "07002";
    }
    else if(departmentName=="数学与应用数学"){
        return "07003";
    }
}

function queryStatus(Status) {
    if(Status=="未确认"){
        return "false";
    }
    else if(Status=="已确认"){
        return "true";
    }
}

//初始化
$(function(){
    $.ajax({
        url:'/admin/showInfo',
        type:'get',
        dataType:'json',
        success:function (data) {
            $(".student-inform").attr("placeholder","当前学生主页内容："+data.student);
            $(".teacher-inform").attr("placeholder","当前教师主页内容："+data.teacher);
       }
    });

});


function deleteStudent(obj) {
    layer.confirm('是否确定删除？', {
        btn: ['确定','取消'] //按钮
    }, function(){
        interceptor();
        $.ajax({
            url:'/admin/deleteStudent',
            type:'get',
            dataType:'text',
            data:{
                studentId:$(obj).parent().parent().find("td").eq(0).text()
            },
            success:function (data) {
                newhint(data);
                $(obj).parent().parent().remove();
            }
        })
    }, function(){
    });
}

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

}

//清空输入框
$(function () {
    $("#reset").click(function () {
        $(".oldPassword").val("");
        $(".newPassword").val("");
        $(".confirmPassword").val("");
    });
    $(".rest-btn-teacher").click(function () {
        $(".teacher-inform").val("");
    });
    $(".rest-btn-student").click(function () {
        $(".student-inform").val("");
    });

    $("#resetTea").click(function(){
        $(".teacherId").val("");
        $(".teacherName").val("");
        $(".studentNum").val("");
        $(".title").val("");
        $(".departmentid").val("");
        $(".content").val("");
        $(".teacherPhone").val("");
    });

    $("#resetStudent").click(function () {
        $(".studentId").val("");
        $(".studentName").val("");
        $(".stu-phone").val("");
        $(".studepartmentid").val("");
    });
});


function resetPassword() {
    layer.confirm('是否重置？', {
        btn: ['确定','取消'] //按钮
    }, function(){
        var id=$(".ids").val();
        interceptor();
        $.ajax({
            url:"/admin/resetpassword",
            type:"post",
            dataType:"text",
            async:"false",
            data:{
                "role":$("#role").find("option:selected").text(),
                "id":id
            },
            success:function (data) {
                layer.msg(data, {icon: 1});
            }
        });
    }, function(){
    });
}

function newhint(message){
    var layer = layui.layer;
    layer.msg(message);
}


$(function () {
    layui.use('form', function () {
        var form = layui.form; //只有执行了这一步，部分表单元素才会自动修饰成功
        form.render();
    });
});



