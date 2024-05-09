package com.expensecalculator.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import com.expensecalculator.dto.Transaction;
import com.expensecalculator.service.InputValidationService;
import com.expensecalculator.service.MappingService;

public class TransactionDao extends CategoryDao{
	public ArrayList<Transaction> getTransaction(int userId, int transactionTypeId) throws ClassNotFoundException, SQLException {
		String sql = "SELECT t.* , tc.*, a.status as autoAdderStatus FROM transaction t join transaction_category tc on t.categoryid=tc.transaction_category_id "
				+ " join auto_adder_status a on a.auto_adder_status_id=t.auto_adder_status_id WHERE t.userid=? and t.transaction_type_id=? order by t.datetime";
		try (Connection connection = DatabaseConnectionDAO.getConnection(); PreparedStatement st = connection.prepareStatement(sql)) {
			st.setInt(1, userId);
			st.setInt(2, transactionTypeId);
			ResultSet rs = st.executeQuery();
			return MappingService.mapToTransactionListWithAutoAdderStatus(rs);
		}

	}
	
	public Transaction getTransactionFromTransactionId(int transactionId) throws ClassNotFoundException, SQLException {
		String sql = "SELECT * FROM transaction WHERE transactionid=?";
		try (Connection connection = DatabaseConnectionDAO.getConnection(); PreparedStatement st = connection.prepareStatement(sql)) {
			st.setInt(1, transactionId);
			ResultSet rs = st.executeQuery();
			return MappingService.mapToTransaction(rs);
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
	
	public String getFilteredTransactionByMonth(int userId) throws ClassNotFoundException, SQLException {
		String sql = "SELECT t.transaction_type_id , tc.category, sum(t.amount) as total_amount FROM transaction t join transaction_category tc "
				+ "on t.categoryid=tc.transaction_category_id WHERE t.userid=? and month(t.datetime)=month(current_date()) and year(t.datetime)=year(current_date()) group by t.categoryid";
		try (Connection connection = DatabaseConnectionDAO.getConnection(); PreparedStatement st = connection.prepareStatement(sql)) {
			st.setInt(1, userId);
			ResultSet rs = st.executeQuery();
			return MappingService.mapToGroupCategoryJson(rs);
		}

	}
	
	public ArrayList<Transaction> getAutoAdderFromUserId(int userId) throws ClassNotFoundException, SQLException {
		String sql = "SELECT * FROM transaction WHERE userid=? and auto_adder_status_id=1 and next_add_date <= CURRENT_TIMESTAMP";
		try (Connection connection = DatabaseConnectionDAO.getConnection(); PreparedStatement st = connection.prepareStatement(sql)) {
			st.setInt(1, userId);
			ResultSet rs= st.executeQuery();
			return MappingService.mapToRepeaterTransactionList(rs);
		}

	}
	
	
	
	public void updateRepeater(int transactionId, Timestamp datetime) throws ClassNotFoundException, SQLException {
		String updateQuery = "update auto_adder set next_add_date=?  where transaction_id=?";
		try (Connection connection = DatabaseConnectionDAO.getConnection();
				PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
			updateStatement.setTimestamp(1, datetime);
			updateStatement.setInt(2, transactionId);
			updateStatement.executeUpdate();
		}
	}
	
	public void removeRepeater(int transactionId) throws ClassNotFoundException, SQLException {
		String deleteQuery = "delete from auto_adder where transaction_id=?";
		String updateQuery = "update transaction set auto_adder_status_id=?  where transactionid=?";
		try (Connection connection = DatabaseConnectionDAO.getConnection(); PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
				PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
			deleteStatement.setInt(1, transactionId);
			updateStatement.setInt(1, 2);
			updateStatement.setInt(2, transactionId);
			deleteStatement.executeUpdate();
			updateStatement.executeUpdate();
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
	
	public HashMap<Integer, String> getAutoAdderCategory() throws ClassNotFoundException, SQLException {
		String sql = "SELECT *  FROM auto_adder_category";
		try (Connection connection = DatabaseConnectionDAO.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
			ResultSet rs = statement.executeQuery();
			return MappingService.mapToAutoAdderCategory(rs);
		}

	}
	
	public int addTransaction(Transaction transaction) throws ClassNotFoundException, SQLException {
		String sql = "INSERT INTO transaction (amount, note, userid, datetime, categoryid, transaction_type_id, auto_adder_status_id) VALUES (?, ?, ?,?, ?, ?,?)";
		int generatedId = -1;
		try (Connection connection = DatabaseConnectionDAO.getConnection(); PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			statement.setDouble(1, transaction.amount);
			statement.setString(2, transaction.note);
			statement.setInt(3, transaction.userId);
			statement.setTimestamp(4, Timestamp.valueOf(transaction.datetime));
			statement.setInt(5, transaction.categoryId);
			statement.setInt(6, transaction.typeId);
			statement.setInt(7, transaction.autoAdderStatus);
			statement.executeUpdate();
			
			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	                generatedId = generatedKeys.getInt(1);
	            }
	        }
		}
		return generatedId;
	}
	
	public void addTransactionWithAutoAdder(Transaction transaction) throws ClassNotFoundException, SQLException {
		String sql = "INSERT INTO transaction (amount, note, userid, datetime, categoryid, transaction_type_id, auto_adder_status_id, next_add_date, repeat_count, auto_adder_category_id) "
				+ "VALUES (?, ?, ?,?, ?, ?,?,?,?,?)";
		try (Connection connection = DatabaseConnectionDAO.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setDouble(1, transaction.amount);
			statement.setString(2, transaction.note);
			statement.setInt(3, transaction.userId);
			statement.setTimestamp(4, Timestamp.valueOf(transaction.datetime));
			statement.setInt(5, transaction.categoryId);
			statement.setInt(6, transaction.typeId);
			statement.setInt(7, transaction.autoAdderStatus);
			statement.setTimestamp(8, transaction.nextAddDateTimestamp);
			statement.setInt(9, transaction.count);
			statement.setInt(10, transaction.autoAdderCategoryId);
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
		String sql = "update transaction set amount=?, note=?, datetime=?, categoryid=?, next_add_date=?, repeat_count=?, auto_adder_category_id=?, auto_adder_status_id=?  where transactionid=?";
		try (Connection connection = DatabaseConnectionDAO.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setDouble(1, transaction.amount);
			statement.setString(2, transaction.note);
			statement.setTimestamp(3, InputValidationService.getTimestamp(transaction.datetime) );
			statement.setInt(4, transaction.categoryId);
			statement.setTimestamp(5, transaction.nextAddDateTimestamp);
			statement.setInt(6, transaction.count);
			statement.setInt(7, transaction.autoAdderCategoryId);
			statement.setInt(8, transaction.autoAdderStatus);
			statement.setInt(9, transaction.transactionId);
			int rowsAffected = statement.executeUpdate();
	        return rowsAffected > 0;
		}

	}
	
	public void updateRepeaterInTransaction(int transactionId, Timestamp nextDateTime) throws ClassNotFoundException, SQLException {
		String sql = "update transaction set next_add_date=? where transactionid=?";
		try (Connection connection = DatabaseConnectionDAO.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setTimestamp(1, nextDateTime);
			statement.setInt(2, transactionId);
			statement.executeUpdate();
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
