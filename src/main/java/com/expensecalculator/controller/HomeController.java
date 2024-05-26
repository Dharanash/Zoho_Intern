package com.expensecalculator.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.expensecalculator.dao.UserDao;
import com.expensecalculator.dto.User;
import com.expensecalculator.service.MappingService;
import com.opensymphony.xwork2.ActionSupport;

public class HomeController extends ActionSupport {
	private UserDao userDao;
	public HomeController() {
		userDao=new UserDao();
	}
	public String showUserHome() throws Exception {
		return SUCCESS;
	}
	
	public String showAdminHome() throws Exception {
		return SUCCESS;
	}
	
	public String showAnalysis() throws Exception {
		return SUCCESS;
	}
	
	public String logout() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request= ServletActionContext.getRequest();
		Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    break;
                }
            }
        }
		return SUCCESS;
	}
	
	public String showExpense() {
		return SUCCESS;
	}
	
	public String showIncome() {
		return SUCCESS;
	}
	
	public String showUsers() {
		return SUCCESS;
	}
	
	public String showCategory() {
		return SUCCESS;
	}
	
	public void getUsers() throws IOException, ClassNotFoundException, SQLException {
		
		String json = userDao.getUsers();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().write(json);
	}
	
	
}
