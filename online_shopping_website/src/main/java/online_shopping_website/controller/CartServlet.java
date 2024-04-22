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
import online_shopping_website.exceptions.UserNotifyException;
import online_shopping_website.model.Cart;
import online_shopping_website.model.Product;
import online_shopping_website.model.User;
import online_shopping_website.services.UtilityService;

/**
 * Servlet implementation class CartServlet
 */
@WebServlet("/CartServlet/*")
public class CartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CartDAO cartDAO;

	public CartServlet() {
		super();
		cartDAO = new CartDAO();
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getPathInfo();
		try {
			switch (action) {
			case "/getCart":
				getCartProducts(request, response);
				break;
			case "/addToCart":
				addToCart(request, response);
				break;
			case "/updateCart":
				updateCartProducts(request, response);
				break;
			case "/removeFromCart":
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
		ArrayList<Cart> cart = (ArrayList<Cart>) request.getSession().getAttribute("cart");
		if (cart == null) {
			cart = cartDAO.getCartElements(user.userId);
			request.getSession().setAttribute("cart", cart);
		}
		request.getSession().setAttribute("cartTotal", UtilityService.getTotalAmount(cart));
		response.sendRedirect("../customer/viewCart.jsp");
	}

	protected void addToCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		ArrayList<Product> products = (ArrayList<Product>) request.getSession().getAttribute("products");
		int productId = Integer.parseInt(request.getParameter("id"));
		Product product = UtilityService.getProductFromId(products, productId);
		try {
			ArrayList<Cart> cart = (ArrayList<Cart>) request.getSession().getAttribute("cart");
			if (cart == null) {
				cart = cartDAO.getCartElements(user.userId);
				request.getSession().setAttribute("cart", cart);
			}
			if (UtilityService.isProductExistInCart(productId, cart)) {
				throw new UserNotifyException("Product already exist.");
			} else {
				cartDAO.addToCart(user.userId, productId);
				cart.add(new Cart(product.productId, product.name, product.description, product.price,
						product.productStatus, product.productStatusId));
				request.getSession().setAttribute("cartTotal", UtilityService.getTotalAmount(cart));
				throw new UserNotifyException("Product added to cart successfully");
			}
		} catch (UserNotifyException e) {
			response.sendRedirect("../customer/viewProducts.jsp?viewProductsMessage=" + e.getMessage());
		}
	}

	protected void updateCartProducts(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		ArrayList<Cart> cart = (ArrayList<Cart>) request.getSession().getAttribute("cart");
		int productId = Integer.parseInt(request.getParameter("productId"));
		int productQuantity = Integer.parseInt(request.getParameter("productQuantity"));
		Cart cartElement = UtilityService.getProductFromId(cart, productId);
		cartElement.setProductQuantity(productQuantity);
		cartDAO.updateProductQuantityInCart(productId, user.userId, productQuantity);
		request.getSession().setAttribute("cartTotal", UtilityService.getTotalAmount(cart));
		response.sendRedirect("../customer/viewCart.jsp");
	}

	protected void removeFromCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		ArrayList<Cart> cart = (ArrayList<Cart>) request.getSession().getAttribute("cart");
		int productId = Integer.parseInt(request.getParameter("id"));
		UtilityService.removeProductFromId(cart, productId);
		cartDAO.removeProductInCart(productId, user.userId);
		request.getSession().setAttribute("cartTotal", UtilityService.getTotalAmount(cart));
		response.sendRedirect("../customer/viewCart.jsp");
	}

}
