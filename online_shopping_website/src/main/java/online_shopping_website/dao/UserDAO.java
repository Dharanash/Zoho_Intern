package online_shopping_website.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import online_shopping_website.enums.*;
import online_shopping_website.model.User;
import online_shopping_website.services.EncryptionService;
import online_shopping_website.services.MappingService;

public class UserDAO {
	
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
		String sql = "INSERT INTO users (name, email, password, role, phonenumber) VALUES (?, ?, ?, ?, ?)";
		try (Connection connection = DatabaseConnectionDAO.getConnection(); PreparedStatement st = connection.prepareStatement(sql)) {

			st.setString(1, user.name);
			st.setString(2, user.email);
			st.setString(3, EncryptionService.encryptPassword(user.password));
			st.setInt(4, user.role.getRoleId());
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
	
	public void updateUser(User user) throws SQLException, ClassNotFoundException {
		String sql = "UPDATE users SET name = ?, password=?, phonenumber = ? WHERE userid = ?";
		try (Connection connection = DatabaseConnectionDAO.getConnection(); PreparedStatement st = connection.prepareStatement(sql)) {
			st.setString(1, user.name);
			st.setString(2, user.password);
			st.setString(3, user.phoneNumber);
			st.setInt(4, user.userId);
			st.executeUpdate();
		}
	}

	
	
}
