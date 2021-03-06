<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Return Ticket</title>
</head>
<body>
Your tickets:
<c:choose>
    <c:when test="${tickets == null}">
    </c:when>
    <c:when test="${tickets.isEmpty()}">
        No refundable tickets.
    </c:when>
    <c:otherwise>
        <c:forEach var="item" items="${tickets}">
            <div>
                <b>Ticket ID: </b>${item.getId()}<br>
                <b>Departure: </b>${item.getViagem().getLocalPartida()}<br>
                <b>Departure date: </b>${item.getViagem().getHoraPartida()}<br>
                <b>Destination: </b>${item.getViagem().getDestino()}<br>
                <b>Destination date: </b>${item.getViagem().getHoraChegada()}<br>
            </div><br/><br/>
        </c:forEach>
        Insert the desired ticket ID:
        <form action="returnTicket" method="post">
            <input name="TicketId" type="text" required/>
            <input type="submit" value="Return"/>
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
