<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: 韦延伦工作用
  Date: 2020/8/11
  Time: 21:48
  To change this template use File | Settings | File Templates.
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<c:set value="${pageContext.request.contextPath}" var="path" scope="page"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <script>
        $(function(){
            //获取验证码
            $("#code").click(function() {
                var phone = $("input[name='phone']").val();
                $.ajax({
                    url: "/phone/code",
                    async:true,
                    data:{
                        phone:phone
                    },
                    method:'POST',
                    type:'post',
                    dataType:'json',
                    success:function (data) {
                        alert(data["msg"]);
                    }
                });
            });
        })
        function validate() {
            var pwd1 = document.getElementById("pwd").value;
            var pwd2 = document.getElementById("pwd1").value;

            <!-- 对比两次输入的密码 -->
            if(pwd1 == pwd2)
            {
                document.getElementById("tishi").innerHTML="<font color='green'>两次密码相同</font>";
                document.getElementById("button").disabled = false;
            }
            else {
                document.getElementById("tishi").innerHTML="<font color='red'>两次密码不相同</font>";
                document.getElementById("button").disabled = true;
            }
        }


    </script>
</head>
<body>
<h1 align="center">手机注册</h1>
<div align="center">
    <form action="${path}/phone/regist" method="post">
        <table>
            <tr>
                <td>用户名</td>
                <td><input type="text" name="username"></td>
                <td><span>${msg}</span></td>
            </tr>
            <tr>
                <td>密码</td>
                <td> <input type="password"  name="pwd" id="pwd" placeholder="请设置登录密码"></td>
            </tr>
            <tr>
                <td>确认密码</td>
                <td><input type="password"  name="pwd1" id="pwd1" placeholder="请再次填写密码" onkeyup="validate()"><span id="tishi"></span></td>
            </tr>
            <tr>
                <td>手机号</td>
                <td><input type="text" name="phone"></td>
                <td><span></span></td>
            </tr>
            <tr>
                <td>验证码</td>
                <td><input type="text" name="code"></td>
                <td><span>${msgCode}</span></td>
                <td><input type="button" name="" id="code" value="发送" ></td>
            </tr>
            <tr>
                <td><input type="submit" id="button" value="提交"></td>
            </tr>
        </table>
    </form>
<a href="${path}/mail/registpage" style="color: #5cb85c">点我使用邮箱注册</a>
</div>
</body>
</html>