package com.expensecalculator.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.expensecalculator.dao.UserDao;
import com.expensecalculator.dto.*;
import com.expensecalculator.redis.RedisConnection;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;

import redis.clients.jedis.Jedis;


public class RegisterController extends ActionSupport {
	private UserDao userDao;
	public RegisterController() {
		userDao=new UserDao();
	}
	
	private String name, email, password, phonenumber;

	public String getName() {
		return name;
	}

	@RequiredStringValidator(message = "Name is required")
	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	@RequiredStringValidator(message = "Email is required")
	@EmailValidator(message = "Email is not in proper format")
	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	@RequiredStringValidator(message = "Password is required")
	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	 @RequiredStringValidator(message = "Phone number is required")
	 @RegexFieldValidator(regex = "[0-9]{10}", message = "Phone number must be 10 digits")
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	
	 
	 public String execute() throws Exception {
		 HttpServletRequest request= ServletActionContext.getRequest() ;
				if (userDao.isUserExist(email)) {
					request.setAttribute("errorMessage", "Email already exist");
					return ERROR;
				}
				User user = new User(name, password, email, phonenumber);
				userDao.addUser(user);
				
				try(Jedis jedis= RedisConnection.getPool().getResource()){
					jedis.del("users");
				}
				System.out.println("registered");
				return SUCCESS;
	    }
	 
	 public String showRegister() {
		 return SUCCESS;
	 }

}
