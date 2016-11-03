<%--
  Created by IntelliJ IDEA.
  User: chuguangming
  Date: 16/9/8
  Time: 上午10:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Title</title>
</head>
<body>
<c:if test="${param.name=='momor' && param.password=='1234'}">
    <h1>${param.name}登录成功</h1>
</c:if>
</body>
</html>
