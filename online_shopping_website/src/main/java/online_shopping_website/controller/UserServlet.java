package online_shopping_website.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import online_shopping_website.dao.UserDAO;
import online_shopping_website.enums.Role;
import online_shopping_website.exceptions.UserNotifyException;
import online_shopping_website.model.User;

@WebServlet("/UserServlet/*")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO;

	public UserServlet() {
		super();
		userDAO = new UserDAO();
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getPathInfo();
		try {
			switch (action) {
			case "/login":
				userLogin(request, response);
				break;
			case "/register":
				userRegister(request, response);
				break;
			case "/updateProfile":
				updateProfile(request, response);
				break;
			case "/addManager":
				addManager(request, response);
				break;
			case "/logout":
				logout(request, response);
				break;
			default:
				break;
			}
		} catch (SQLException | ClassNotFoundException ex) {
			throw new ServletException(ex);
		}
	}

	protected void userLogin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		response.setContentType("text/html;charset=UTF-8");
		try {
			User user = userDAO.checkUser(request.getParameter("email"), request.getParameter("password"));
			if (user == null) {
				throw new UserNotifyException("Invalid Email / Password");
			} else {
				request.getSession().setAttribute("auth", user);
				response.sendRedirect("../user/index.jsp");
			}
		} catch (UserNotifyException e) {
			response.sendRedirect("../login.jsp?loginErrorMessage=" + e.getMessage());
		}
	}

	protected void userRegister(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String phoneNumber = request.getParameter("phoneNumber");
		User user = new User(name, password, email, phoneNumber);
		UserDAO userDAO = new UserDAO();
		try {
			if (userDAO.isUserExist(email)) {
				throw new UserNotifyException("Email already exist.");
			}
			userDAO.addUser(user);
			response.sendRedirect("../login.jsp");
		} catch (UserNotifyException e) {
			response.sendRedirect("../register.jsp?registerErrorMessage=" + e.getMessage());
		}
	}

	protected void addManager(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String phoneNumber = request.getParameter("phoneNumber");
		User user = new User(name, password, online_shopping_website.enums.Role.Manager, email, phoneNumber);
		try {
			if (userDAO.isUserExist(email)) {
				throw new UserNotifyException("Email already exist.");
			}
			userDAO.addUser(user);
			String message = "Manager added successfully";
			response.sendRedirect("../admin/addManager.jsp?addManagerSuccessMessage=" + message);
		} catch (UserNotifyException e) {
			response.sendRedirect("../addManager.jsp?addManagerErrorMessage=" + e.getMessage());
		}
	}

	protected void updateProfile(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		String phoneNumber = request.getParameter("phoneNumber");

		user.setName(name);
		user.setPassword(password);
		user.setPhoneNumber(phoneNumber);
		userDAO.updateUser(user);
		response.sendRedirect("../user/profile.jsp?profileSuccessMessage=" + "Profile updated succesfully.");

	}

	protected void logout(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}

		response.sendRedirect("../login.jsp");
	}

}
