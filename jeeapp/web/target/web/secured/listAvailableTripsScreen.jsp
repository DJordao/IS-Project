<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>List Available Trips</title>
</head>
<body>
Insert a date interval:
<form action="listAvailableTrips" method="get">
    <input name="DateStart" type="date" required/>
    -
    <input name="DateEnd" type="date" required/>
    <input type="submit" value="Confirmar">
</form>
<c:choose>
    <c:when test="${trips == null}">
    </c:when>
    <c:when test="${trips.isEmpty()}">
        No trips for the given date interval.
    </c:when>
    <c:otherwise>
        <c:forEach var="item" items="${trips}">
            <div>${item}</div>
        </c:forEach>
    </c:otherwise>
</c:choose>
</body>
<footer>
    <form action="logout" method="post">
        <input type="submit" value="Logout">
    </form>
</footer>
</html>
