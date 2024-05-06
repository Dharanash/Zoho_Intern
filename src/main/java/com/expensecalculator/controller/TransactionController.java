package com.expensecalculator.controller;

import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.expensecalculator.dao.TransactionDao;
import com.expensecalculator.dao.UserDao;
import com.expensecalculator.dto.Transaction;
import com.expensecalculator.dto.User;
import com.expensecalculator.enums.ResponseStatus;
import com.expensecalculator.service.InputValidationService;
import com.expensecalculator.service.MappingService;
import com.google.gson.JsonObject;
import com.opensymphony.xwork2.ActionSupport;

public class TransactionController extends ActionSupport {
	private TransactionDao transactionDao;

	public TransactionController() {
		transactionDao = new TransactionDao();
	}

	public void getTransactions() throws Exception {

		HttpServletRequest request = ServletActionContext.getRequest();
		int userId = Integer.parseInt(request.getParameter("userId"));
		int transactionTypeId = Integer.parseInt(request.getParameter("transactionTypeId"));
		String json = MappingService.mapToJson(transactionDao.getTransaction(userId, transactionTypeId));
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json");
		System.out.println(json);
		response.getWriter().write(json);
	}

	public void getFilteredTransactions() throws Exception {

		HttpServletRequest request = ServletActionContext.getRequest();
		int userId = Integer.parseInt(request.getParameter("userId"));
		Timestamp sdate = Timestamp.valueOf(request.getParameter("sdate")+" 00:00:00");
		Timestamp edate = Timestamp.valueOf(request.getParameter("edate")+" 23:59:00");
		int transationCategoryId = Integer.parseInt(request.getParameter("categoryId"));
		int transationTypeId = Integer.parseInt(request.getParameter("transactionTypeId"));
		String json = "";
		if(transationTypeId==0) {
			json = MappingService
					.mapToJson(transactionDao.getFilteredTransaction(userId, sdate, edate));
		}
		else if (transationCategoryId == 0) {
			json = MappingService
					.mapToJson(transactionDao.getFilteredTransactionFromTypeId(userId, sdate, edate, transationTypeId));
		} else {
			json = MappingService.mapToJson(transactionDao.getFilteredTransactionFromCategoryId(userId,sdate, edate, transationCategoryId));
		}

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json");
		System.out.println(json);
		response.getWriter().write(json);
	}

	public void getTransactionCategory() throws Exception {

		HttpServletRequest request = ServletActionContext.getRequest();
		int userId = Integer.parseInt(request.getParameter("userId"));
		int transactionTypeId = Integer.parseInt(request.getParameter("transactionTypeId"));
		String json = MappingService.mapToJson(transactionDao.getTransactionCategory(userId, transactionTypeId));
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json");
		System.out.println(json);
		response.getWriter().write(json);
	}

	public void addTransaction() throws Exception {

		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		Transaction transaction = null;
		try {

			int userId = Integer.parseInt(request.getParameter("userId"));
			int transactionTypeId = Integer.parseInt(request.getParameter("transactionTypeId"));
			String notes = request.getParameter("note");
			double amount = Double.parseDouble(request.getParameter("amount"));
			int categoryId = Integer.parseInt(request.getParameter("categoryId"));
			if (categoryId==0) {
				
				String category = request.getParameter("category");
				if(transactionDao.isCategoryExist(userId, category, transactionTypeId)) {
					response.setStatus(HttpServletResponse.SC_CONFLICT);
					response.getWriter().write(ResponseStatus.Failure.toString());
					return;
				}
				int customCategoryId = transactionDao.addTransactionCategory(category, userId, transactionTypeId);
				transaction = new Transaction(userId, amount, notes, customCategoryId, transactionTypeId);
			} else {
				if (!transactionDao.isValidCategory(userId, categoryId, transactionTypeId)) {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().write(ResponseStatus.Failure.toString());
					return;
				}
				transaction = new Transaction(userId, amount, notes, categoryId, transactionTypeId);
			}

			transactionDao.addTransaction(transaction);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write(ResponseStatus.Error.toString());
			throw e;
		}
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write(ResponseStatus.Success.toString());
	}

	public void updateTransaction() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		int transactionId = Integer.parseInt(request.getParameter("transactionId"));
		int transactionTypeId = Integer.parseInt(request.getParameter("transactionTypeId"));
		int userId = Integer.parseInt(request.getParameter("userId"));
		String notes = request.getParameter("note");
		double amount = Double.parseDouble(request.getParameter("amount"));
		String time = request.getParameter("time");
		int categoryId = Integer.parseInt(request.getParameter("categoryId"));
		String date = request.getParameter("date");
		Transaction transaction = new Transaction(transactionId, userId, amount, notes,date, time, categoryId,
				transactionTypeId);
		if(!transactionDao.updateTransaction(transaction)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write(ResponseStatus.Failure.toString());
			return;
		}
		response.getWriter().write(ResponseStatus.Success.toString());
	}

	public void removeTransaction() throws Exception {

		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		int transactionId = 0;
		try {
			transactionId = Integer.parseInt(request.getParameter("transactionId"));
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write(ResponseStatus.Error.toString());
			throw e;
		}

		try {
			if (!transactionDao.removeTransaction(transactionId)) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(ResponseStatus.Failure.toString());
				return;
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write(ResponseStatus.Error.toString());
			throw e;
		}
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write(ResponseStatus.Success.toString());
	}
}
