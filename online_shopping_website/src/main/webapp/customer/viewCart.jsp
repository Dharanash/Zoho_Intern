<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="online_shopping_website.model.*"%>
<%@page import="java.util.ArrayList"%>
<%
ArrayList<Cart> cart = (ArrayList<Cart>) request.getAttribute("cart");
double cartTotal = Double.parseDouble(request.getAttribute("cartTotal").toString());
%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="../CSS/userpage-style.css">
<%@include file="../../Includes/head.jsp"%>
<meta charset="ISO-8859-1">
<title>User Cart</title>
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
			<h3>
				Total Price:
				<%=cartTotal%>
			</h3>
			<a class="mx-3 btn btn-primary" href="../orders/placeorder">Order</a>
		</div>
		<table class="table table-light">
			<thead>
				<tr>
					<th scope="col">Name</th>
					<th scope="col">Description</th>
					<th scope="col">Price</th>
					<th scope="col">Available Quantity</th>
					<th scope="col">Quantity</th>
					<th scope="col">Status</th>
					<th scope="col">Update</th>
					<th scope="col">Remove</th>
				</tr>
			</thead>
			<tbody>
				<%
				if (cart != null) {
					for (Cart c : cart) {
				%>
				<tr>
					<form action="../cart/update" method="post">
						<td><%=c.getName()%></td>
						<td><%=c.getDescription()%></td>
						<td><%=c.getPrice()%></td>
						<td><%=c.getQuantity()%></td>
						<td><input type="number" name="productQuantity"
							value="<%=c.getProductQuantity()%>" min="0" class="form-control">
							<input type="hidden" name="productId"
							value="<%=c.getProductId()%>"></td>
						<td>
							<%
							if (c.getQuantity() < c.getProductQuantity()) {
							%> <%=ProductStatus.OutOfStock.toString()%>
							<%
							} else {
							%> <%=c.getProductStatus()%> <%}%>
						</td>
						<td>
							<button type="submit" class="btn btn-sm btn-success"
								<%if (c.getProductStatusId() != (ProductStatus.Available.getProductStatusId())) {%>
								disabled <%}%>>Update</button>
						</td>
					</form>
					<td><a href="../cart/remove?id=<%=c.getProductId()%>"
						class="btn btn-sm btn-danger">Remove</a></td>
				</tr>
				<%
				}
				}
				%>
			</tbody>
		</table>
	</div>

</body>
</html>