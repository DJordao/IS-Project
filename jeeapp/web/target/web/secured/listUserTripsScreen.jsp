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
            <div>
                <b>ID: </b>${item.getId()}<br>
                <b>Departure: </b>${item.getLocalPartida()}<br>
                <b>Departure date: </b>${item.getHoraPartida()}<br>
                <b>Destination: </b>${item.getDestino()}<br>
                <b>Destination date: </b>${item.getHoraChegada()}<br>
            </div><br/><br/>
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

