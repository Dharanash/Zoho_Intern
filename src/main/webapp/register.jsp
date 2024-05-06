<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="../css/userpage-style.css">
<%@include file="../includes/head.jsp"%>
<meta charset="ISO-8859-1">
<title>Register</title>
</head>
<body>

	<%
	if (request.getAttribute("errorMessage") != null) {
	%>
	<div id="messageBox">
		<p class="error"><%=request.getAttribute("errorMessage")%></p>
	</div>
	<%
	}
	else if(request.getAttribute("successMessage") != null){
	%>
	<div id="messageBox">
		<p class="success"><%=request.getAttribute("successMessage")%></p>
	</div>
	<%
	}
	%>

	<a href="../user/showlogin" class="btn btn-success"
			style="position: absolute; top: 20px; right: 20px;">Go to Login</a>
			
		<div
		class="container d-flex justify-content-center align-items-center h-100">
		<div class="card w-50">
			<div class="card-header text-center">For New Register</div>
			<div class="card-body">
				<s:form action="../user/register">
						<s:textfield key="name" cssClass="form-control" label="Enter your name " />
						<s:textfield key="email" cssClass="form-control" label="Enter your email " />
						<s:password key="password" cssClass="form-control" label="Enter your password " />
						<s:textfield key="phonenumber" cssClass="form-control" label="Enter your phone number " />
						<s:submit cssClass="btn btn-primary" value="Register" />
				</s:form>
			</div>
		</div>
	</div>

</body>
</html>