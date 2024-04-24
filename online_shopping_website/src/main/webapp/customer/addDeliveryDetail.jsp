<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" type="text/css" href="../CSS/userpage-style.css">
<%@include file="../../Includes/head.jsp"%>
<title>Add Delivery Detail</title>
</head>
<body>

<%@include file="../../Includes/navbar.jsp"%>
	<div class="container d-flex justify-content-center align-items-center h-100">
			<div class="card w-50">
				<div class="card-header text-center">Add Delivery Detail</div>
				<div class="card-body">
					<form action="../orders/adddeliverydetail" method="post">
						<div class="form-group">
							<label>Enter address</label> <input type="text" name="address"
								class="form-control" placeholder="Enter your full address" required>
						</div>
						<div class="form-group">
							<label>Password</label> <input type="tel" name="pincode"
								class="form-control" pattern="[0-9]{6}" placeholder="Enter 6 digit pincode" required>
						</div>
						<div class="text-center">
							<button type="submit" class="btn btn-primary">Add</button>
						</div>
					</form>
				</div>
			</div>
		</div>
</body>
</html>