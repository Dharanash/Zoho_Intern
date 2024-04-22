<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="online_shopping_website.model.*"%>
<%@page import="online_shopping_website.servlets.*"%>
<%
User curUser = (User) request.getSession().getAttribute("auth");
%>

<!DOCTYPE html>
<html>
<head>

<%@include file="/Includes/head.jsp"%>
<meta charset="ISO-8859-1">
<title>Home</title>
</head>
<body>
<%@include file="../Includes/navbar.jsp" %>
<br><br>
<div class="container d-flex justify-content-center align-items-center h-100">
<h2>Welcome <%=curUser.getName()%></h2></div>
</body>
</html>