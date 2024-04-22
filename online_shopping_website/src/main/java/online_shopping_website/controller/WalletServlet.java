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

/**
 * Servlet implementation class WalletServlet
 */
@WebServlet("/WalletServlet/*")
public class WalletServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private WalletDAO walletDAO;

	public WalletServlet() {
		super();
		walletDAO = new WalletDAO();
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getPathInfo();
		try {
			switch (action) {
			case "/getWallet":
				getWallet(request, response);
				break;
			case "/addAmount":
				addAmountToWallet(request, response);
				break;
			case "/redeemPoints":
				addPointsToWallet(request, response);
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
		if(request.getSession().getAttribute("wallet")==null) {
		Wallet wallet = walletDAO.getWalletFromId(user.userId);
		request.getSession().setAttribute("wallet", wallet);}
		response.sendRedirect("../customer/wallet.jsp");
	}
	
	protected void addAmountToWallet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		Wallet wallet =(Wallet) request.getSession().getAttribute("wallet");
		double amount = Double.parseDouble( request.getParameter("amount"));
		wallet.addAmountToWalletBalance(amount);
		walletDAO.updateWallet(wallet);
		response.sendRedirect("../customer/wallet.jsp");
	}
	
	protected void addPointsToWallet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		Wallet wallet =(Wallet) request.getSession().getAttribute("wallet");
		wallet.redeemToWalletBalance();
		walletDAO.updateWallet(wallet);
		response.sendRedirect("../customer/wallet.jsp");
	}

}
