<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>List Available Trips</title>
</head>
<body>
Insert a date interval:
<form action="listAvailableTrips" method="get">
    <input name="DateStart" type="datetime-local" required/>
    -
    <input name="DateEnd" type="datetime-local" required/>
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
