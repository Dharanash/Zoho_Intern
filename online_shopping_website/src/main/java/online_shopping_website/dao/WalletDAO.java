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
	
	public void updateWallet(Wallet wallet) throws ClassNotFoundException, SQLException {
		try (Connection conn = DatabaseConnectionDAO.getConnection();
				PreparedStatement st = conn
						.prepareStatement("UPDATE wallet SET balance = ?, points = ? WHERE userid = ?")) {
			st.setDouble(1, wallet.balance);
			st.setInt(2, wallet.point);
			st.setInt(3, wallet.userId);

			st.executeUpdate();
		}
	}
}
