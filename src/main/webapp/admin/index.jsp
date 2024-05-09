<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="com.expensecalculator.dto.*"%>
<%@page import="java.util.*"%>

<!DOCTYPE html>
<html>
<head>
<%@include file="../includes/head.jsp"%>
<meta charset="ISO-8859-1">
<title>Home</title>
</head>
<body>
	<%@include file="../includes/navbar.jsp"%>
	<br>
	<br>
	<div
		class="container d-flex justify-content-center align-items-center h-100">
		<h2 id="userMsg"></h2>
	</div>
	
	<script type="text/javascript">
		var userName = sessionStorage.getItem('userName');
		var userId = sessionStorage.getItem('userId');
		document.getElementById('userMsg').textContent = 'Welcome ' + userName;
		
	</script>
</body>
</html>