<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Top Passengers</title>
</head>
<body>


<c:choose>
    <c:when test="${topPassengers == null}">
    </c:when>
    <c:when test="${topPassengers.isEmpty()}">
        No Passengers available.
    </c:when>
    <c:otherwise>
        <strong> Top Passengers</strong>
        <c:forEach var="item" items="${topPassengers}">
            <div>${item.key} with ${item.value} trips</div>
        </c:forEach> <br/>
    </c:otherwise>
</c:choose><br/><br/>

</body>
<footer>
    <br>
    <a href="/web/secured/display.jsp"> Back </a>
    <br>
    <br>
</footer>
</html>
