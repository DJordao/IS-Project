<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Search Bus Trips</title>
</head>
<body>
Insert a date to search trips:
<form action="detailedBusTrips" method="get">
    <label> Start time: </label>
    <input type="date" id="start-time" name="start-time" required><br/><br/>
    <input type="submit" value="Search">
</form>
<c:choose>
    <c:when test="${detailedTrip == null}">
    </c:when>
    <c:when test="${detailedTrip.isEmpty()}">
        No trips for the given date interval.
    </c:when>
    <c:otherwise>
        <c:forEach var="item" items="${detailedTrip}">
            <div>
                <b>ID: </b>${item.getId()}<br>
                <b>Local de Partida: </b>${item.getLocalPartida()}<br>
                <b>Hora de Partida: </b>${item.getHoraPartida()}<br>
                <b>Local de Chegada: </b>${item.getDestino()}<br>
                <b>Hora de Chegada: </b>${item.getHoraChegada()}<br>
            </div><br/><br/>
        </c:forEach>
        Insert the desired trip ID to list all the passengers in it:
        <form action="listallPassengersdetail" method="get">
            <input name="BusTripId" type="text" required/>
            <input type="submit" value="List Passengers"/>
        </form>
        <c:choose>
            <c:when test="${usersTrip == null}">
            </c:when>
            <c:when test="${usersTrip.isEmpty()}">
                You don't have users in this trip.
            </c:when>
            <c:otherwise>
                <c:forEach var="item" items="${usersTrip}">
                    <div>
                        <b>Name: </b> ${item.getNome()}
                        <b>Email: </b>${item.getEmail()}
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
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
