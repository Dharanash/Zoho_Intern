<%@page import="online_shopping_website.model.*"%>
<%@page import="online_shopping_website.controller.*"%>
<%@page import="online_shopping_website.enums.*"%>
<%@page import="java.util.*"%>
<%
User user=(User) session.getAttribute("auth");
%>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
	<h3>Online Shopping Website</h3>
	<div class="container">
		<div class="collapse navbar-collapse">
			<ul class="navbar-nav ml-auto">
				
				<li class="nav-item"><a class="nav-link"
					href="../user/home">Home</a></li>
				<%
				if (user.role == Role.Admin) {
				%>
				
				<li class="nav-item"><a class="nav-link"
					href="../inventory/get">Inventory </a></li>
				<li class="nav-item"><a class="nav-link"
					href="../orders/getallorders">Orders </a></li>
				<li class="nav-item"><a class="nav-link"
					href="../orders/getreceivedorders">Received Orders </a></li>
				<li class="nav-item"><a class="nav-link"
					href="../user/getaddmanager">Add Manager </a></li>
				<%
				} else if (user.role == Role.Manager) {
				%>
				<li class="nav-item"><a class="nav-link"
					href="../inventory/get">Inventory </a></li>
				<li class="nav-item"><a class="nav-link"
					href="../orders/getreceivedorders">Received Orders </a></li>
				<%
				} else if (user.role == Role.Customer) {
				%>
				<li class="nav-item"><a class="nav-link"
					href="../inventory/get">View Products </a></li>
				<li class="nav-item"><a class="nav-link"
					href="../cart/get">Cart </a></li>
				<li class="nav-item"><a class="nav-link"
					href="../orders/getuserorder">Purchase History</a></li>
				<li class="nav-item"><a class="nav-link"
					href="../wallet/get">Wallet</a></li>
				<li class="nav-item"><a class="nav-link"
					href="../orders/getdeliverydetails">Delivery Details</a></li>
				<%
				}
				%>
				<li class="nav-item ml-5 mr-3"><a
					class="nav-link btn btn-success" href="../user/getprofile">Profile</a>
				</li>
				<li class="nav-item"><a class="nav-link btn btn-danger"
					href="../user/logout">Logout</a></li>
			</ul>

		</div>
	</div>
</nav>