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
    ->
    <input name="Destination" type="text" required/>
    <input type="submit" value="Confirmar">
</form>

<c:choose>
    <c:when test="${trips == null}">
    </c:when>
    <c:when test="${trips.isEmpty()}">
        No available trips for the given locations.
    </c:when>
    <c:otherwise>
        <c:forEach var="item" items="${trips}">
            <div>${item}</div>
        </c:forEach>
        Insert the desired trip ID:
        <form action="purchaseTicket" method="get">
            <input name="BusTripId" type="text" required/>
            <input type="submit" value="Purchase"/>
        </form>
    </c:otherwise>
</c:choose>
</body>
</html>
