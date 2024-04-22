<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="../CSS/userpage-style.css">
<%@include file="../Includes/head.jsp"%>
<meta charset="ISO-8859-1">
<title>Add Product</title>
</head>
<body>
	<%@include file="../Includes/navbar.jsp"%>
	<%
	HashMap<Integer, String> productStatus = (HashMap<Integer, String>) request.getSession().getAttribute("productStatus");
	if (request.getParameter("addProductErrorMessage") != null) {
	%>
	<div id="messageBox">
		<p class="error"><%=request.getParameter("addProductErrorMessage")%></p>
	</div>
	<%
	} else if (request.getParameter("addProductSuccessMessage") != null) {
	%>
	<div id="messageBox">
		<p class="success"><%=request.getParameter("addProductSuccessMessage")%></p>
	</div>
	<%
	}
	%>

	<div
		class="container d-flex justify-content-center align-items-center h-100">
		<div class="card w-50">
			<div class="card-header text-center">Add Product</div>
			<div class="card-body">
				<form action="../InventoryServlet/postAddProduct" method="post">
					<div class="form-group">
						<label>Enter Product Name : </label> <input type="text"
							name="pname" class="form-control" required>
					</div>
					<div class="form-group">
						<label>Enter Description : </label> <input type="text"
							name="description" class="form-control" required>
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
					<div class="form-group">
						<label>Enter Price : </label> <input pattern="[0-9.]{1,}"
							title="Please enter only positive number" name="price"
							class="form-control" required>
					</div>
					
					<div class="text-center">
						<button type="submit" class="btn btn-success">Add</button>
					</div>
				</form>
			</div>
		</div>
	</div>

</body>
</html>