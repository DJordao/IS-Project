<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
</head>
<body>
<strong>Welcome ${auth} !</strong>

<nav>
    <ul>
        <li> <a href="secured/editUserInfoScreen.html"> Edit user information </a></li>
        <li> <a href="secured/chargeWalletScreen.html"> Charge my wallet </a></li>
        <li> <a href="secured/listAvailableTripsScreen.jsp"> List available trips </a></li>
        <li> <a href="secured/purchaseTicketScreen.jsp"> Purchase tickets  - NOT DONE</a></li>
        <li> <a href="listAvailableTrips.jsp"> Return tickets  - NOT DONE</a></li>
        <li> <a href="listAvailableTrips.jsp"> My trips  - NOT DONE</a></li>
        <li> <a href="listAvailableTrips.jsp"> Delete profile  - NOT DONE</a></li><br/>
        <li> <a href="listAvailableTrips.jsp"> Logout  - NOT DONE</a></li>

    </ul>
</nav>
</body>
</html>



