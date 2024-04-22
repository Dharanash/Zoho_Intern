<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ page import="online_shopping_website.model.Product" %>
 
 <%
 	Product product=(Product) request.getSession().getAttribute("product");
 	HashMap<Integer, String> productStatus = (HashMap<Integer, String>) request.getSession().getAttribute("productStatus");
 %>
 
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" type="text/css" href="../CSS/userpage-style.css">
<%@include file="../Includes/head.jsp"%>
<title>Update Product</title>
</head>
<body>
<%@include file="../Includes/navbar.jsp"%>
	<div
		class="container d-flex justify-content-center align-items-center h-100">
		<div class="card w-50">
			<div class="card-header text-center">Update Product</div>
			<div class="card-body">
				<form action="../InventoryServlet/postUpdateProduct" method="post">
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
						<input pattern="[0-9.]+" title="Please enter only positive number" name="price" class="form-control" value="<%= product.getPrice() %>" required>
					</div>
					<div class="form-group">
						<label>Select Status:</label> <select name="selectedStatus"
							class="form-control" required>
							<%
							if (productStatus != null) {
								for (Map.Entry<Integer, String> entry : productStatus.entrySet()) {
							%>
							<option value="<%=entry.getKey()%>"><%=entry.getValue()%></option>
							<% }} %>
						</select>
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