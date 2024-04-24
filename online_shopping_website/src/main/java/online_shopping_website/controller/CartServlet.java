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
import online_shopping_website.exceptions.UserNotifyException;
import online_shopping_website.model.Cart;
import online_shopping_website.model.Product;
import online_shopping_website.model.User;
import online_shopping_website.services.UtilityService;

/**
 * Servlet implementation class CartServlet
 */
@WebServlet("/cart/*")
public class CartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CartDAO cartDAO;

	public CartServlet() {
		super();
		cartDAO = new CartDAO();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getPathInfo();
		try {
			switch (action) {
			case "/get":
				getCartProducts(request, response);
				break;
			case "/add":
				addToCart(request, response);
				break;
			case "/remove":
				removeFromCart(request, response);
				break;
			default:
				break;
			}
		} catch (SQLException | ClassNotFoundException ex) {
			throw new ServletException(ex);
		}
	}
	
	protected void getCartProducts(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		ArrayList<Cart> cart = cartDAO.getCartElements(user.userId);
		request.setAttribute("cart", cart);
		request.setAttribute("cartTotal", UtilityService.getTotalAmount(cart));
		request.getRequestDispatcher("../customer/viewCart.jsp").forward(request, response);
	}
	
	protected void addToCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		int productId = Integer.parseInt(request.getParameter("id"));
		try {
			ArrayList<Cart> cart = (ArrayList<Cart>) request.getSession().getAttribute("cart");
			if (cartDAO.isProductExistInCart(productId, user.userId)) {
				throw new UserNotifyException("Product already exist.");
			} else {
				cartDAO.addToCart(user.userId, productId);
				throw new UserNotifyException("Product added to cart successfully");
			}
		} catch (UserNotifyException e) {
			response.sendRedirect("../inventory/get?successMessage=" + e.getMessage());
		}
	}
	
	protected void removeFromCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		int productId = Integer.parseInt(request.getParameter("id"));
		cartDAO.removeProductInCart(productId, user.userId);
		response.sendRedirect("../cart/get?successMessage=Product removed from cart.");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getPathInfo();
		try {
			switch (action) {
			case "/update":
				updateCartProducts(request, response);
				break;
			default:
				break;
			}
		} catch (SQLException | ClassNotFoundException ex) {
			throw new ServletException(ex);
		}
	}

	protected void updateCartProducts(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		int productId = Integer.parseInt(request.getParameter("productId"));
		int productQuantity =Math.abs(Integer.parseInt(request.getParameter("productQuantity")));
		cartDAO.updateProductQuantityInCart(productId, user.userId, productQuantity);
		response.sendRedirect("../cart/get?successMessage=Product updated successfully.");
	}
}
