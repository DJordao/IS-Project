<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
</head>
<body>

<nav>
<c:choose>
    <c:when test="${type == 'Passenger'}">
        <strong>Welcome ${name} !</strong></br>
    <ul>
        <li> <a href="secured/editUserInfoScreen.html"> Edit user information </a></li>
        <li> <a href="/web/secured/chargeWalletScreen.html"> Charge my wallet </a></li>
        <li> <a href="secured/listAvailableTripsScreen.jsp"> List available trips </a></li>
        <li> <a href="secured/purchaseTicketScreen.jsp"> Purchase tickets  - NOT DONE</a></li>
        <li> <a href="secured/returnTicketScreen.jsp"> Return tickets  - NOT DONE</a></li>
        <li> <a href="secured/listUserTripsScreen.jsp"> My trips  - NOT DONE</a></li>
        <li> <a href="listAvailableTrips.jsp"> Delete profile  - NOT DONE</a></li><br/>
        <li> <a href="listAvailableTrips.jsp"> Logout  - NOT DONE</a></li>
    </ul>
    </c:when>
     <c:otherwise>
        <strong>Welcome Manager ${name} !</strong></br>
        <ul>
            <li> <a href="secured/createBusTrips.html"> Create Bus Trips </a></li>
            <li> <a href="secured/editUserInfoScreen.html"> Delete Bus Trips </a></li>
            <li> <a href="secured/editUserInfoScreen.html"> List Top 5 Passengers </a></li>
            <li> <a href="secured/editUserInfoScreen.html"> Search Bus Trips </a></li>
            <li> <a href="secured/editUserInfoScreen.html"> Search Detailed Bus Trips </a></li>
            <li> <a href="secured/editUserInfoScreen.html"> List Passengers on a Trip </a></li>

        </ul>

     </c:otherwise>
</c:choose>
</nav>
</body>
</html>



