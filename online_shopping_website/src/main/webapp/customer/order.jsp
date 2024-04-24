<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
ArrayList<Cart> cart = (ArrayList<Cart>) request.getAttribute("cart");
ArrayList<DeliveryDetails> deliveryDetails=(ArrayList<DeliveryDetails>) request.getAttribute("deliveryDetails");
Wallet wallet = (Wallet) request.getAttribute("wallet");
double totalAmount =Double.parseDouble(request.getAttribute("cartTotal").toString());
double balance=wallet.getBalance();
%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="../CSS/userpage-style.css">
<%@include file="../Includes/head.jsp"%>
<meta charset="ISO-8859-1">
<meta charset="ISO-8859-1">
<title>Order</title>
</head>
<body>
	<%@include file="../Includes/navbar.jsp"%>
	
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
			<h3>Total Price: <%=totalAmount%></h3>
		</div>
		<table class="table table-light">
			<thead>
				<tr>
					<th scope="col">Name</th>
					<th scope="col">Description</th>
					<th scope="col">Price</th>
					<th scope="col">Quantity</th>
				</tr>
			</thead>
			<tbody>
				<%
				for (Cart c : cart) {
					if (c.getProductStatusId() == ProductStatus.Available.getProductStatusId()) {
				%>
				<tr>

					<td><%=c.getName()%></td>
					<td><%=c.getDescription()%></td>
					<td><%=c.getPrice()%></td>
					<td><%=c.getProductQuantity()%></td>
				</tr>
				<%
				}}
				%>
			</tbody>
		</table>
		
		<div class="container py-3">
			<h3>Balance After Order :<%=balance%> - <%=totalAmount%> = <%=balance-totalAmount%></h3>
			</div>
			<%if(balance-totalAmount<0){ %>
			<div class="container py-3">
			<h4 class="text-danger py-3">Insufficient balance to make this purchase.</h4>
			<button class="mx-3 btn btn-primary"  disabled>Place Order</button>
			</div>
			<%}else if(deliveryDetails==null || deliveryDetails.size()==0){ %>
			<div class="container py-3">
			<h4 class="text-danger py-3">Kindly add your delivery details.</h4>
			<button class="mx-3 btn btn-primary"  disabled>Place Order</button>
			</div>
			<%}else{ %>
			
			<form action="../orders/placeorder" method="post">
					<div class="form-group">
						<label>Select Delivery Details :</label> <select name="deliveryDetailId"
							class="form-control" required>
							<%
							if (deliveryDetails != null) {
								for (DeliveryDetails d : deliveryDetails) {
							%>
							<option value="<%=d.getDeliveryDetailsId()%>"><%=d.getAddress() +","+ d.getPincode()%></option>
							<% }} %>
						</select>
					</div>
					
					<div class="text-center">
						<button type="submit" class="btn btn-success">Place Order</button>
					</div>
				</form>
				<% } %>
	</div>

</body>
</html>