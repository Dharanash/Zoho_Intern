package com.expensecalculator.service;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.json.annotations.JSON;

import com.expensecalculator.dto.AutoAdderTransaction;
import com.expensecalculator.dto.Category;
import com.expensecalculator.dto.Transaction;
import com.expensecalculator.dto.User;
import com.expensecalculator.enums.Role;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class MappingService {
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	public static User mapToUser(ResultSet result) throws SQLException {
		User user = null;
		int userid = result.getInt("userid");
		String name = result.getString("name");
		int roleId = result.getInt("roleid");
		user = new User(userid, name, roleId);

		return user;
	}

	public static ArrayList<Transaction> mapToTransactionList(ResultSet result) throws SQLException {
		ArrayList<Transaction> transactions = new ArrayList<>();
		while (result.next()) {
			int transactionId = result.getInt("transactionid");
			int userId = result.getInt("userid");
			String note = result.getString("note");
			double amount = result.getDouble("amount");
			int categoryId = result.getInt("categoryid");
			int typeId = result.getInt("transaction_type_id");
			String category = result.getString("category");
			Timestamp datetime = result.getTimestamp("datetime");
			int autoAdderStatusId = result.getInt("auto_adder_status_id");
			transactions.add(
					new Transaction(transactionId, userId, amount , note, datetime.toString(), categoryId,typeId, category, autoAdderStatusId));
		}
		
		return transactions;
	}
	
	public static ArrayList<AutoAdderTransaction> mapToAutoAdderTransactionList(ResultSet result) throws SQLException {
		ArrayList<AutoAdderTransaction> transactions = new ArrayList<>();
		while (result.next()) {
			int transactionId = result.getInt("transactionid");
			int userId = result.getInt("userid");
			String note = result.getString("note");
			double amount = result.getDouble("amount");
			int categoryId = result.getInt("categoryid");
			int typeId = result.getInt("transaction_type_id");
			Timestamp datetime = result.getTimestamp("datetime");
			int autoAdderStatusId = result.getInt("auto_adder_status_id");
			Timestamp ndatetime = result.getTimestamp("next_add_date");
			transactions.add(
					new AutoAdderTransaction(transactionId, userId, amount , note, datetime.toString(), categoryId,typeId, autoAdderStatusId, ndatetime));
		}
		
		return transactions;
	}
	
	public static String mapToGroupCategoryJson(ResultSet result) throws SQLException {
		JsonArray jsonArray = new JsonArray();
		while (result.next()) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("typeId", result.getInt("transaction_type_id"));
			jsonObject.addProperty("category", result.getString("category"));
			jsonObject.addProperty("amount", result.getInt("total_amount"));
			jsonArray.add(jsonObject);
		}
		
		return jsonArray.toString();
	}
	
	public static ArrayList<Category> mapToCategoryList(ResultSet result) throws SQLException {
		ArrayList<Category> categories = new ArrayList<>();
		while (result.next()) {
			int categoryId = result.getInt("transaction_category_id");
			String category = result.getString("category");
			int typeId = result.getInt("transaction_type_id");
			String type = result.getString("type");
			Timestamp date = result.getTimestamp("adddate");
			categories.add(
					new Category(categoryId, category, typeId , type, date));
		}
		
		return categories;
	}

	public static HashMap<Integer, String> mapToTransactionCategory(ResultSet result) throws SQLException {
		HashMap<Integer, String> map = new HashMap<>();
		while (result.next()) {
			int categoryId = result.getInt("transaction_category_id");
			String category = result.getString("category");
			map.put(categoryId, category);
		}
		
		return map;
		
	}
	
	public static <E> String mapToJson(ArrayList<E> object) {
		Gson gson = new Gson();
		return gson.toJson(object);
	}
	
	public static String mapToJson(HashMap<Integer, String> map) {
		JsonObject jsonObject = new JsonObject();
		for(Map.Entry<Integer, String> m: map.entrySet()) {
			jsonObject.addProperty(m.getKey().toString(), m.getValue());
		}
		System.out.println(jsonObject);
		return jsonObject.toString();
	}
	
	public static String mapToJson(Object object) {
		Gson gson = new Gson();
		return gson.toJson(object);
	}
	
}