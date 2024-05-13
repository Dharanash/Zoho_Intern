package online_shopping_system.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import online_shopping_system.POJO.User;
import online_shopping_system.enums.Role;

public class AdminService extends ManagerService {

	public void addManager() throws ClassNotFoundException, SQLException {
		User manager = createuser(Role.Manager);
		dbconnDao.addUser(manager);
		System.out.println("Manager created successfully\n");
	}

	public void viewAllOrders() throws SQLException, ClassNotFoundException {
		System.out.println(" Order History\n");
		ResultSet orders = dbconnDao.getAllOrders();
		System.out.println(
				"Order Id  User Id  Delivery Address\t Pincode\t" + "Product Name\t Quantity   Price   AddedTime\t Order Status");
		while (orders.next()) {
			int orderId = orders.getInt("orderid");
			String uname = orders.getString("name");
			String deliveryAddress = orders.getString("address");
			int pincode = orders.getInt("pincode");
			String productName = orders.getString("productname");
			int quantity = orders.getInt("quantity");
			double price = orders.getInt("price");
			Timestamp addedTime = orders.getTimestamp("addtime");
			String orderStatus = orders.getString("orderstatus");
			System.out.println(orderId + "\t" + uname + "\t" + deliveryAddress + "\t" + pincode + "\t" + productName
					+ "\t" + quantity + "\t" + price + "\t" + addedTime + "\t" + orderStatus);
		}
		orders.close();
		System.out.println();
	}
}
