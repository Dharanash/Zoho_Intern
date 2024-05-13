package online_shopping_system.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import online_shopping_system.Exceptions.UserNotifyException;
import online_shopping_system.POJO.Product;
import online_shopping_system.POJO.Wallet;
import online_shopping_system.enums.OrderStatus;

public class ManagerService extends UserService {

	public void addProduct(int userId) throws ClassNotFoundException, SQLException {
		Product product = createProduct(userId);
		dbconnDao.addProduct(product);
		System.out.println("Product added successfully\n");
	}

	public Product createProduct(int createdBy) {
		String name = InputValidationService.getStringInput("Enter product name : ");
		String description = InputValidationService.getStringInput("Enter description : ");
		double price = InputValidationService.getDoubleInput("Enter price : ");
		int quantity = InputValidationService.getIntegerInput("Enter quantity : ");
		Product product = new Product(name, description, price, quantity, createdBy);

		return product;
	}

	public void updateProduct(int userid) throws ClassNotFoundException, SQLException, UserNotifyException {
		int pid = InputValidationService.getIntegerInput("Enter the product id : ");
		if (!dbconnDao.isProductExistInInventory(pid)) {
			throw new UserNotifyException("Given product does not exist.\n");
		}

		Product product = createProduct(userid);
		dbconnDao.updateProduct(pid, product);

		System.out.println("Product updated successfully\n");
	}

	public void removeProduct(int userId) throws ClassNotFoundException, SQLException, UserNotifyException {
		int pid = InputValidationService.getIntegerInput("Enter the product id : ");
		if (!dbconnDao.isProductExistInInventory(pid)) {
			throw new UserNotifyException("Given product does not exist.\n");
		}

		dbconnDao.removeProductInInventory(pid, userId);

		System.out.println("Product with id : " + pid + " removed successfully\n");
	}
	
	public void viewDispatchOrders(int userId) throws ClassNotFoundException, SQLException {
		ResultSet orders = dbconnDao.getOrdersForDispatch(userId);
		System.out.println(
				"Order Id  User Name  Delivery Address\t Pincode\t" + "Product Name\t Quantity   Price   AddedTime\t Order Status");
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
			System.out.println(orderId + "\t"+ uname + "\t"+ deliveryAddress + "\t" + pincode + "\t" + productName + "\t" + quantity
					+ "\t" + price + "\t" + addedTime + "\t" + orderStatus);

		}
		orders.close();
		System.out.println();
	}

	
	public void dispatchOrder(int userId) throws SQLException, ClassNotFoundException, UserNotifyException {
		viewDispatchOrders(userId);
		int orderId = InputValidationService.getIntegerInput("Enter order id : ");
		try (ResultSet orderSt = dbconnDao.getOrderFromId(orderId)) {
			if (!orderSt.next()) {
				throw new UserNotifyException("Given order id does not exist.");
			}
			String orderStatus = orderSt.getString("orderstatus");
			if (!orderStatus.equalsIgnoreCase(OrderStatus.Placed.toString())) {
				System.out.println("Order already " + orderStatus + "\n");
				return;
			}
			int quantity = orderSt.getInt("quantity");
			double price = orderSt.getDouble("price");
			int orderUserId = orderSt.getInt("userId");
			addPointsToWallet(orderUserId, quantity, price);
			dbconnDao.updateOrderStatus(orderId, OrderStatus.Dispatched);
			System.out.println("Order with id " + orderId + " dispatched for delivery.");
		}
	}
	
	public void addPointsToWallet(int userId,int quantity, double price) throws ClassNotFoundException, SQLException {
		Wallet wallet = dbconnDao.getWalletFromId(userId);
		wallet.point += (int)quantity*price / 10;
		dbconnDao.updateWallet(wallet);
	}

}
