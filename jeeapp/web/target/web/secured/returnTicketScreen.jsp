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
        No tickets owned.
    </c:when>
    <c:otherwise>
        <c:forEach var="item" items="${tickets}">
            <div>${item}</div>
        </c:forEach>
        Insert the desired ticket ID:
        <form action="returnTicket" method="get">
            <input name="TicketId" type="text" required/>
            <input type="submit" value="Return"/>
        </form>
    </c:otherwise>
</c:choose>
</body>
<footer>
    <form action="logout" method="post">
        <input type="submit" value="Logout">
    </form>
</footer>
</html>
