package online_shopping_system.services;

import java.sql.ResultSet;
import java.sql.SQLException;

import online_shopping_system.POJO.User;
import online_shopping_system.POJO.Wallet;

public class MappingService {

	public static User mapToUser(ResultSet result) throws SQLException {
		User user = null;
		int userid = result.getInt("userid");
		String name = result.getString("name");
		String password = result.getString("password");
		int roleid = result.getInt("role");
		String email = result.getString("email");
		int phoneNumber = result.getInt("contactnumber");
		user = new User(userid, name, password, roleid, email, phoneNumber);

		return user;
	}

	public static Wallet mapToWallet(ResultSet result) throws SQLException {
		int walletId = result.getInt("walletid");
		double balance = result.getDouble("balance");
		int userId = result.getInt("userid");
		int point = result.getInt("points");

		return new Wallet(walletId, balance, userId, point);
	}
}
