package online_shopping_website.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import online_shopping_website.model.*;
import online_shopping_website.services.*;

public class WalletDAO {
	public Wallet getWalletFromId(int userId) throws ClassNotFoundException, SQLException {
		Wallet wallet = null;
		try (Connection conn = DatabaseConnectionDAO. getConnection();
				PreparedStatement st = conn.prepareStatement("Select * from wallet WHERE userid = ?")) {
			st.setInt(1, userId);
			ResultSet rs = st.executeQuery();
			if (!rs.next()) {
				return addWallet(userId);
			}

			wallet = MappingService.mapToWallet(rs);
		}
		return wallet;
	}

	public Wallet addWallet(int userId) throws ClassNotFoundException, SQLException {
		Wallet wallet = null;
		try (Connection conn = DatabaseConnectionDAO.getConnection();
				PreparedStatement st = conn.prepareStatement(
						"INSERT INTO wallet (balance, userid, points) VALUES (?, ?, ?)")){
			wallet = new Wallet(userId);
			st.setDouble(1, wallet.balance); // Initial balance
			st.setInt(2, wallet.userId);
			st.setInt(3, wallet.point);
			st.executeUpdate();
		}

		return wallet;
	}
	
	public void addToWalletAmount(int userId, double amount) throws ClassNotFoundException, SQLException{
		String query="UPDATE wallet SET balance = balance+? WHERE userid = ?";
		updateWalletAmount(query, userId, Math.abs(amount));
	}
	
	public void reduceToWalletAmount(int userId, double amount) throws ClassNotFoundException, SQLException{
		String query="UPDATE wallet SET balance = balance-? WHERE userid = ?";
		updateWalletAmount(query, userId, Math.abs(amount));
	}
	
	public void redeemPointsToWalletAmount(int userId) throws ClassNotFoundException, SQLException{
		String query="UPDATE wallet SET balance = balance+points, points=0 WHERE userid = ?";
		try (Connection conn = DatabaseConnectionDAO.getConnection();
				PreparedStatement st = conn
						.prepareStatement(query)) {
			st.setInt(1, userId);

			st.executeUpdate();
		}
	}
	
	public void addToWalletPoints(int userId, int points) throws ClassNotFoundException, SQLException{
		String query="UPDATE wallet SET points=points+? WHERE userid = ?";
		try (Connection conn = DatabaseConnectionDAO.getConnection();
				PreparedStatement st = conn
						.prepareStatement(query)) {
			st.setInt(1,Math.abs(points));
			st.setInt(2, userId);

			st.executeUpdate();
		}
	}
	
	public void updateWalletAmount(String query, int userId, double amount) throws ClassNotFoundException, SQLException {
		try (Connection conn = DatabaseConnectionDAO.getConnection();
				PreparedStatement st = conn
						.prepareStatement(query)) {
			st.setDouble(1, Math.abs(amount));
			st.setInt(2, userId);

			st.executeUpdate();
		}
	}
	
}
