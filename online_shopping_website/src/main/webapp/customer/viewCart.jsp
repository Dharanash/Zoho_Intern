<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%
ArrayList<Cart> cart = (ArrayList<Cart>) request.getSession().getAttribute("cart");
%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="../CSS/userpage-style.css">
<%@include file="../Includes/head.jsp"%>
<meta charset="ISO-8859-1">
<title>User Cart</title>
</head>
<body>

	<%@include file="../Includes/navbar.jsp"%>

	<div class="container my-3">
		<div class="d-flex py-3">
			<h3>Total Price: <%= request.getSession().getAttribute("cartTotal")%>  </h3>
			<a class="mx-3 btn btn-primary" href="../OrderServlet/preOrder">Order</a>
		</div>
		<table class="table table-light">
			<thead>
				<tr>
					<th scope="col">Name</th>
					<th scope="col">Description</th>
					<th scope="col">Price</th>
					<th scope="col">Status</th>
					<th scope="col">Quantity</th>
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
					<form action="../CartServlet/updateCart" method="post">
						<td><%=c.getName()%></td>
						<td><%=c.getDescription()%></td>
						<td><%=c.getPrice()%></td>
						<td><%=c.getProductStatus()%></td>
						<td><input type="number" name="productQuantity"
							value="<%=c.getProductQuantity()%>" class="form-control"
							<%if (!c.getProductStatus().equals(ProductStatus.Available.toString())) {%>
							disabled <%}%> min="0"> <input type="hidden" name="productId"
							value="<%=c.getProductId()%>"></td>
						<td>
							<button type="submit" class="btn btn-sm btn-success"
								<%if (!c.getProductStatus().equals(ProductStatus.Available.toString())) {%>
								disabled <%}%>>Update</button>
						</td>
					</form>
					<td><a href="../CartServlet/removeFromCart?id=<%=c.getProductId() %>" class="btn btn-sm btn-danger">Remove</a></td>
				</tr>
				<%
				}}
				%>
			</tbody>
		</table>
	</div>

</body>
</html>