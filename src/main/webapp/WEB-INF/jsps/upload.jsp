<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: 韦延伦工作用
  Date: 2020/9/4
  Time: 13:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<c:set value="${pageContext.request.contextPath}" var="path" scope="page"/>
<div align="center">
    <img src=${data}>
    <form action="${path}/u/img" method="post" enctype="multipart/form-data">
        <input type="file" name="file">
        <br>
        用户:<input type="text" name="username">
        <br>
        <div align="center"><input type="submit" value="点击上传"></div>
        <br>
    </form>
</div>
</body>
</html>
