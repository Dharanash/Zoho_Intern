package com.expensecalculator.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import com.expensecalculator.dto.Transaction;
import com.expensecalculator.service.InputValidationService;
import com.expensecalculator.service.MappingService;

public class TransactionDao extends CategoryDao{
	public ArrayList<Transaction> getTransaction(int userId, int transactionTypeId) throws ClassNotFoundException, SQLException {
		String sql = "SELECT t.* , tc.* FROM transaction t join transaction_category tc on t.categoryid=tc.transaction_category_id WHERE t.userid=? and t.transaction_type_id=?";
		try (Connection connection = DatabaseConnectionDAO.getConnection(); PreparedStatement st = connection.prepareStatement(sql)) {
			st.setInt(1, userId);
			st.setInt(2, transactionTypeId);
			ResultSet rs = st.executeQuery();
			return MappingService.mapToTransactionList(rs);
		}

	}
	
	public ArrayList<Transaction> getFilteredTransaction(int userId, Timestamp sdate, Timestamp edate) throws ClassNotFoundException, SQLException {
		String sql = "SELECT t.* , tc.* FROM transaction t join transaction_category tc on t.categoryid=tc.transaction_category_id WHERE t.userid=? and t.datetime>=? and t.datetime<=? ";
		try (Connection connection = DatabaseConnectionDAO.getConnection(); PreparedStatement st = connection.prepareStatement(sql)) {
			st.setInt(1, userId);
			st.setTimestamp(2, sdate);
			st.setTimestamp(3, edate);
			ResultSet rs = st.executeQuery();
			return MappingService.mapToTransactionList(rs);
		}

	}
	
	public ArrayList<Transaction> getFilteredTransactionFromTypeId(int userId, Timestamp sdate, Timestamp edate, int typeId) throws ClassNotFoundException, SQLException {
		String sql = "SELECT t.* , tc.* FROM transaction t join transaction_category tc on t.categoryid=tc.transaction_category_id WHERE t.transaction_type_id=? and t.userid=? and t.datetime>=? and t.datetime<=? ";
		try (Connection connection = DatabaseConnectionDAO.getConnection(); PreparedStatement st = connection.prepareStatement(sql)) {
			st.setInt(1, typeId);
			st.setInt(2, userId);
			st.setTimestamp(3, sdate);
			st.setTimestamp(4, edate);
			ResultSet rs = st.executeQuery();
			return MappingService.mapToTransactionList(rs);
		}

	}
	
	public ArrayList<Transaction> getFilteredTransactionFromCategoryId(int userId, Timestamp sdate, Timestamp edate, int categoryId) throws ClassNotFoundException, SQLException {
		String sql = "SELECT t.* , tc.* FROM transaction t join transaction_category tc on t.categoryid=tc.transaction_category_id and t.categoryid=? WHERE t.userid=? and t.datetime>=? and t.datetime<=? ";
		try (Connection connection = DatabaseConnectionDAO.getConnection(); PreparedStatement st = connection.prepareStatement(sql)) {
			st.setInt(1, categoryId);
			st.setInt(2, userId);
			st.setTimestamp(3, sdate);
			st.setTimestamp(4, edate);
			ResultSet rs = st.executeQuery();
			return MappingService.mapToTransactionList(rs);
		}

	}
	
	public HashMap<Integer, String> getTransactionCategory(int userId, int typeId) throws ClassNotFoundException, SQLException {
		String sql = "SELECT *  FROM transaction_category where userid in (1,?) and transaction_type_id=? ";
		try (Connection connection = DatabaseConnectionDAO.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, userId);
			statement.setInt(2, typeId);
			ResultSet rs = statement.executeQuery();
			return MappingService.mapToTransactionCategory(rs);
		}

	}
	
	public void addTransaction(Transaction transaction) throws ClassNotFoundException, SQLException {
		String sql = "INSERT INTO transaction (amount, note, userid, datetime, categoryid, transaction_type_id) VALUES (?, ?, ?, ?, ?,?)";
		try (Connection connection = DatabaseConnectionDAO.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setDouble(1, transaction.amount);
			statement.setString(2, transaction.note);
			statement.setInt(3, transaction.userId);
			statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
			statement.setInt(5, transaction.categoryId);
			statement.setInt(6, transaction.typeId);
			statement.executeUpdate();
		}

	}
	
	public boolean isValidCategory(int userId, int categoryId, int typeId) throws ClassNotFoundException, SQLException {
	    String sql = "select * from transaction_category where transaction_category_id=? and transaction_type_id=? and userid in (1,?) ";
	    try (Connection connection = DatabaseConnectionDAO.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
	        statement.setInt(1, categoryId);
	        statement.setInt(2, typeId);
	        statement.setInt(3, userId);
	        try (ResultSet resultSet = statement.executeQuery()) {
	            return resultSet.next();
	        }
	    }
	}

	
	public int addTransactionCategory(String category, int userId, int transactionTypeId) throws ClassNotFoundException, SQLException {
	    String sql = "INSERT INTO transaction_category (category, adddate, userid, transaction_type_id) VALUES (?, ?, ?, ?)";
	    int generatedId = -1;
	    try (Connection connection = DatabaseConnectionDAO.getConnection(); PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
	        statement.setString(1, category.trim().toLowerCase());
	        statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
	        statement.setInt(3, userId);
	        statement.setInt(4, transactionTypeId);
	        statement.executeUpdate();
	        
	        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	                generatedId = generatedKeys.getInt(1);
	            }
	        }
	    }
	    return generatedId;
	}
	
	public boolean updateTransaction(Transaction transaction) throws ClassNotFoundException, SQLException {
		String sql = "update transaction set amount=?, note=?, datetime=?, categoryid=?  where transactionid=?";
		try (Connection connection = DatabaseConnectionDAO.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setDouble(1, transaction.amount);
			statement.setString(2, transaction.note);
			statement.setTimestamp(3, InputValidationService.getTimestamp(transaction.date, transaction.time) );
			statement.setInt(4, transaction.categoryId);
			statement.setInt(5, transaction.transactionId);
			int rowsAffected = statement.executeUpdate();
	        return rowsAffected > 0;
		}

	}
	
	public boolean removeTransaction(int transactionId) throws ClassNotFoundException, SQLException {
		String sql = "delete from transaction  where transactionid=?";
		try (Connection connection = DatabaseConnectionDAO.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, transactionId);
			int rowsAffected = statement.executeUpdate();
	        return rowsAffected > 0;
		}
	}
	
}
