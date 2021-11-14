<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Delete Future Bus Trips</title></br></br>
</head>
<body>


<c:choose>
    <c:when test="${futureTrips == null}">
    </c:when>
    <c:when test="${futureTrips.isEmpty()}">
        No future trips available.
    </c:when>
    <c:otherwise>
        <strong> Future Bus Trips</strong>
        <c:forEach var="item" items="${futureTrips}">
            <div>
                 <b>ID: </b>${item.getId()}<br>
                 <b>Local de Partida: </b>${item.getLocalPartida()}<br>
                 <b>Hora de Partida: </b>${item.getHoraPartida()}<br>
                 <b>Local de Chegada: </b>${item.getDestino()}<br>
                 <b>Hora de Chegada: </b>${item.getHoraChegada()}<br>
            </div><br><br>
        </c:forEach> <br/>

        Insert the desired trip ID to remove it:
        <form action="removeBusTrip" method="get">
            <input name="BusTripId" type="text" required/>
            <input type="submit" value="Remove"/>
        </form>
    </c:otherwise>
</c:choose>
</body>
</html>
