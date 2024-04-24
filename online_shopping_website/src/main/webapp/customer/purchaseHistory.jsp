<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%
ArrayList<Order> orders = (ArrayList<Order>) request.getAttribute("orders");
%>

<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="../CSS/userpage-style.css">
<%@include file="../../Includes/head.jsp"%>
<meta charset="ISO-8859-1">
<title>Purchase History</title>
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
			<h3>Order History</h3>
		</div>
		<table class="table table-light">
			<thead>
				<tr>
					<th scope="col">Name</th>
					<th scope="col">Description</th>
					<th scope="col">Price</th>
					<th scope="col">Quantity</th>
					<th scope="col">Total</th>
					<th scope="col">Ordered Date</th>
					<th scope="col">Status</th>
					<th scope="col">Cancel</th>
				</tr>
			</thead>
			<tbody>
				<%
				if (orders != null) {
					for (Order r : orders) {
				%>
				<tr>

					<td><%=r.getName()%></td>
					<td><%=r.getDescription()%></td>
					<td><%=r.getPrice()%></td>
					<td><%=r.getProductQuantity()%></td>
					<td><%=r.getPrice() * r.getProductQuantity()%></td>
					<td><%=r.getAddedTime()%></td>
					<td><%=r.getOrderStatus()%></td>
					<td><a
						href="../orders/cancelorder?id=<%=r.getOrderId()%>"
						class="btn btn-sm <%if (r.getOrderStatusId() != OrderStatus.Placed.getOrderStatusId()) {%> btn-secondary"
						onclick="return false;" <%} else {%>btn-danger"<%}%>">Cancel </a></td>
				</tr>

				<%
				}}
				%>
			</tbody>
		</table>
	</div>
</body>
</html>