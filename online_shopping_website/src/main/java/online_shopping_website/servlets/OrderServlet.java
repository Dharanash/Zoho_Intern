package online_shopping_website.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import online_shopping_website.dao.OrderDAO;
import online_shopping_website.enums.OrderStatus;
import online_shopping_website.enums.Role;
import online_shopping_website.model.Cart;
import online_shopping_website.model.DeliveryDetails;
import online_shopping_website.model.DetailedProduct;
import online_shopping_website.model.Order;
import online_shopping_website.model.User;
import online_shopping_website.model.Wallet;
import online_shopping_website.services.UtilityService;

/**
 * Servlet implementation class OrderServlet
 */
@WebServlet("/OrderServlet/*")
public class OrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OrderDAO orderDAO;

	public OrderServlet() {
		super();
		orderDAO = new OrderDAO();
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getPathInfo();
		try {
			switch (action) {
			case "/getDeliveryDetails":
				getDeliveryDetails(request, response);
				break;
			case "/addDeliveryDetails":
				addDeliveryDetails(request, response);
				break;
			case "/updateDeliveryDetails":
				updateDeliveryDetails(request, response);
				break;
			case "/preOrder":
				prePlaceOrder(request, response);
				break;
			case "/postOrder":
				postPlaceOrder(request, response);
				break;
			case "/getUserOrder":
				getUserOrders(request, response);
				break;
			case "/getAllOrder":
				getAllOrders(request, response);
				break;
			case "/cancelOrder":
				cancelOrder(request, response);
				break;
			case "/getReceivedOrders":
				getReceivedOrders(request, response);
				break;
			case "/dispatchReceivedOrder":
				dispatchReceivedOrder(request, response);
				break;
			case "/cancelReceivedOrder":
				cancelReceivedOrder(request, response);
				break;
			default:
				break;
			}
		} catch (SQLException | ClassNotFoundException ex) {
			throw new ServletException(ex);
		}
	}

	protected void getDeliveryDetails(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		ArrayList<DeliveryDetails> deliveryDetails = orderDAO.getCustomerDetails(user.userId);
		request.getSession().setAttribute("deliveryDetails", deliveryDetails);
		response.sendRedirect("../customer/deliveryDetails.jsp");
	}

	protected void addDeliveryDetails(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		String address = request.getParameter("address");
		int pincode = Integer.parseInt(request.getParameter("pincode"));
		ArrayList<DeliveryDetails> deliveryDetails = (ArrayList<DeliveryDetails>) request.getSession()
				.getAttribute("deliveryDetails");
		DeliveryDetails detail = new DeliveryDetails(user.userId, address, pincode);
		int detailId = orderDAO.addCustomerDetails(detail);
		detail.setDeliveryDetailsId(detailId);
		deliveryDetails.add(detail);
		response.sendRedirect("../customer/deliveryDetails.jsp");
	}

	protected void updateDeliveryDetails(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		int id = Integer.parseInt(request.getParameter("id"));
		String address = request.getParameter("address");
		int pincode = Integer.parseInt(request.getParameter("pincode"));
		ArrayList<DeliveryDetails> deliveryDetails = (ArrayList<DeliveryDetails>) request.getSession()
				.getAttribute("deliveryDetails");
		DeliveryDetails detail = UtilityService.getDeliveryDetailFromId(deliveryDetails, id);
		detail.setAddress(address);
		detail.setPincode(pincode);
		response.sendRedirect("../customer/deliveryDetails.jsp");
	}

	protected void prePlaceOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		if (request.getSession().getAttribute("wallet") == null) {
			request.getSession().setAttribute("wallet", orderDAO.getWalletFromId(user.userId));
		}
		if (request.getSession().getAttribute("deliveryDetails") == null) {
			request.getSession().setAttribute("deliveryDetails", orderDAO.getCustomerDetails(user.userId));
		}
		response.sendRedirect("../customer/order.jsp");
	}

	protected void postPlaceOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		int deliveryDetailsId = Integer.parseInt(request.getParameter("deliveryDetailId"));
		ArrayList<Cart> cart = (ArrayList<Cart>) request.getSession().getAttribute("cart");
		orderDAO.addOrder(cart, user.userId, deliveryDetailsId);

		Wallet wallet = (Wallet) request.getSession().getAttribute("wallet");
		double totalAmount = Double.parseDouble(request.getSession().getAttribute("cartTotal").toString());
		wallet.balance -= totalAmount;
		orderDAO.updateWallet(wallet);
		response.sendRedirect("../customer/viewCart.jsp");
	}

	protected void getUserOrders(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		if (request.getSession().getAttribute("orders") == null) {
			request.getSession().setAttribute("orders", orderDAO.getUserOrders(user.userId));
		}

		if (request.getSession().getAttribute("orderStatus") == null) {
			request.getSession().setAttribute("orderStatus", orderDAO.getOrderStatus());
		}
		response.sendRedirect("../customer/purchaseHistory.jsp");
	}

	protected void cancelOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		int orderId = Integer.parseInt(request.getParameter("id"));
		ArrayList<Order> orders = (ArrayList<Order>) request.getSession().getAttribute("orders");
		Order order = UtilityService.getOrderFromId(orders, orderId);
		orderDAO.updateOrderStatus(orderId, OrderStatus.Cancelled);
		order.setOrderStatusId(OrderStatus.Cancelled.getOrderStatusId());
		if (request.getSession().getAttribute("wallet") == null) {
			request.getSession().setAttribute("wallet", orderDAO.getWalletFromId(user.userId));
		}
		Wallet wallet = (Wallet) request.getSession().getAttribute("wallet");
		wallet.addAmountToWalletBalance(order.price * order.productQuantity);
		orderDAO.updateWallet(wallet);
		response.sendRedirect("../customer/purchaseHistory.jsp");
	}

	protected void getAllOrders(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		if (request.getSession().getAttribute("orders") == null) {
			request.getSession().setAttribute("orders", orderDAO.getAllOrders());
		}

		if (request.getSession().getAttribute("orderStatus") == null) {
			request.getSession().setAttribute("orderStatus", orderDAO.getOrderStatus());
		}
		response.sendRedirect("../admin/viewOrders.jsp");
	}

	protected void getReceivedOrders(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		if (request.getSession().getAttribute("receivedOrders") == null) {
			request.getSession().setAttribute("receivedOrders", orderDAO.getOrdersForDispatch(user.userId));
		}
		if (request.getSession().getAttribute("orderStatus") == null) {
			request.getSession().setAttribute("orderStatus", orderDAO.getOrderStatus());
		}
		ArrayList<Order> orders = (ArrayList<Order>) request.getSession().getAttribute("receivedOrders");
		response.sendRedirect("../admin/receivedOrders.jsp");
	}

	protected void dispatchReceivedOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		int orderId = Integer.parseInt(request.getParameter("id"));
		if (request.getSession().getAttribute("receivedOrders") == null) {
			request.getSession().setAttribute("receivedOrders", orderDAO.getOrdersForDispatch(user.userId));
		}
		ArrayList<Order> orders = (ArrayList<Order>) request.getSession().getAttribute("receivedOrders");
		Order order = UtilityService.getOrderFromId(orders, orderId);
		order.setOrderStatusId(OrderStatus.Dispatched.getOrderStatusId());
		orderDAO.updateOrderStatus(orderId, OrderStatus.Dispatched);

		if (request.getSession().getAttribute("wallet") == null) {
			request.getSession().setAttribute("wallet", orderDAO.getWalletFromId(order.deliveryDetails.userId));
		}
		Wallet wallet = (Wallet) request.getSession().getAttribute("wallet");
		wallet.addPointsToWallet((int) (order.price * order.productQuantity) / 10);
		orderDAO.updateWallet(wallet);
		request.getSession().removeAttribute("wallet");
		response.sendRedirect("../admin/receivedOrders.jsp");
	}

	protected void cancelReceivedOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		int orderId = Integer.parseInt(request.getParameter("id"));
		if (request.getSession().getAttribute("receivedOrders") == null) {
			request.getSession().setAttribute("receivedOrders", orderDAO.getOrdersForDispatch(user.userId));
		}
		ArrayList<Order> orders = (ArrayList<Order>) request.getSession().getAttribute("receivedOrders");
		Order order = UtilityService.getOrderFromId(orders, orderId);
		order.setOrderStatusId(OrderStatus.Cancelled.getOrderStatusId());
		orderDAO.updateOrderStatus(orderId, OrderStatus.Cancelled);

		if (request.getSession().getAttribute("wallet") == null) {
			request.getSession().setAttribute("wallet", orderDAO.getWalletFromId(order.deliveryDetails.userId));
		}
		Wallet wallet = (Wallet) request.getSession().getAttribute("wallet");
		wallet.addAmountToWalletBalance(order.price * order.productQuantity);
		orderDAO.updateWallet(wallet);
		request.getSession().removeAttribute("wallet");
		response.sendRedirect("../admin/receivedOrders.jsp");

	}

}
