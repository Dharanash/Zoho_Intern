package com.expensecalculator.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.expensecalculator.dao.UserDao;
import com.expensecalculator.dto.User;
import com.expensecalculator.enums.ResponseStatus;
import com.expensecalculator.service.MappingService;
import com.google.gson.JsonObject;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
public class LoginController extends ActionSupport {
	private UserDao userDao;
	public LoginController() {
		userDao=new UserDao();
	}
	
	private String useremail, password;
	
	public String getUseremail() {
		return useremail;
	}

	@RequiredStringValidator(message = "Email is reqired")
	@EmailValidator(message = "Email is not in proper format")
	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}

	public String getPassword() {
		return password;
	}
	
	@RequiredStringValidator(message = "Password is required")
	public void setPassword(String password) {
		this.password = password;
	}

	public void login() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String email = request.getParameter("email");
		String password = request.getParameter("password");
 		User user = userDao.checkUser(email, password);
		HttpServletResponse response = ServletActionContext.getResponse();
		if (user == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write(ResponseStatus.Failure.toString());
			return;
		}
		
		String token = generateJWT(user.getUserId(), user.getRoleId());
        
        ServletActionContext.getResponse().addHeader("Authorization", token);
		String jsonObject= MappingService.mapToJson(user);
		response.getWriter().write(jsonObject.toString());
		return;
    }
	
	
	 private String generateJWT(int userId, int userRoleId) {
	        String secret = "secret";
	        
	        Algorithm algorithm = Algorithm.HMAC256(secret);
	        String token = JWT.create()
	                .withClaim("userId", userId)
	                .withClaim("userRoleId", userRoleId)
	                .sign(algorithm);
	        
	        return token;
	    }
	
	public String showLogin() throws Exception {
		return SUCCESS;
	}
	
}
