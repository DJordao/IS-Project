<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Search Bus Trips</title>
</head>
<body>
Insert a date interval:
<form action="searchBusTrips" method="get">
    <label> Start time: </label>
    <input type="datetime-local" id="start-time" name="start-time" required><br/><br/>

    <label> End time: </label>
    <input type="datetime-local" id="end-time" name="end-time" required><br/><br/>
    <input type="submit" value="Search">
</form>
<c:choose>
    <c:when test="${allTrips == null}">
    </c:when>
    <c:when test="${allTrips.isEmpty()}">
        No trips for the given date interval.
    </c:when>
    <c:otherwise>
        <c:forEach var="item" items="${allTrips}">
            <div>
                <b>ID: </b>${item.getId()}<br>
                <b>Departure: </b>${item.getLocalPartida()}<br>
                <b>Departure date: </b>${item.getHoraPartida()}<br>
                <b>Destination: </b>${item.getDestino()}<br>
                <b>Destination date: </b>${item.getHoraChegada()}<br>
            </div><br/><br/>
        </c:forEach>
        Insert the desired trip ID to list all the passengers in it:
        <form action="listallPassengers" method="get">
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

<form>
    <input type="button" value="Back" onclick="history.back()">
</form>
</body>
</html>
