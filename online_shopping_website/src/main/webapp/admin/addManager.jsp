<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="../CSS/userpage-style.css">
<%@include file="../Includes/head.jsp"%>

<meta charset="ISO-8859-1">
<title>Add Manager</title>
</head>
<body>
	<%@include file="../Includes/navbar.jsp"%>
	<%
	if (request.getParameter("addManagerErrorMessage") != null) {
	%>
	<div id="messageBox">
		<p class="error"><%=request.getParameter("addManagerErrorMessage")%></p>
	</div>
	<%
	}
	else if(request.getParameter("addManagerSuccessMessage") != null){
	%>
	<div id="messageBox">
		<p class="success"><%=request.getParameter("addManagerSuccessMessage")%></p>
	</div>
	<%
	}
	%>
	
	<div
		class="container d-flex justify-content-center align-items-center h-100">
		<div class="card w-50">
			<div class="card-header text-center">Add Manager</div>
			<div class="card-body">
				<form action="../AddManagerServlet" method="post">
					<div class="form-group">
						<label>Enter Name : </label> <input type="text" name="name"
							class="form-control" required>
					</div>
					<div class="form-group">
						<label>Enter Email : </label> <input type="email" name="email"
							placeholder="example@gmail.com" class="form-control" required>
					</div>
					<div class="form-group">
						<label>Enter Password : </label> <input type="password"
							name="password" class="form-control" required>
					</div>
					<div class="form-group">
						<label>Enter Phone Number : </label> <input pattern="[0-9]{10}"
							name="phoneNumber" placeholder="10-digit number"
							class="form-control" title="Please enter a 10-digit number" required>
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