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
            <div>${item}</div>
        </c:forEach>
    </c:otherwise>
</c:choose><br/><br/>

<form>
    <input type="button" value="Back" onclick="history.back()">
</form>
</body>
</html>
