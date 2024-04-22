<%@page import="online_shopping_website.model.*"%>
<%@page import="online_shopping_website.servlets.*"%>
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
					href="../user/index.jsp">Home</a></li>
				<%
				if (user.role == Role.Admin) {
				%>
				
				<li class="nav-item"><a class="nav-link"
					href="../InventoryServlet/getInventory">Inventory </a></li>
				<li class="nav-item"><a class="nav-link"
					href="../OrderServlet/getAllOrder">Orders </a></li>
				<li class="nav-item"><a class="nav-link"
					href="../OrderServlet/getReceivedOrders">Received Orders </a></li>
				<li class="nav-item"><a class="nav-link"
					href="../admin/addManager.jsp">Add Manager </a></li>
				<%
				} else if (user.role == Role.Manager) {
				%>
				<li class="nav-item"><a class="nav-link"
					href="../InventoryServlet/getInventory">Inventory </a></li>
				<li class="nav-item"><a class="nav-link"
					href="../OrderServlet/getReceivedOrders">Received Orders </a></li>
				<%
				} else if (user.role == Role.Customer) {
				%>
				<li class="nav-item"><a class="nav-link"
					href="../InventoryServlet/getInventory">View Products </a></li>
				<li class="nav-item"><a class="nav-link"
					href="../CartServlet/getCart">Cart </a></li>
				<li class="nav-item"><a class="nav-link"
					href="../OrderServlet/getUserOrder">Purchase History</a></li>
				<li class="nav-item"><a class="nav-link"
					href="../WalletServlet/getWallet">Wallet</a></li>
				<li class="nav-item"><a class="nav-link"
					href="../OrderServlet/getDeliveryDetails">Delivery Details</a></li>
				<%
				}
				%>
				<li class="nav-item ml-5 mr-3"><a
					class="nav-link btn btn-success" href="../user/profile.jsp">Profile</a>
				</li>
				<li class="nav-item"><a class="nav-link btn btn-danger"
					href="../UserServlet/logout">Logout</a></li>
			</ul>

		</div>
	</div>
</nav>