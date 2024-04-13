package online_shopping_system.services;

import java.sql.ResultSet;
import java.sql.SQLException;

import online_shopping_system.POJO.User;
import online_shopping_system.enums.OrderStatus;
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

		while (orders.next()) {
			int orderId = orders.getInt("orderid");
			int userId = orders.getInt("userid");
			String status = orders.getString("status");
			int deliveryAddressId = orders.getInt("customer_detailsid");
			java.sql.Timestamp addedTime = orders.getTimestamp("addtime");

			System.out.println(orderId + "\t" + userId + "\t" + addedTime + "\t" + deliveryAddressId + "\t" + status);
		}
		orders.close();
		System.out.println();
	}

	public void approveOrder() throws SQLException, ClassNotFoundException {
		int orderId = InputValidationService.getIntegerInput("Enter order id : ");
		try(ResultSet orderSt = dbconnDao.getOrderFromId(orderId)){
			if(orderSt.next()) {
				String orderStatus = orderSt.getString("status");
				if(orderStatus.equalsIgnoreCase(OrderStatus.Cancelled.toString())) {
					System.out.println("Order cancelled.");
					return;
				}
				dbconnDao.updateOrderStatus(orderId, OrderStatus.Approved);
				System.out.println("Order with id " + orderId + " approved for delivery.");
			}
			else {
				System.out.println("Given order id does not exist.");
				return;
			}
		}
	}
}
