<%--
  Created by IntelliJ IDEA.
  User: chuguangming
  Date: 16/8/22
  Time: 下午3:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>登录</title>
</head>
<body>
<c:if test="${!empty error}">
    <font color="red"><c:out value="${error}"></c:out></font>
</c:if>
<form action="<c:url value='loginCheck.html'/>" method="post">

    username:<input type="text" name="userName">
    <br/>
    password:<input type="text" name="passWord">
    <br>
    <input type="submit" value="login"/>
    <input type="reset" value="reset"/>
</form>
${user.userName},欢迎进入我们的Spring MVC
</body>
</html>
