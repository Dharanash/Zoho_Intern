<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="online_shopping_website.model.*"%>
<%@page import="online_shopping_website.servlets.*"%>
<%@page import="java.util.*"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="../CSS/userpage-style.css">
<%@include file="../Includes/head.jsp"%>
<meta charset="ISO-8859-1">
<title>Profile</title>
</head>
<body>
<%@include file="../Includes/navbar.jsp"%>
	<%
	User curUser = (User) request.getSession().getAttribute("auth");
	if(curUser==null){
	response.sendRedirect("./login.jsp");}
	if (request.getParameter("profileSuccessMessage") != null) {
	%>
	<div id="messageBox">
		<p class="success"><%=request.getParameter("profileSuccessMessage")%></p>
	</div>
	<%
	}
	%>


	<div
		class="container d-flex justify-content-center align-items-center h-100">
		<div class="card w-50">
			<div class="card-header text-center">For New Register</div>
			<div class="card-body">
				<form action="../UserServlet/updateProfile" method="post">
				<input type="hidden" name="id" value="${curUser.getUserId}" >
					<div class="form-group">
						<label>Enter Name : </label> <input type="text" name="name"
							class="form-control" value="<%=curUser.getName()%>">
					</div>
					<div class="form-group">
						<label>Enter Email : </label> <input type="email" name="email"
							 class="form-control" value="<%=curUser.getEmail()%>" disabled>
					</div>
					<div class="form-group">
						<label>Enter Password : </label> <input type="password"
							name="password" class="form-control" value="<%=curUser.getPassword()%>" required>
					</div>
					<div class="form-group">
						<label>Enter Phone Number : </label> <input pattern="[0-9]{10}"
							name="phoneNumber"
							class="form-control" value="<%=curUser.getPhoneNumber()%>" required>
					</div>
					<div class="text-center">
						<button type="submit" class="btn btn-primary">Update</button>
					</div>
				</form>
			</div>
		</div>
	</div>

</body>
</html>