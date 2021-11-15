<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Bus - Company</title>
</head>
<body>

<nav>
<c:choose>
    <c:when test="${type == 'Passenger'}">
        <strong>Welcome ${name} !</strong></br>
    <ul>
        <li> <a href="/web/secured/editUserInfoScreen.html"> Edit user information </a></li>
        <li> <a href="/web/secured/chargeWalletScreen.html"> Charge my wallet </a></li>
        <li> <a href="/web/secured/listAvailableTripsScreen.jsp"> List available trips </a></li>
        <li> <a href="/web/secured/purchaseTicketScreen.jsp"> Purchase tickets </a></li>
        <li> <a href="/web/secured/searchTickets"> Return tickets </a></li>
        <li> <a href="/web/secured/listUserTripsScreen.jsp"> My trips </a></li>
        <form action="deleteProfile" method="post">
            <input type="submit" value="Delete profile">
        </form>
        <form action="userLogout" method="post">
            <input type="submit" value="Logout">
        </form>
    </ul>
    </c:when>
     <c:otherwise>
        <strong>Welcome Manager ${name} !</strong></br>
        <ul>
            <li> <a href="/web/secured/createBusTrips.html"> Create Bus Trips </a></li>
            <li> <a href="/web/secured/deleteFutureTrips.jsp"> Delete Bus Trips </a></li>
            <li> <a href="/web/secured/listTopPassengers"> List Top Passengers </a></li>
            <li> <a href="/web/secured/searchBusTrips.jsp"> Search Bus Trips </a></li>
            <li> <a href="/web/secured/detailedBusTrip.jsp"> Search Detailed Bus Trip </a></li>
        </ul>
         <form action="adminLogout" method="post">
             <input type="submit" value="Logout">
         </form>
     </c:otherwise>
</c:choose>
</nav>
</body>
</html>



