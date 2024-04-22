package online_shopping_website.servlets;

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
import online_shopping_website.enums.Role;
import online_shopping_website.model.*;
import online_shopping_website.services.UtilityService;

/**
 * Servlet implementation class InventoryServlet
 */
@WebServlet("/InventoryServlet/*")
public class InventoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private InventoryDAO inventoryDAO;

    public InventoryServlet() {
        super();
        inventoryDAO=new InventoryDAO();
    }

    protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getPathInfo();
		try {
			switch (action) {
			case "/getInventory":
				getInventory(request, response);
				break;
			case "/postAddProduct":
				postAddProduct(request, response);
				break;
			case "/deleteProduct":
				removeProduct(request, response);
				break;
			case "/preUpdateProduct":
				preUpdate(request, response);
				break;
			case "/postUpdateProduct":
				postUpdate(request, response);
				break;
			default:
				break;
			}
		} catch (SQLException | ClassNotFoundException ex) {
			throw new ServletException(ex);
		}
	}
   
	protected void getInventory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User)request.getSession().getAttribute("auth");
			if(request.getSession().getAttribute("products")==null) {
				request.getSession().setAttribute("products", inventoryDAO.getInventory(user.userId, user.role));
			}
			if(request.getSession().getAttribute("productStatus")==null) {
				request.getSession().setAttribute("productStatus", inventoryDAO.getProductStatus());
			}
			
			if(user.role==Role.Customer) {
				response.sendRedirect("../customer/viewProducts.jsp");
			}
			else {
				response.sendRedirect("../admin/viewInventory.jsp");
			}
	}
	
	protected void postAddProduct(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
    	User user = (User)request.getSession().getAttribute("auth");
    	
		String pname = request.getParameter("pname");
		String description = request.getParameter("description");
		double price = Double.parseDouble(request.getParameter("price"));
		int productStatusId = Integer.parseInt( request.getParameter("selectedStatus"));
		DetailedProduct product = new DetailedProduct(pname, description,price, user.email, productStatusId);
		ArrayList<DetailedProduct> products = (ArrayList<DetailedProduct>)request.getSession().getAttribute("products");
			int productId =inventoryDAO.addProduct(product);
			product.setProductId(productId);
			products.add(product);
			String message= "Product added successfully";
			response.sendRedirect("../admin/addProduct.jsp?addProductSuccessMessage="+message);
	}
	
	protected void preUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ClassNotFoundException, SQLException {
		if(request.getSession().getAttribute("productStatus")==null) {
			request.getSession().setAttribute("productStatus", inventoryDAO.getProductStatus());
		}
		ArrayList<DetailedProduct> products = (ArrayList<DetailedProduct>)request.getSession().getAttribute("products");
		int productId=Integer.parseInt( request.getParameter("id"));
		Product product = UtilityService.getProductFromId(products, productId);
		request.getSession().setAttribute("product", product);
		response.sendRedirect("../admin/updateProduct.jsp");
	}
	
	protected void postUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user =(User) request.getSession().getAttribute("auth");
		HashMap<Integer, String> productStatus = (HashMap<Integer, String>) request.getSession().getAttribute("productStatus");
		int productId =Integer.parseInt( request.getParameter("pid"));
		ArrayList<DetailedProduct> products = (ArrayList<DetailedProduct>)request.getSession().getAttribute("products");
		DetailedProduct product = UtilityService.getProductFromId(products, productId);
		product.setName(request.getParameter("pname"));
		product.setDescription(request.getParameter("description"));
		product.setPrice(Double.parseDouble(request.getParameter("price")));
		int productStatusId = Integer.parseInt(request.getParameter("selectedStatus"));
		product.setProductStatusId(productStatusId);
		product.setProductStatus(productStatus.get(productStatusId));
		product.setModefiedBy(user.email);
		product.setModifiedTime();
			inventoryDAO.updateProduct(productId, product);
			response.sendRedirect("../admin/viewInventory.jsp");
	}
	
	protected void removeProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user =(User) request.getSession().getAttribute("auth");
		int productId = Integer.parseInt( request.getParameter("id"));
		ArrayList<DetailedProduct> products = (ArrayList<DetailedProduct>)request.getSession().getAttribute("products");
		UtilityService.removeProductFromId(products, productId);
		inventoryDAO.removeProductFromInventory(user.userId, productId);
		response.sendRedirect("../admin/viewInventory.jsp");
	}

}
