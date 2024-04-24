<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="../CSS/userpage-style.css">
<meta charset="ISO-8859-1">
<title>Login</title>
<%@include file="../Includes/head.jsp"%>
</head>
<body>
	<%
	if (request.getParameter("errorMessage") != null) {
	%>
	<div id="messageBox">
		<p class="error"><%=request.getParameter("errorMessage")%></p>
	</div>
	<%
	}
	else if(request.getParameter("successMessage") != null){
	%>
	<div id="messageBox">
		<p class="success"><%=request.getParameter("successMessage")%></p>
	</div>
	<%
	}
	%>

		<a href="../user/register" class="btn btn-success"
			style="position: absolute; top: 20px; right: 20px;">Go to Register</a>
		<div
			class="container d-flex justify-content-center align-items-center h-100">
			<div class="card w-50">
				<div class="card-header text-center">User Login</div>
				<div class="card-body">
					<form action="../user/login" method="post">
						<div class="form-group">
							<label>Email address</label> <input type="email" name="email"
								class="form-control" placeholder="Enter email">
						</div>
						<div class="form-group">
							<label>Password</label> <input type="password" name="password"
								class="form-control" placeholder="Enter Password">
						</div>
						<div class="text-center">
							<button type="submit" class="btn btn-primary">Login</button>
						</div>
					</form>
				</div>
			</div>
		</div>


</body>
</html>