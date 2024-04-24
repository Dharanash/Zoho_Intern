package online_shopping_website.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import online_shopping_website.dao.InventoryDAO;
import online_shopping_website.enums.ProductStatus;
import online_shopping_website.enums.Role;
import online_shopping_website.model.*;
import online_shopping_website.services.UtilityService;

/**
 * Servlet implementation class InventoryServlet
 */
@WebServlet("/inventory/*")
public class InventoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private InventoryDAO inventoryDAO;

	public InventoryServlet() {
		super();
		inventoryDAO = new InventoryDAO();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getPathInfo();
		try {
			switch (action) {
			case "/get":
				getInventory(request, response);
				break;
			case "/addproduct":
				getAddProduct(request, response);
				break;
			case "/remove":
				removeProduct(request, response);
				break;
			case "/updateproduct":
				preUpdateProduct(request, response);
				break;
			default:
				break;
			}
		} catch (SQLException | ClassNotFoundException ex) {
			throw new ServletException(ex);
		}
	}

	protected void getInventory(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		request.setAttribute("products", inventoryDAO.getInventory(user.userId, user.role));

		if (user.role == Role.Customer) {
			request.setAttribute("viewProductsMessage", request.getParameter("viewProductsMessage"));
			request.getRequestDispatcher("../customer/viewProducts.jsp").forward(request, response);
		} else {
			request.getRequestDispatcher("../admin/viewInventory.jsp").forward(request, response);
		}
	}
	
	protected void getAddProduct(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		request.getRequestDispatcher("../admin/addProduct.jsp").forward(request, response);	
	}
	
	protected void preUpdateProduct(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		request.setAttribute("productStatus", inventoryDAO.getProductStatus());
		int productId = Integer.parseInt(request.getParameter("id"));
		DetailedProduct product = inventoryDAO.getProductFromId(productId, user.role);
		request.setAttribute("product", product);
		request.getRequestDispatcher("../admin/updateProduct.jsp").forward(request, response);
	}
	

	protected void removeProduct(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		int productId = Integer.parseInt(request.getParameter("id"));
		inventoryDAO.removeProductFromInventory(user.userId, productId);
		response.sendRedirect("../inventory/get?successMessage=Product removed successfully");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getPathInfo();
		try {
			switch (action) {
			case "/addproduct":
				postAddProduct(request, response);
				break;
			case "/updateproduct":
				postUpdate(request, response);
				break;
			default:
				break;
			}
		} catch (SQLException | ClassNotFoundException ex) {
			throw new ServletException(ex);
		}
	}

	protected void postAddProduct(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");

		String pname = request.getParameter("pname");
		String description = request.getParameter("description");
		double price = Double.parseDouble(request.getParameter("price"));
		int quantity = Integer.parseInt(request.getParameter("quantity"));
		DetailedProduct product = new DetailedProduct(pname, description, price,quantity, user.userId, ProductStatus.Available.getProductStatusId());
		inventoryDAO.addProduct(product);
		response.sendRedirect("../inventory/addproduct?successMessage=Product added successfully.");
	}

	protected void postUpdate(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		int productId = Integer.parseInt(request.getParameter("pid"));
		DetailedProduct product= inventoryDAO.getProductFromId(productId, user.role);
		product.setName(request.getParameter("pname"));
		product.setDescription(request.getParameter("description"));
		product.setPrice(Double.parseDouble(request.getParameter("price")));
		product.setQuantity(Integer.parseInt(request.getParameter("quantity")));
		product.setModifiedById(user.userId);
		product.setModifiedTime();
		inventoryDAO.updateProduct(productId, product);
		response.sendRedirect("../inventory/get?successMessage=Product updated successfully.");
	}

}
