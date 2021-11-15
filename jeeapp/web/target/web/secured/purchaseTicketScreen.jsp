<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Purchase Ticket</title>
</head>
<body>
Insert your desired departure and destination:
<form action="searchTrips" method="get">
    <input name="Departure" type="text" required/>
    to
    <input name="Destination" type="text" required/>
    <input type="submit" value="Confirmar">
</form>

<c:choose>
    <c:when test="${cache == null}">
    </c:when>
    <c:when test="${cache.isEmpty()}">
        No available trips for the given locations.
    </c:when>
    <c:otherwise>
        <c:forEach var="item" items="${cache}">
            <div>
                <b>ID: </b>${item.getId()}<br>
                <b>Departure: </b>${item.getLocalPartida()}<br>
                <b>Departure date: </b>${item.getHoraPartida()}<br>
                <b>Destination: </b>${item.getDestino()}<br>
                <b>Destination date: </b>${item.getHoraChegada()}<br>
            </div><br/><br/>
        </c:forEach>
        Insert the desired trip ID:
        <form action="purchaseTicket" method="post">
            <input name="BusTripId" type="text" required/>
            <input type="submit" value="Purchase"/>
        </form>
    </c:otherwise>
</c:choose>
</body>
<footer>
    <br>
    <a href="/web/secured/display.jsp"> Back </a>
    <br>
    <br>
    <form action="userLogout" method="post">
        <input type="submit" value="Logout">
    </form>
</footer>
</html>
