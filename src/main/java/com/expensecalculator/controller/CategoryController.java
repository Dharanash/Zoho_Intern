package com.expensecalculator.controller;

import java.sql.Date;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.expensecalculator.dao.CategoryDao;
import com.expensecalculator.dto.Transaction;
import com.expensecalculator.enums.ResponseStatus;
import com.expensecalculator.service.InputValidationService;
import com.expensecalculator.service.MappingService;
import com.opensymphony.xwork2.ActionSupport;

public class CategoryController extends ActionSupport {
	
	private CategoryDao categoryDao;
	public CategoryController() {
		categoryDao=new CategoryDao();
	}
	
	public void getCategories() throws Exception {

		HttpServletRequest request = ServletActionContext.getRequest();
		int userId = Integer.parseInt(request.getParameter("userId"));
		String json = MappingService.mapToJson(categoryDao.getCategories(userId));
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json");
		System.out.println(json);
		response.getWriter().write(json);
	}

	
	public void addCategory() throws Exception {

		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
		int userId = Integer.parseInt(request.getParameter("userId"));
		int transactionTypeId = Integer.parseInt(request.getParameter("type"));
		String category = request.getParameter("category");
		categoryDao.addCategory(category, userId, transactionTypeId);	
		}
		catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			response.getWriter().write(ResponseStatus.Failure.toString());
			System.out.println(e);
			return;
		}
		catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write(ResponseStatus.Error.toString());
			System.out.println(e);
			return;
		}
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write(ResponseStatus.Success.toString());
	}
	
	public void updateCategory() throws Exception {

		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
		int userId = Integer.parseInt(request.getParameter("userId"));
		String category = request.getParameter("category");
		int categoryId = Integer.parseInt(request.getParameter("categoryId"));
		if(!categoryDao.updateCategory(category, categoryId, userId)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write(ResponseStatus.Failure.toString());
			return;
		}
		}
		catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			response.getWriter().write(ResponseStatus.Failure.toString());
			System.out.println(e);
			return;
		}
		catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write(ResponseStatus.Error.toString());
			throw e;
		}
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write(ResponseStatus.Success.toString());
	}
}
