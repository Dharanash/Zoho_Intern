<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="online_shopping_website.model.*" %>
<%@page import="java.util.ArrayList"%>
    <%
    ArrayList<DetailedProduct> products = (ArrayList<DetailedProduct>)request.getAttribute("products");
%>

<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="../CSS/userpage-style.css">
<%@include file="../../Includes/head.jsp"%>
<meta charset="ISO-8859-1">
<title>Products</title>
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
		<div class="d-flex py-3"><h3>List Of Products</h3> </div>
		<table class="table table-light">
			<thead>
				<tr>
					<th scope="col">Name</th>
					<th scope="col">Description</th>
					<th scope="col">Price</th>
					<th scope="col">Available Quantity</th>
					<th scope="col">Add To Cart</th>
				</tr>
			</thead>
			<tbody>
				<%
				if (products != null) {
					for (Product p : products) {
				%>
				<tr>
					<td><%=p.getName()%></td>
					<td><%=p.getDescription()%></td>
					<td><%=p.getPrice()%></td>
					<td><%=p.getQuantity()%></td>	
					<td><a href="../cart/add?id=<%=p.getProductId() %>" class="btn btn-sm <%if (p.getQuantity()<=0) {%> btn-secondary"
						onclick="return false;" <%} else {%>btn-success"<%}%>">Add To Cart</a></td>
				</tr>

				<%
				}}%>
			</tbody>
		</table>
	</div>

</body>
</html>