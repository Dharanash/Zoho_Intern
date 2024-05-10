package com.expensecalculator.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.expensecalculator.dao.TransactionDao;
import com.expensecalculator.dao.UserDao;
import com.expensecalculator.dto.Transaction;
import com.expensecalculator.enums.AutoAdderStatus;
import com.expensecalculator.enums.ResponseStatus;
import com.expensecalculator.enums.TransactionType;
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

	public void getTransactionFromTransactionId() throws Exception {

		HttpServletRequest request = ServletActionContext.getRequest();
		int transactionId = Integer.parseInt(request.getParameter("transactionId"));
		String json = MappingService.mapToJson(transactionDao.getTransactionFromTransactionId(transactionId));
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json");
		System.out.println(json);
		response.getWriter().write(json);
	}

	public void getFilteredTransactions() throws Exception {

		HttpServletRequest request = ServletActionContext.getRequest();
		int userId = Integer.parseInt(request.getParameter("userId"));
		Timestamp sdate = Timestamp.valueOf(request.getParameter("sdate") + " 00:00:00");
		Timestamp edate = Timestamp.valueOf(request.getParameter("edate") + " 23:59:00");
		int transationCategoryId = Integer.parseInt(request.getParameter("categoryId"));
		int transationTypeId = Integer.parseInt(request.getParameter("transactionTypeId"));
		String json = "";
		if (transationTypeId == 0) {
			json = MappingService.mapToJson(transactionDao.getFilteredTransaction(userId, sdate, edate));
		} else if (transationCategoryId == 0) {
			json = MappingService
					.mapToJson(transactionDao.getFilteredTransactionFromTypeId(userId, sdate, edate, transationTypeId));
		} else {
			json = MappingService.mapToJson(
					transactionDao.getFilteredTransactionFromCategoryId(userId, sdate, edate, transationCategoryId));
		}

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json");
		System.out.println(json);
		response.getWriter().write(json);
	}

	public void getFilteredTransactionsByMonth() throws Exception {

		HttpServletRequest request = ServletActionContext.getRequest();
		int userId = Integer.parseInt(request.getParameter("userId"));
		String json = transactionDao.getFilteredTransactionByMonth(userId);
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

	public void getAutoAdderCategory() throws Exception {
		String json = MappingService.mapToJson(transactionDao.getAutoAdderCategory());
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
			String dateTime = InputValidationService.getTimestamp(request.getParameter("datetime")).toString();
			int autoAdderStatusId = request.getParameter("autoAdder") == null ? AutoAdderStatus.Unchecked.getStatusId() : AutoAdderStatus.Checked.getStatusId();

			if (!transactionDao.isValidCategory(userId, categoryId, transactionTypeId)) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(ResponseStatus.Failure.toString());
				return;
			}

			if (autoAdderStatusId == AutoAdderStatus.Checked.getStatusId()) {
				int count = Integer.parseInt(request.getParameter("autoAdderCount"));
				int autoAdderCategoryId = Integer.parseInt(request.getParameter("autoAdderCategoryId"));
				Timestamp dataTimestamp = new Timestamp(
						InputValidationService.getNextTimestamp(dateTime, count, autoAdderCategoryId));
				transaction = new Transaction(userId, amount, notes, dateTime, categoryId, transactionTypeId,
						autoAdderStatusId, dataTimestamp, count, autoAdderCategoryId);
				transactionDao.addTransactionWithAutoAdder(transaction);
			} else {
				transaction = new Transaction(userId, amount, notes, dateTime, categoryId, transactionTypeId,
						autoAdderStatusId);
				transactionDao.addTransaction(transaction);
			}

		} catch (ClassNotFoundException | SQLException e) {
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
		try {
			int transactionId = Integer.parseInt(request.getParameter("transactionId"));
			int transactionTypeId = Integer.parseInt(request.getParameter("transactionTypeId"));
			int userId = Integer.parseInt(request.getParameter("userId"));
			String notes = request.getParameter("note");
			double amount = Double.parseDouble(request.getParameter("amount"));
			String datetime = request.getParameter("datetime");
			int categoryId = Integer.parseInt(request.getParameter("categoryId"));
			int autoAdderStatusId = Integer.parseInt(request.getParameter("autoAdderStatusId"));

			if (autoAdderStatusId == AutoAdderStatus.Checked.getStatusId()) {
				int count = Integer.parseInt(request.getParameter("autoAdderCount"));
				int autoAdderCategoryId = Integer.parseInt(request.getParameter("autoAdderCategoryId"));
				String dateTimeStr = InputValidationService.getTimestamp(datetime).toString();
				Timestamp nextTimestamp = new Timestamp(
						InputValidationService.getNextTimestamp(dateTimeStr, count, autoAdderCategoryId));
				Transaction transaction = new Transaction(transactionId, userId, amount, notes, datetime, categoryId,
						transactionTypeId, autoAdderStatusId, nextTimestamp, count, autoAdderCategoryId);
				transactionDao.updateTransaction(transaction);
			} else {
				Transaction transaction = new Transaction(transactionId, userId, amount, notes, datetime, categoryId,
						transactionTypeId, autoAdderStatusId);
				if (!transactionDao.updateTransaction(transaction)) {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().write(ResponseStatus.Failure.toString());
					return;
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write(ResponseStatus.Error.toString());
			throw e;
		}
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write(ResponseStatus.Success.toString());
	}

	public void removeTransaction() throws Exception {

		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		try {
			int transactionId = Integer.parseInt(request.getParameter("transactionId"));
			if (!transactionDao.removeTransaction(transactionId)) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(ResponseStatus.Failure.toString());
				return;
			}
		} catch (ClassNotFoundException | SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write(ResponseStatus.Error.toString());
			throw e;
		}
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write(ResponseStatus.Success.toString());
	}

	public void executeAutoAdderTransactions()
			throws ClassNotFoundException, SQLException, ParseException, IOException {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		int userId = Integer.parseInt(request.getParameter("userId"));

		int expenseCount = 0;
		int incomeCount = 0;

		try {
			ArrayList<Transaction> transactions = transactionDao.getAutoAdderFromUserId(userId);
			for (Transaction transaction : transactions) {
				String startDate = transaction.datetime;
				transaction.datetime = transaction.nextAddDateTimestamp.toString();
				transaction.nextAddDateTimestamp = new Timestamp(
						InputValidationService.getNextTimestamp(startDate, transaction.nextAddDateTimestamp.toString(),
								transaction.count, transaction.autoAdderCategoryId));
				transaction.autoAdderStatus = AutoAdderStatus.Repeated.getStatusId();
				transactionDao.addTransaction(transaction);
				transactionDao.updateRepeaterInTransaction(transaction.transactionId, transaction.nextAddDateTimestamp);
				
				Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
				while (transaction.nextAddDateTimestamp.before(currentTimestamp)) {
					transaction.datetime = transaction.nextAddDateTimestamp.toString();
					transaction.nextAddDateTimestamp = new Timestamp(InputValidationService.getNextTimestamp(startDate,
							transaction.nextAddDateTimestamp.toString(), transaction.count,
							transaction.autoAdderCategoryId));
					transactionDao.addTransaction(transaction);
					transactionDao.updateRepeaterInTransaction(transaction.transactionId, transaction.nextAddDateTimestamp);
					
					if (transaction.typeId == TransactionType.Expense.getTypeId()) {
						expenseCount++;
					} else if (transaction.typeId == TransactionType.Income.getTypeId()) {
						incomeCount++;
					}
				}

				if (transaction.typeId == TransactionType.Expense.getTypeId()) {
					expenseCount++;
				} else if (transaction.typeId == TransactionType.Income.getTypeId()) {
					incomeCount++;
				}
				
			}
		} catch (ClassNotFoundException | SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write(ResponseStatus.Error.toString());
			throw e;
		}

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("expenseCount", expenseCount);
		jsonObject.addProperty("incomeCount", incomeCount);

		response.setContentType("application/json");
		response.getWriter().write(jsonObject.toString());
	}
}
