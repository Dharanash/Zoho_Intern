<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    <%
    ArrayList<DetailedProduct> products = (ArrayList<DetailedProduct>)request.getAttribute("products");
%>

<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="../CSS/userpage-style.css">
<%@include file="../../Includes/head.jsp"%>
<meta charset="ISO-8859-1">
<title>View Inventory</title>
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
			<h3>Inventory</h3>
			<a class="mx-3 btn btn-primary" href="../inventory/addproduct">Add Product</a>
		</div>
		<table class="table table-light">
			<thead>
				<tr>
					<th scope="col">Name</th>
					<th scope="col">Description</th>
					<th scope="col">Price</th>
					<th scope="col">Created By</th>
					<th scope="col">Modified By</th>
					<th scope="col">Created Date</th>
					<th scope="col">Modified Date</th>
					<th scope="col">Available Quantity</th>
					<th scope="col">Update</th>
					<th scope="col">Remove</th>
				</tr>
			</thead>
			<tbody>
				<%
				if (products != null) {
					for (DetailedProduct p : products) {
				%>
				<tr>
					<td><%=p.getName()%></td>
					<td><%=p.getDescription()%></td>
					<td><%=p.getPrice()%></td>
					<td><%=p.getCreatedBy()%></td>
					<td><%=p.getModifiedBy() %></td>
					<td><%=p.getCreatedTime() %></td>
					<td><%=p.getModifiedTime() %></td>
					<td><%=p.getQuantity()%></td>
					<td><a href="../inventory/updateproduct?id=<%=p.getProductId() %>" class="btn btn-sm btn-warning">Update</a></td>	
					<td><a href="../inventory/remove?id=<%=p.getProductId() %>" class="btn btn-sm btn-danger">Remove</a></td>
				</tr>
				<%
				}}%>
			</tbody>
		</table>
	</div>

</body>
</html>