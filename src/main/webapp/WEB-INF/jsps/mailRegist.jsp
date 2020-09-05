<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: 韦延伦工作用
  Date: 2020/8/29
  Time: 19:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set value="${pageContext.request.contextPath}" var="path" scope="page"/>
<html>
<head>
    <meta charset="UTF-8">
    <script>
        $(function(){
            //获取验证码
            $("#code").click(function() {
                var mail = $("input[name='mail']").val();
                $.ajax({
                    url: "/mail/code",
                    async:true,
                    data:{
                        mail:mail
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
<c:set value="${pageContext.request.contextPath}" var="path" scope="page"/>
<h1 align="center">邮箱注册</h1>
<div align="center">
<form action="${path}/mail/regist" method="post">
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
            <td>  <input type="password"  name="pwd" id="pwd1" placeholder="请再次填写密码" onkeyup="validate()"><span id="tishi"></span></td>
        </tr>
        <tr>
            <td>邮箱</td>
            <td><input name="mail" id="mail" type="text" value=""></td>
            <td><span></span></td>
        </tr>
        <tr>
            <td>验证码</td>
            <td><input type="text" name="code"></td>
            <td><input type="button"  name="" id="code" value="发送"></td>
            <td><span>${msgCode}</span></td>
        </tr>
        <tr>
            <td><input type="submit" id="button" value="提交"></td>
        </tr>
    </table>
</form>
<a href="${path}/phone/registpage" style="color: #5cb85c">点我使用手机注册</a>
</div>
</body>
</html>
