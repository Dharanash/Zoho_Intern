<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%Wallet wallet =(Wallet) request.getSession().getAttribute("wallet") ;  %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" type="text/css" href="../CSS/userpage-style.css">
<%@include file="../Includes/head.jsp"%>
<title>Wallet</title>
</head>
<body>
<%@include file="../Includes/navbar.jsp"%>
<div class="container my-3">
		<table class="table table-light">
			<thead>
				<tr>
					<th scope="col"><h2>Current Balance: <%=wallet.getBalance()%> </h2></th>
					<th scope="col"><h2>Points : <%=wallet.getPoint()%> </h2></th>
					<th scope="col"><a href="../WalletServlet/redeemPoints?id=<%=wallet.getPoint()%>" class="btn btn-sm btn-warning">Redeem</a></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<form action="../WalletServlet/addAmount" method="post">
						<td>Enter your amount : </td>
						<td><input type="number" name="amount" class="form-control" min="0" step="any" required></td>
						<td>
							<button type="submit" class="btn btn-sm btn-success">Add</button>
						</td>
					</form>
				</tr>
			</tbody>
		</table>
	</div>

</body>
</html>