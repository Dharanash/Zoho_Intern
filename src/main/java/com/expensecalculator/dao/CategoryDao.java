package com.expensecalculator.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.expensecalculator.dto.Category;
import com.expensecalculator.dto.Transaction;
import com.expensecalculator.service.MappingService;

public class CategoryDao {
	public ArrayList<Category> getCategories(int userId) throws ClassNotFoundException, SQLException {
		String sql = "SELECT c.* , t.*, u.roleid FROM transaction_category c join transaction_type t on t.transaction_type_id=c.transaction_type_id  "
				+ "join users u on c.userid=u.userid WHERE c.userid in (1,?)";
		try (Connection connection = DatabaseConnectionDAO.getConnection(); PreparedStatement st = connection.prepareStatement(sql)) {
			st.setInt(1, userId);
			ResultSet rs = st.executeQuery();
			return MappingService.mapToCategoryList(rs);
		}
	}
	
	public boolean isCategoryExist(int userId, String category, int typeId) throws ClassNotFoundException, SQLException {
	    String sql = "select * from transaction_category where category=? and transaction_type_id=? and userid in (1,?) ";
	    try (Connection connection = DatabaseConnectionDAO.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
	        statement.setString(1, category.trim().toLowerCase());
	        statement.setInt(2, typeId);
	        statement.setInt(3, userId);
	        try (ResultSet resultSet = statement.executeQuery()) {
	            return resultSet.next();
	        }
	    }
	}
	
	public void addCategory(String category, int userId, int transactionTypeId) throws ClassNotFoundException, SQLException {
	    String sql = "INSERT INTO transaction_category (category, adddate, userid, transaction_type_id) VALUES (?, ?, ?, ?)";
	    try (Connection connection = DatabaseConnectionDAO.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
	        statement.setString(1, category);
	        statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
	        statement.setInt(3, userId);
	        statement.setInt(4, transactionTypeId);
	        statement.executeUpdate();
	    }
	}
	
	public boolean updateCategory(String category, int categoryId, int userId) throws ClassNotFoundException, SQLException {
	    String sql = "update transaction_category set category=?, adddate=? where transaction_category_id=? and userid=? ";
	    try (Connection connection = DatabaseConnectionDAO.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
	        statement.setString(1, category);
	        statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
	        statement.setInt(3, categoryId);
	        statement.setInt(4, userId);
	        int rowsAffected = statement.executeUpdate();
	        System.out.println(rowsAffected);
	        return rowsAffected > 0;
	    }
	}
}
