<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>List Trips</title>
</head>
<body>
Your trips:
<c:choose>
    <c:when test="${bustrips == null}">
    </c:when>
    <c:when test="${bustrips.isEmpty()}">
        You don't have trips scheduled.
    </c:when>
    <c:otherwise>
        <c:forEach var="item" items="${bustrips}">
            <div>${item}</div>
        </c:forEach>
    </c:otherwise>
</c:choose>
</body>
</html>

