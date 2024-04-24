<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <% ArrayList<DeliveryDetails> deliveryDetails =(ArrayList<DeliveryDetails>)request.getAttribute("deliveryDetails"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" type="text/css" href="../CSS/userpage-style.css">
<%@include file="../../Includes/head.jsp"%>
<title>Delivery Details</title>
</head>
<body>
<%@include file="../../Includes/navbar.jsp"%>

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
	
<div class="container my-3">
<div class="d-flex py-3">
			<h3>Delivery Details</h3>
			<a class="mx-3 btn btn-primary" href="../orders/adddeliverydetail">Add</a>
		</div>
		<table class="table table-light">
			<thead>
				<tr>
					<th scope="col">Address</th>
					<th scope="col">Pincode</th>
					<th scope="col">Update</th>
				</tr>
			</thead>
			<tbody>
			<%if(deliveryDetails!=null) {
				for(DeliveryDetails d: deliveryDetails){
			%>
				<tr>
					<form action="../orders/updatedeliverydetails" method="post">
						 <input type="hidden" name="id" value="<%=d.getDeliveryDetailsId()%>">
						<td><input type="text" name="address" class="form-control" value="<%=d.getAddress()%>" required></td>
						<td><input type="text" name="pincode" pattern="[0-9]{6}" class="form-control" value="<%=d.getPincode()%>" required></td>
						<td>
							<button type="submit" class="btn btn-sm btn-success">Update</button>
						</td>
					</form>
				</tr>
				<%}} %>
			</tbody>
		</table>
	</div>

</body>
</html>