<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
    <title>Title</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="">
    <meta name="viewport" content="width=device -width,initial-scale=1">
    <title>管理员页面</title>
    <link rel="stylesheet" type="text/css" href="../css/admin.css">
    <link rel="stylesheet" href="../css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="../js/jquery-3.3.1.js"></script>
    <script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>
    <script src="https://cdn.staticfile.org/jquery/2.1.1/jquery.min.js"></script>
    <script src="../js/bootstrap.min.js"></script>
    <script src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="../js/admin.js"> </script>
    <script type="text/javascript" src="../resources/layui.all.js"></script>
    <script type="text/javascript" src="../resources/layui.js"></script>
    <script>
        $(function () {
            $("#logout").click(function () {
                window.history.go(-1);
            })
        })
    </script>
</head>
<body>
<div class="container" style="margin:auto;width: 95%">
    <div class="page-header text-center ">
        <h4 class="co-p"><strong>毕业设计师生互选系统</strong></h4>
    </div>
    <nav class="navbar navbar-inverse">
        <div class="container-fluid"  style="background-color: #4B627A;">
            <div class="navbar-header">
                <button class="navbar-toggle" data-toggle="collapse" data-target="#slider-left">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a href="#" class="navbar-brand" style="color: rgb(255, 255, 255)">学生登陆页面</a>
            </div>
            <ul class="nav navbar-nav navbar-right nav-ul-right">
                <li class="col-xs-6"><a href="javascript:void(0)" style="color: white" id="logout">返回上层</a></li>
            </ul>
            <div class="navbar-default navbar-collapse collapse" id="slider-left">
                <ul class="nav">
                    <li class="index-button">
                        <a href="#sub1" data-toggle="collapse">消息提示页
                            <span class="glyphicon glyphicon-menu-right pull-right"></span>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
    <div class="center">

        <!-- ---------------主页 ----------------------->
        <div class="row list index-page"  >
            <div class="col-md-12">
                <div class="panel panel-default" style="padding-bottom: 20px;">
                    <div class="panel-heading">关于毕业设计师生互选的通知</div>
                    <div class="panel-body">
                        <c:forEach items="${list}" var="item" varStatus="vs">
                            <p>${item}</p>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
