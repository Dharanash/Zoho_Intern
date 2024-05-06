package com.expensecalculator.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.expensecalculator.dto.User;
import com.expensecalculator.service.EncryptionService;
import com.expensecalculator.service.MappingService;

public class UserDao {
	public User checkUser(String userEmail, String password) throws ClassNotFoundException, SQLException {
		User user = null;
		String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
		try (Connection connection = DatabaseConnectionDAO.getConnection(); PreparedStatement st = connection.prepareStatement(sql)) {
			st.setString(1, userEmail);
			st.setString(2, EncryptionService.encryptPassword(password));
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				user = MappingService.mapToUser(rs);
			}
			return user;
		}

	}
	
	public void addUser(User user) throws SQLException, ClassNotFoundException {
		String sql = "INSERT INTO users (name, email, password, roleid, phonenumber) VALUES (?, ?, ?, ?, ?)";
		try (Connection connection = DatabaseConnectionDAO.getConnection(); PreparedStatement st = connection.prepareStatement(sql)) {

			st.setString(1, user.name);
			st.setString(2, user.email);
			st.setString(3, EncryptionService.encryptPassword(user.password));
			st.setInt(4, user.roleId);
			st.setString(5, user.phoneNumber);
			st.executeUpdate();
		}
	}
	
	public boolean isUserExist(String email) throws SQLException, ClassNotFoundException {
		String sql = "SELECT * FROM users WHERE LOWER(email) = LOWER(?)";
		try (Connection connection = DatabaseConnectionDAO.getConnection(); PreparedStatement st = connection.prepareStatement(sql)) {
			st.setString(1, email);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				return true;
			}
			return false;
		}
	}
}
