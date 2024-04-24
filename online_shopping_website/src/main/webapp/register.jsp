<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="../CSS/userpage-style.css">
<%@include file="../Includes/head.jsp"%>
<meta charset="ISO-8859-1">
<title>Register</title>
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

	<a href="../user/login" class="btn btn-success"
			style="position: absolute; top: 20px; right: 20px;">Go to Login</a>
	<div
		class="container d-flex justify-content-center align-items-center h-100">
		<div class="card w-50">
			<div class="card-header text-center">For New Register</div>
			<div class="card-body">
				<form action="../user/register" method="post">
					<div class="form-group">
						<label>Enter Name : </label> <input type="text" name="name"
							class="form-control" required>
					</div>
					<div class="form-group">
						<label>Enter Email : </label> <input type="email" name="email"
							placeholder="example@gmail.com" class="form-control" required>
					</div>
					<div class="form-group">
						<label>Enter Password : </label> <input type="password" pattern="^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{4,}$"
						title="Password must contain at least 4 characters including 1 uppercase letter, 1 lowercase letter, 1 digit, and 1 special character."	name="password" class="form-control" required>
					</div>
					<div class="form-group">
						<label>Enter Phone Number : </label> <input pattern="[0-9]{10}"
							name="phoneNumber" placeholder="10-digit number" title="Please enter a 10-digit number"
							class="form-control" required>
					</div>
					<div class="text-center">
						<button type="submit" class="btn btn-primary">Submit</button>
					</div>
				</form>
			</div>
		</div>
	</div>

</body>
</html>