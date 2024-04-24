package online_shopping_website.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import online_shopping_website.dao.WalletDAO;
import online_shopping_website.model.*;

@WebServlet("/wallet/*")
public class WalletServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private WalletDAO walletDAO;

	public WalletServlet() {
		super();
		walletDAO = new WalletDAO();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getPathInfo();
		try {
			switch (action) {
			case "/get":
				getWallet(request, response);
				break;
			case "/redeem":
				addPointsToWallet(request, response);
				break;
			default:
				break;
			}
		} catch (SQLException | ClassNotFoundException ex) {
			throw new ServletException(ex);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getPathInfo();
		try {
			switch (action) {
			case "/add":
				addAmountToWallet(request, response);
				break;
			default:
				break;
			}
		} catch (SQLException | ClassNotFoundException ex) {
			throw new ServletException(ex);
		}
	}

	protected void getWallet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		Wallet wallet = walletDAO.getWalletFromId(user.userId);
		request.setAttribute("wallet", wallet);
		request.getRequestDispatcher("../customer/wallet.jsp").forward(request, response);
	}
	
	protected void addAmountToWallet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		double amount = Double.parseDouble( request.getParameter("amount"));
		walletDAO.addToWalletAmount(user.userId, amount);
		response.sendRedirect("../wallet/get?successMessage=Amount added successfully.");
	}
	
	protected void addPointsToWallet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		walletDAO.redeemPointsToWalletAmount(user.userId);
		response.sendRedirect("../wallet/get?successMessage=points redeemed successfully.");
	}

}
