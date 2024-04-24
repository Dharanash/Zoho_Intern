<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ page import="online_shopping_website.model.Product" %>
 
 <%
    DetailedProduct product=(DetailedProduct) request.getAttribute("product");
 	HashMap<Integer, String> productStatus = (HashMap<Integer, String>) request.getAttribute("productStatus");
 %>
 
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" type="text/css" href="../CSS/userpage-style.css">
<%@include file="../../Includes/head.jsp"%>
<title>Update Product</title>
</head>
<body>
<%@include file="../../Includes/navbar.jsp"%>
	<div
		class="container d-flex justify-content-center align-items-center h-100">
		<div class="card w-50">
			<div class="card-header text-center">Update Product</div>
			<div class="card-body">
				<form action="../inventory/updateproduct" method="post">
				 <input type="hidden" name="pid" value="${product.getProductId() }">
					<div class="form-group">
						<label>Enter Product Name:</label> 
						<input type="text" name="pname" class="form-control" value="${ product.getName() }" required>
					</div>
					<div class="form-group">
						<label>Enter Description:</label> 
						<input type="text" name="description" class="form-control" value="<%= product.getDescription() %>" required>
					</div>
					<div class="form-group">
						<label>Enter Price:</label> 
						<input type="number" min="0" step="any" title="Please enter only positive number" name="price" class="form-control" value="<%= product.getPrice() %>" required>
					</div>
					<div class="form-group">
						<label>Enter Quantity : </label> <input type="number" min="0"
							name="quantity" class="form-control" value="<%= product.getQuantity() %>" required>
					</div>
					<div class="text-center">
						<button type="submit" class="btn btn-success">Update</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>