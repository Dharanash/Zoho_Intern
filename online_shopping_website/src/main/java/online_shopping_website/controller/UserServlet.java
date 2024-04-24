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
import online_shopping_website.services.InputValidationService;

@WebServlet("/user/*")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO;

	public UserServlet() {
		super();
		userDAO = new UserDAO();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getPathInfo();
		switch (action) {
		case "/login":
			loadLogin(request, response);
			break;
		case "/register":
			loadRegister(request, response);
			break;
		case "/getprofile":
			getProfile(request, response);
			break;
		case "/home":
			loadHome(request, response);
			break;
		case "/getaddmanager":
			getAddManager(request, response);
			break;
		case "/logout":
			logout(request, response);
			break;
		default:
			break;
		}
	}

	protected void loadLogin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("../login.jsp").forward(request, response);
	}

	protected void loadRegister(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("../register.jsp").forward(request, response);
	}

	protected void loadHome(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("../User/index.jsp").forward(request, response);
	}

	protected void getAddManager(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("../admin/addManager.jsp").forward(request, response);
	}

	protected void getProfile(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("../User/profile.jsp").forward(request, response);
	}

	protected void logout(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}

		response.sendRedirect("../user/login");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
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
			case "/updateprofile":
				updateProfile(request, response);
				break;
			case "/addmanager":
				addManager(request, response);
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
		try {
			User user = userDAO.checkUser(request.getParameter("email"), request.getParameter("password"));
			if (user == null) {
				throw new UserNotifyException("Invalid Email / Password");
			} else {
				request.getSession().setAttribute("auth", user);
				response.sendRedirect("../user/home");
			}
		} catch (UserNotifyException e) {
			response.sendRedirect("../user/login?errorMessage=" + e.getMessage());
		}
	}

	protected void userRegister(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {

		try {
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			if (!InputValidationService.isValidPassword(password)) {
				throw new UserNotifyException("Invalid password format.");
			}
			String email = request.getParameter("email");
			if (!InputValidationService.isValidEmail(email)) {
				throw new UserNotifyException("Invalid email format.");
			}
			String phoneNumber = request.getParameter("phoneNumber");
			User user = new User(name, password, email, phoneNumber);

			if (userDAO.isUserExist(email)) {
				throw new UserNotifyException("Email already exist.");
			}
			userDAO.addUser(user);
			response.sendRedirect("../user/login?successMessage=Registered Sucessfully");
		} catch (UserNotifyException e) {
			response.sendRedirect("../user/register?errorMessage=" + e.getMessage());
		}
	}

	protected void addManager(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		try {
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			if (!InputValidationService.isValidPassword(password)) {
				throw new UserNotifyException("Invalid password format.");
			}
			String email = request.getParameter("email");
			if (!InputValidationService.isValidEmail(email)) {
				throw new UserNotifyException("Invalid email format.");
			}
			String phoneNumber = request.getParameter("phoneNumber");
			User user = new User(name, password, Role.Manager, email, phoneNumber);
			if (userDAO.isUserExist(email)) {
				throw new UserNotifyException("Email already exist.");
			}
			userDAO.addUser(user);
			response.sendRedirect("../user/getaddmanager?successMessage=Manager added successfully");
		} catch (UserNotifyException e) {
			response.sendRedirect("../user/getaddmanager?errorMessage=" + e.getMessage());
		}
	}

	protected void updateProfile(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException {
		User user = (User) request.getSession().getAttribute("auth");
		try {
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		if (!InputValidationService.isValidPassword(password)) {
			throw new UserNotifyException("Invalid password format.");
		}
		String phoneNumber = request.getParameter("phoneNumber");

		user.setName(name);
		user.setPassword(password);
		user.setPhoneNumber(phoneNumber);
		userDAO.updateUser(user);
		response.sendRedirect("../user/getprofile?successMessage=Profile updated succesfully.");
		}
		catch (UserNotifyException e) {
			response.sendRedirect("../user/getprofile?errorMessage=" + e.getMessage());
		}

	}

}
