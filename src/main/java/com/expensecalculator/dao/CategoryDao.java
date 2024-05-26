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
import com.expensecalculator.enums.TransactionType;
import com.expensecalculator.redis.RedisUtils;
import com.expensecalculator.service.MappingService;

public class CategoryDao {
	private final static String transaction_category="transaction_category:";
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
        String cacheKey = transaction_category + userId + ":" + transactionTypeId;
        
        String sql = "INSERT INTO transaction_category (category, adddate, userid, transaction_type_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnectionDAO.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            statement.setString(1, category);
            statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            statement.setInt(3, userId);
            statement.setInt(4, transactionTypeId);
            statement.executeUpdate();
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int categoryId = generatedKeys.getInt(1);
                    RedisUtils.addToCacheHset(cacheKey, categoryId, category);
                } else {
                    throw new SQLException("Creating category failed, no ID obtained.");
                }
            }
        }
    }
	
	public boolean updateCategory(String category, int categoryId, int userId, int transactionTypeId) throws ClassNotFoundException, SQLException {
		String cacheKey = transaction_category + userId + ":" + transactionTypeId;
		
	    String sql = "UPDATE transaction_category SET category = ?, adddate = ? WHERE transaction_category_id = ? AND userid = ?";
	    try (Connection connection = DatabaseConnectionDAO.getConnection();
	         PreparedStatement statement = connection.prepareStatement(sql)) {
	        
	        statement.setString(1, category);
	        statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
	        statement.setInt(3, categoryId);
	        statement.setInt(4, userId);
	        
	        int rowsAffected = statement.executeUpdate();
	        if (rowsAffected > 0) {
	        	RedisUtils.addToCacheHset(cacheKey, categoryId, category);
	        }
	        return rowsAffected > 0;
	    }
	}
	
	public boolean removeCategoryByUserId(int userid) throws ClassNotFoundException, SQLException {
		String cacheKey = transaction_category + userid + ":";
	    String sql = "delete from transaction_category where userid=? and transaction_category_id not in ( select categoryid from transaction where userid=? )";
	    try (Connection connection = DatabaseConnectionDAO.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
	        statement.setInt(1, userid);
	        statement.setInt(2, userid);
	        int rowsAffected = statement.executeUpdate();
	        if (rowsAffected > 0) {
	            RedisUtils.removeFromCacheHset(cacheKey+TransactionType.Expense.getTypeId());
	            RedisUtils.removeFromCacheHset(cacheKey+TransactionType.Income.getTypeId());
	        }
	        
	        return rowsAffected > 0;
	    }
	}
}
