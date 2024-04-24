package online_shopping_website.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import online_shopping_website.dao.CartDAO;
import online_shopping_website.dao.OrderDAO;
import online_shopping_website.enums.OrderStatus;
import online_shopping_website.enums.Role;
import online_shopping_website.exceptions.UserNotifyException;
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
@WebServlet("/orders/*")
public class OrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OrderDAO orderDAO;
	private CartDAO cartDAO;

	public OrderServlet() {
		super();
		orderDAO = new OrderDAO();
		cartDAO = new CartDAO();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getPathInfo();
		try {
			switch (action) {
			case "/getdeliverydetails":
				getDeliveryDetails(request, response);
				break;
			case "/adddeliverydetail":
				getAddDeliveryDetails(request, response);
				break;
			case "/placeorder":
				getPlaceOrder(request, response);
				break;
			case "/getuserorder":
				getUserOrders(request, response);
				break;
			case "/getallorders":
				getAllOrders(request, response);
				break;
			case "/cancelorder":
				cancelOrder(request, response);
				break;
			case "/getreceivedorders":
				getReceivedOrders(request, response);
				break;
			case "/dispatchreceivedorder":
				dispatchReceivedOrder(request, response);
				break;
			case "/cancelreceivedorder":
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
		request.setAttribute("deliveryDetails", deliveryDetails);
		request.getRequestDispatcher("../customer/deliveryDetails.jsp").forward(request, response);
	}

	protected void getAddDeliveryDetails(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		request.getRequestDispatcher("../customer/addDeliveryDetail.jsp").forward(request, response);
	}

	protected void updateDeliveryDetails(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		int id = Integer.parseInt(request.getParameter("id"));
		String address = request.getParameter("address");
		int pincode = Integer.parseInt(request.getParameter("pincode"));
		DeliveryDetails detail = new DeliveryDetails(id, user.userId, address, pincode);
		orderDAO.updateCustomerDetails(detail);
		response.sendRedirect("../orders/getdeliverydetails?successMessage=Delivery address updated successfully.");
	}

	protected void getPlaceOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		ArrayList<Cart> cart = cartDAO.getCartElements(user.userId);
		request.setAttribute("cart", cart);
		double totalAmount = UtilityService.getTotalAmount(cart);
		request.setAttribute("cartTotal", totalAmount);
		request.setAttribute("wallet", orderDAO.getWalletFromId(user.userId));
		request.setAttribute("deliveryDetails", orderDAO.getCustomerDetails(user.userId));
		request.getRequestDispatcher("../customer/order.jsp").forward(request, response);
	}

	protected void getUserOrders(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		request.setAttribute("orders", orderDAO.getUserOrders(user.userId));
		request.getRequestDispatcher("../customer/purchaseHistory.jsp").forward(request, response);
	}

	protected void getAllOrders(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		request.setAttribute("orders", orderDAO.getAllOrders());
		request.getRequestDispatcher("../admin/viewOrders.jsp").forward(request, response);
	}

	protected void cancelOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		int orderId = Integer.parseInt(request.getParameter("id"));
		orderDAO.updateOrderStatus(orderId, OrderStatus.Cancelled);
		double amount =orderDAO.releaseProductsToInventory(orderId);
		orderDAO.addToWalletAmount(user.userId, amount);
		response.sendRedirect("../orders/getuserorder?successMessage=Order cancelled successfully.");
	}
	
	protected void getReceivedOrders(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		request.getSession().setAttribute("receivedOrders", orderDAO.getOrdersForDispatch(user.userId));
		request.getRequestDispatcher("../admin/receivedOrders.jsp").forward(request, response);
	}
	
	protected void dispatchReceivedOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		int orderId = Integer.parseInt(request.getParameter("id"));
		int userId = Integer.parseInt(request.getParameter("userid"));
		double amount = Double.parseDouble(request.getParameter("amount"));
		orderDAO.updateOrderStatus(orderId, OrderStatus.Dispatched);

		orderDAO.addToWalletPoints(userId, (int)amount/10);
		response.sendRedirect("../orders/getreceivedorders?successMessage=Order dispatched successfully.");
	}

	protected void cancelReceivedOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		int orderId = Integer.parseInt(request.getParameter("id"));
		int userId = Integer.parseInt(request.getParameter("userid"));
		orderDAO.updateOrderStatus(orderId, OrderStatus.Cancelled);
		double amount =orderDAO.releaseProductsToInventory(orderId);
		orderDAO.addToWalletAmount(userId, amount);
		response.sendRedirect("../orders/getreceivedorders?successMessage=Order cancelled successfully.");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getPathInfo();
		try {
			switch (action) {
			case "/adddeliverydetail":
				addDeliveryDetails(request, response);
				break;
			case "/updatedeliverydetails":
				updateDeliveryDetails(request, response);
				break;
			case "/placeorder":
				postPlaceOrder(request, response);
				break;
			default:
				break;
			}
		} catch (SQLException | ClassNotFoundException ex) {
			throw new ServletException(ex);
		}
	}

	protected void addDeliveryDetails(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		try {
		if(orderDAO.getCustomerDetails(user.userId).size()>=3) {
			throw new UserNotifyException("Reached maximum address to add.");
		}
		String address = request.getParameter("address");
		int pincode = Integer.parseInt(request.getParameter("pincode"));
		DeliveryDetails detail = new DeliveryDetails(user.userId, address, pincode);
		orderDAO.addCustomerDetails(detail);
		response.sendRedirect("../orders/getdeliverydetails?successMessage=Delivery Details successfully added.");
		}
		catch (UserNotifyException e) {
			response.sendRedirect("../orders/getdeliverydetails?errorMessage="+e.getMessage());
		}
	}

	protected void postPlaceOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		try {
		User user = (User) request.getSession().getAttribute("auth");
		int deliveryDetailsId = Integer.parseInt(request.getParameter("deliveryDetailId"));
		ArrayList<Cart> cart = cartDAO.getCartElements(user.userId);
		double totalAmount = UtilityService.getTotalAmount(cart);
		if(totalAmount<=0) {
			throw new UserNotifyException("Can't make empty orders.");
		}
		if(!orderDAO.deduceProductQuantityFromInventory(cart,user.userId)) {
			throw new UserNotifyException("Some products doesn't have sufficient quantity, check and update the cart to make order.");
		}
		orderDAO.addOrder(cart, user.userId, deliveryDetailsId);
		orderDAO.reduceToWalletAmount(user.userId, totalAmount);
		response.sendRedirect("../cart/get?successMessage=Order placed successfully");
		}
		catch (UserNotifyException e) {
			response.sendRedirect("../cart/get?errorMessage="+e.getMessage());
		}
	}

}
