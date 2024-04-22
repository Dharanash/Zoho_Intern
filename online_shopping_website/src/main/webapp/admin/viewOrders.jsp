<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%
ArrayList<Order> orders = (ArrayList<Order>) request.getSession().getAttribute("orders");
HashMap<Integer, String> orderStatus = (HashMap<Integer, String>)request.getSession().getAttribute("orderStatus");
%>

<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="../CSS/userpage-style.css">
<%@include file="../Includes/head.jsp"%>
<meta charset="ISO-8859-1">
<title>Orders</title>
</head>
<body>
<%@include file="../Includes/navbar.jsp"%>
	<div class="container my-3">
		<div class="d-flex py-3"><h3>Order History</h3> </div>
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
					<th scope="col">User Name</th>
					<th scope="col">Delivery Address</th>
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
					<td><%=r.getPrice()*r.getProductQuantity() %></td>	
					<td><%=r.getAddedTime()%></td>
					<td><%=orderStatus.get(r.getOrderStatusId()) %></td>
					<td><%=r.getUserName()%></td>
					<td><%=r.getDeliveryAddress()+", "+r.getDeliveryPincode()%></td>
				</tr>

				<%
				}}%>
			</tbody>
		</table>
	</div>
</body>
</html>